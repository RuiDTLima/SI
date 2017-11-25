package pt.isel.si.routes;

import com.google.gson.Gson;
import pt.isel.si.entities.AccessInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static pt.isel.si.routes.OpenIDConnectServlet.CLIENT_ID;

public class OpenIDConnectCallback extends HttpServlet {
    private static final Gson GSON = new Gson();
    private static final int SESSIONAGE = -1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");

        URL url = new URL("https://www.googleapis.com/oauth2/v3/token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        PrintWriter output = new PrintWriter(
                new OutputStreamWriter(connection.getOutputStream()));
        output.print("code=" + code + "&");
        output.print("client_id=" + CLIENT_ID + "&");
        output.print("client_secret=" + OpenIDConnectServlet.CLIENT_SECRET + "&");
        output.print("redirect_uri=" + OpenIDConnectServlet.REDIRECT_URI + "&");
        output.print("grant_type=authorization_code");
        output.flush();

        BufferedReader input = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;StringBuilder result = new StringBuilder();

        while ((line = input.readLine()) != null) {
            result.append(line);
        }

        AccessInfo googleAccessInfo = GSON.fromJson(result.toString(), AccessInfo.class);

        Cookie tempCookie = new Cookie("accessInfoNumber", String.valueOf(RouteServlet.addGoogleInfo(googleAccessInfo)));
        tempCookie.setMaxAge(SESSIONAGE);
        resp.addCookie(tempCookie);

        output.close();
        input.close();

        resp.setStatus(302);
        resp.setHeader("Location", "http://localhost:8080/search");
    }
}