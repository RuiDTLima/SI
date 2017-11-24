package pt.isel.si.routes;

import pt.isel.si.entities.GoogleAccessInfo;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public abstract class RouteServlet extends HttpServlet {
    public static HashMap<Integer, GoogleAccessInfo> usersInfo = new HashMap<>();

    public static int addUserInfo(GoogleAccessInfo googleAccessInfo) {
        Random rnd = new Random();
        int number = rnd.nextInt();
        usersInfo.put(number, googleAccessInfo);
        return number;
    }

    public boolean validateCookieRedirect(HttpServletRequest req, HttpServletResponse resp){
        Optional<Cookie> cookie = Arrays.stream(req.getCookies())
                .filter((c) -> c.getName().equals("accessInfoNumber"))
                .findFirst();

        if (RouteServlet.usersInfo.isEmpty() || !cookie.isPresent() || !usersInfo.containsKey(Integer.valueOf(cookie.get().getValue()))){
            resp.setStatus(302);
            resp.setHeader("Location", "http://localhost:8080/index");
            return false;
        }
        return true;
    }
}