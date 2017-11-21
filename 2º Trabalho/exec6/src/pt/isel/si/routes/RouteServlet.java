package pt.isel.si.routes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

public abstract class RouteServlet extends HttpServlet {
    private static Cookie cookie;

    public static void setCookie(Cookie cookie) {
        RouteServlet.cookie = cookie;
    }

    public boolean validateCookieRedirect(HttpServletRequest req, HttpServletResponse resp){
        Optional<Cookie> cookie = Arrays.stream(req.getCookies())
                .filter((c) -> c.getName().equals("access_token"))
                .findFirst();

        if (RouteServlet.cookie == null || !cookie.isPresent() || !cookie.get().getValue().equals(RouteServlet.cookie.getValue())){
            resp.setStatus(302);
            resp.setHeader("Location", "http://localhost:8080/index");
            return false;
        }
        return true;
    }

    public static byte[] getHash(byte[] file) {
        try {
            return MessageDigest.getInstance("SHA-1").digest(file);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}