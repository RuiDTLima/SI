package pt.isel.si.routes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends RouteServlet {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) throws IOException {
        int number = Integer.parseInt(cookie.getValue());

        googleUsersInfo.remove(number);
        githubUsersInfo.remove(number);

        resp.setStatus(302);
        resp.setHeader("Location", "http://localhost:8080/index");
    }
}