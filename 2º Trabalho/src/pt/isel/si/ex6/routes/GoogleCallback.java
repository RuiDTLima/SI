package pt.isel.si.ex6.routes;

import com.google.gson.Gson;
import pt.isel.si.ex6.entities.AccessInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleCallback extends HttpServlet {
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

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            out.write("code=" + code + "&");
            out.write("client_id=" + GoogleServlet.CLIENT_ID + "&");
            out.write("client_secret=" + GoogleServlet.CLIENT_SECRET + "&");
            out.write("redirect_uri=" + GoogleServlet.REDIRECT_URI + "&");
            out.write("grant_type=authorization_code");
        }

        StringBuilder result;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null)
                result.append(line);
        }

        AccessInfo googleAccessInfo = GSON.fromJson(result.toString(), AccessInfo.class);

        Cookie cookie = new Cookie("accessInfoNumber", String.valueOf(RouteServlet.addGoogleInfo(googleAccessInfo)));
        cookie.setMaxAge(SESSIONAGE);
        resp.addCookie(cookie);
        resp.setStatus(302);
        resp.setHeader("Location", "http://localhost:8080/search");
    }
}