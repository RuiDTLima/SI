package pt.isel.si.routes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OpenIDConnectServlet extends RouteServlet {
    /* the client id of your web client */
    public static final String CLIENT_ID
            = "451375149594-8ptgug89a53j6jeq1o29nkoimagi5s5v.apps.googleusercontent.com";

    /* the client secret of your web client */
    public static final String CLIENT_SECRET
            = "3SSsMvbiY6w0RTKO3mNhnnKk";

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
            "https://accounts.google.com/o/oauth2/v2/auth?"+
            "scope=openid%20email%20profile%20https://www.googleapis.com/auth/calendar&"+
            "redirect_uri="+REDIRECT_URI+"&"+
            "response_type=code&"+
            "prompt=consent&"+
            "client_id="+CLIENT_ID);
    }
}