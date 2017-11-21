package pt.isel.si.routes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MilestonesServlet extends RouteServlet {
    private final String URL = "https://api.github.com/repos/%s/%s/milestones";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!validateCookieRedirect(req, resp)){
            return;
        }

        System.out.println("--New request was received --");
        System.out.println(req.getRequestURI());
        System.out.println(req.getMethod());
        System.out.println(req.getHeader("Accept"));

        URL url = new URL(String.format(URL, req.getParameter("owner"), req.getParameter("repo")));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));

        BufferedReader input = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = input.readLine()) != null) {
            writer.write(line);
        }

        writer.close();
        input.close();
        resp.setStatus(200);
    }
}