package pt.isel.si.routes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CalendarServlet extends RouteServlet {
    private static final String URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events?access_token=%s";
    private static final String BODY = "{\"end\":{\"date\":\"%s\"},\"start\":{\"date\":\"%s\"},\"summary\":\"%s\"}";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) throws IOException {
        int number = Integer.parseInt(cookie.getValue());
        URL url = new URL(String.format(URL, googleUsersInfo.get(number).access_token));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        String date = req.getParameter("due_on");
        String title = req.getParameter("title");

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            out.write(String.format(BODY, date, date, title));
        }

        resp.setStatus(303);
        resp.setHeader("Location", req.getHeader("Referer"));
    }
}