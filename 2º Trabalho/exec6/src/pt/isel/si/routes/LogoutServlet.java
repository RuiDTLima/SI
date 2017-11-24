package pt.isel.si.routes;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class LogoutServlet extends RouteServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Cookie> cookie = Arrays.stream(req.getCookies())
                .filter((c) -> c.getName().equals("accessInfoNumber"))
                .findFirst();

        cookie.ifPresent(cookie1 -> RouteServlet.usersInfo.remove(Integer.parseInt(cookie1.getValue())));

        resp.setStatus(302);
        resp.setHeader("Location", "http://localhost:8080/index");
    }
}