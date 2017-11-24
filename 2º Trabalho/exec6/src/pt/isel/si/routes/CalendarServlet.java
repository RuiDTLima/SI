package pt.isel.si.routes;

import pt.isel.si.entities.GoogleAccessInfo;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public class CalendarServlet extends RouteServlet {
    private final String URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events?access_token=%s";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data = "{\"end\":{\"date\":\"%s\"},\"start\":{\"date\":\"%s\"},\"summary\":\"%s\"}";

        Optional<Cookie> cookie = Arrays.stream(req.getCookies())
                .filter((c) -> c.getName().equals("accessInfoNumber"))
                .findFirst();

        int number = Integer.parseInt(cookie.get().getValue());
        GoogleAccessInfo googleAccessInfo = usersInfo.get(number);
        String access_token = googleAccessInfo.access_token;

        URL url = new URL(String.format(URL, access_token));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        String date = req.getParameter("due_on");
        String title = req.getParameter("title");

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
        String body = String.format(data, date, date, title);
        wr.writeBytes(body);
        wr.flush();

        BufferedReader input = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }

        wr.close();
        input.close();

        resp.setStatus(303);
        resp.setHeader("Location", req.getHeader("Referer"));
    }
}