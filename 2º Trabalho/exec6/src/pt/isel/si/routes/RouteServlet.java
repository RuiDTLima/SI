package pt.isel.si.routes;

import pt.isel.si.entities.AccessInfo;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public abstract class RouteServlet extends HttpServlet {
    public static HashMap<Integer, AccessInfo> googleUsersInfo = new HashMap<>();
    public static HashMap<Integer, AccessInfo> githubUsersInfo = new HashMap<>();

    public static int addGoogleInfo(AccessInfo googleAccessInfo) {
        Random rnd = new Random();
        int number = rnd.nextInt();
        googleUsersInfo.put(number, googleAccessInfo);
        return number;
    }

    public void doExecute(HttpServletRequest req, HttpServletResponse resp){
        Optional<Cookie> cookie = Arrays.stream(req.getCookies())
                .filter((c) -> c.getName().equals("accessInfoNumber"))
                .findFirst();

        if (RouteServlet.googleUsersInfo.isEmpty() || !cookie.isPresent() || !googleUsersInfo.containsKey(Integer.valueOf(cookie.get().getValue()))){
            resp.setStatus(302);
            resp.setHeader("Location", "http://localhost:8080/index");
            return;
        }

        try {
            execute(req, resp, cookie.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doExecute(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doExecute(req, resp);
    }

    public abstract void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) throws IOException;
}