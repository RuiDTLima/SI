package pt.isel.si.routes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoogleServlet extends HttpServlet {
    public static final String CLIENT_ID = "451375149594-8ptgug89a53j6jeq1o29nkoimagi5s5v.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "3SSsMvbiY6w0RTKO3mNhnnKk";
    public static final String REDIRECT_URI = "http://localhost:8080/google-callback";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(302);
        resp.setHeader("Location",
            "https://accounts.google.com/o/oauth2/v2/auth?" +
            "scope=openid%20email%20profile%20https://www.googleapis.com/auth/calendar&" +
            "redirect_uri=" + REDIRECT_URI + "&" +
            "response_type=code&" +
            "prompt=consent&" +
            "client_id=" + CLIENT_ID);
    }
}