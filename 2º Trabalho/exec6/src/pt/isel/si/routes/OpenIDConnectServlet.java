package pt.isel.si.routes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OpenIDConnectServlet extends RouteServlet {
    /* the client id of your web client */
    public static final String CLIENT_ID
            = "310972940538-uurrpitkk6po2fnm3gsrn5h8hke2aok1.apps.googleusercontent.com";

    /* the client secret of your web client */
    public static final String CLIENT_SECRET
            = "beP5MVxzEUgEOWm7A1bA5mHO";

    public static final String REDIRECT_URI
            = "http://localhost:8080/google-callback"; /* the callback uri id of your web client */

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("--New request was received --");
        System.out.println(req.getRequestURI());
        System.out.println(req.getMethod());
        System.out.println(req.getHeader("Accept"));

        resp.setStatus(302);
        resp.setHeader("Location",
            // google's authorization endpoint
            "https://accounts.google.com/o/oauth2/v2/auth?"+
            "scope=openid%20email%20profile&"+
            "redirect_uri="+REDIRECT_URI+"&"+
            "response_type=code&"+
            "client_id="+CLIENT_ID);
    }
}