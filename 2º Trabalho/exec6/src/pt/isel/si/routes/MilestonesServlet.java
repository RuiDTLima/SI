package pt.isel.si.routes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pt.isel.si.entities.Milestone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static pt.isel.si.Utils.load;

public class MilestonesServlet extends RouteServlet {
    private static final Gson GSON = new Gson();
    private static final String MILESTONE = load("./src/pt/isel/si/views/Milestone.html");
    private static final String MILESTONEROW = load("./src/pt/isel/si/views/MilestoneRow.html");
    private static final String URL = "https://api.github.com/repos/%s/%s/milestones%s";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) throws IOException {
        int number = Integer.parseInt(cookie.getValue());

        URL url = new URL(String.format(URL, req.getParameter("owner"), req.getParameter("repo"),
                githubUsersInfo.containsKey(number) ? ("?access_token=" + githubUsersInfo.get(number).access_token) : ""));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        if (connection.getResponseCode() == 404) {
            resp.sendError(404, connection.getResponseMessage());
            return;
        }

        StringBuilder result;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null)
                result.append(line);
        }

        ArrayList<Milestone> milestones = GSON.fromJson(result.toString(), new TypeToken<ArrayList<Milestone>>(){}.getType());

        String html = milestones.stream().map(milestone -> String.format(MILESTONEROW,
                milestone.title,
                milestone.description,
                milestone.html_url,
                milestone.html_url,
                milestone.due_on.split("T")[0],
                milestone.due_on.split("T")[0],
                milestone.title))
                .collect(Collectors.joining());

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()))) {
            out.write(String.format(MILESTONE, html));
        }
        resp.setStatus(200);
    }
}