package pt.isel.si.routes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GithubServlet extends RouteServlet {
    public static final String CLIENT_ID = "c24702b46d74c97b7d5e";
    public static final String CLIENT_SECRET = "fc31efe7e5cb4669c7031d05c6bdb7c0a20632e0";
    public static final String REDIRECT_URI = "http://localhost:8080/github-callback";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) {
        resp.setStatus(302);
        resp.setHeader("Location", "https://github.com/login/oauth/authorize?" +
                "client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=repo" +
                "&state=" + cookie.getValue() +
                "&allow_signup=false");
    }
}