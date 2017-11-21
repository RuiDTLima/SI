package pt.isel.si.routes;

import com.google.gson.Gson;
import pt.isel.si.entity.AccessInfo;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenIDConnectCallback extends RouteServlet {
    private static final Gson GSON = new Gson();
    private static final int SESSIONAGE = -1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        System.out.println("Authorization code is = " + code);

        System.out.println("Send code to token endpoint");
        URL url = new URL("https://www.googleapis.com/oauth2/v3/token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        PrintWriter output = new PrintWriter(
                new OutputStreamWriter(connection.getOutputStream()));
        output.print("code=" + code + "&");
        output.print("client_id=" + OpenIDConnectServlet.CLIENT_ID + "&");
        output.print("client_secret=" + OpenIDConnectServlet.CLIENT_SECRET + "&");
        output.print("redirect_uri=" + OpenIDConnectServlet.REDIRECT_URI + "&");
        output.print("grant_type=authorization_code");
        output.flush();

        BufferedReader input = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line, result = "";

        while ((line = input.readLine()) != null) {
            System.out.println(line);
            result += line;
        }

        AccessInfo accessInfo = GSON.fromJson(result, AccessInfo.class);

        Cookie tempCookie = new Cookie("access_token", RouteServlet.getHash(accessInfo.access_token.getBytes()).toString());    //
        tempCookie.setMaxAge(SESSIONAGE);

        RouteServlet.setCookie(tempCookie);

        resp.addCookie(tempCookie);

        /* exchange 'code' by 'id_token' and 'access_token' */
        System.out.println("Collect access_token in the response");

        output.close();
        input.close();

        resp.setStatus(302);
        resp.setHeader("Location",
                // google's authorization endpoint
                "http://localhost:8080/search");
    }
}