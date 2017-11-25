package pt.isel.si.routes;

import com.google.gson.Gson;
import pt.isel.si.entities.AccessInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GithubCallback extends RouteServlet {
    private static final Gson GSON = new Gson();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) throws IOException {
        String state = cookie.getValue();

        if (!req.getParameter("state").equals(state)){
            resp.setStatus(302);
            resp.setHeader("Location", "http://localhost:8080/search");
            return;
        }
        String code = req.getParameter("code");

        URL url = new URL("https://github.com/login/oauth/access_token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");

        PrintWriter output = new PrintWriter(
                new OutputStreamWriter(connection.getOutputStream()));
        output.print("code=" + code + "&");
        output.print("client_id=" + GithubServlet.CLIENT_ID + "&");
        output.print("client_secret=" + GithubServlet.CLIENT_SECRET + "&");
        output.print("redirect_uri=" + GithubServlet.REDIRECT_URI + "&");
        output.print("state=" + state);
        output.flush();

        BufferedReader input = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;StringBuilder result = new StringBuilder();

        while ((line = input.readLine()) != null) {
            result.append(line);
        }

        AccessInfo githubAccessInfo = GSON.fromJson(result.toString(), AccessInfo.class);

        githubUsersInfo.put(Integer.parseInt(state), githubAccessInfo);

        resp.setStatus(302);
        resp.setHeader("Location", "http://localhost:8080/search");
    }
}