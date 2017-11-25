package pt.isel.si.routes;

import pt.isel.si.entities.AccessInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CalendarServlet extends RouteServlet {
    private final String URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events?access_token=%s";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) throws IOException {
        String data = "{\"end\":{\"date\":\"%s\"},\"start\":{\"date\":\"%s\"},\"summary\":\"%s\"}";

        int number = Integer.parseInt(cookie.getValue());
        AccessInfo googleAccessInfo = googleUsersInfo.get(number);
        String access_token = googleAccessInfo.access_token;

        URL url = new URL(String.format(URL, access_token));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        String date = req.getParameter("due_on");
        String title = req.getParameter("title");

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        String body = String.format(data, date, date, title);
        wr.writeBytes(body);
        wr.close();

        resp.setStatus(303);
        resp.setHeader("Location", req.getHeader("Referer"));
    }
}