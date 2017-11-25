package pt.isel.si.ex6.routes;

import com.google.gson.Gson;
import pt.isel.si.ex6.entities.AccessInfo;

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

        URL url = new URL("https://github.com/login/oauth/access_token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            out.write("code=" + req.getParameter("code") + "&");
            out.write("client_id=" + GithubServlet.CLIENT_ID + "&");
            out.write("client_secret=" + GithubServlet.CLIENT_SECRET + "&");
            out.write("redirect_uri=" + GithubServlet.REDIRECT_URI + "&");
            out.write("state=" + state);
        }

        StringBuilder result;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null)
                result.append(line);
        }

        githubUsersInfo.put(Integer.parseInt(state), GSON.fromJson(result.toString(), AccessInfo.class));

        resp.setStatus(302);
        resp.setHeader("Location", "http://localhost:8080/search");
    }
}