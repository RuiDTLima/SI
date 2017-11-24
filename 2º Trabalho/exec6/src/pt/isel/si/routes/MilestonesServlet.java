package pt.isel.si.routes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pt.isel.si.entities.Milestone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MilestonesServlet extends RouteServlet {
    private static final Gson GSON = new Gson();
    private static final String MILESTONE = load("./src/pt/isel/si/views/Milestone.html");
    private static final String MILESTONEROW = load("./src/pt/isel/si/views/MilestoneRow.html");
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

        int status = connection.getResponseCode();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        StringBuilder result = new StringBuilder();

        while ((line = input.readLine()) != null)
            result.append(line);

        ArrayList<Milestone> milestones = GSON.fromJson(result.toString(), new TypeToken<ArrayList<Milestone>>(){}.getType());

        String html = milestones.stream().map(milestone -> String.format(MILESTONEROW,
                milestone.title,
                milestone.description,
                milestone.html_url,
                milestone.due_on.split("T")[0],
                milestone.due_on.split("T")[0],
                milestone.title))
                .collect(Collectors.joining());

        writer.write(String.format(MILESTONE, html));

        writer.close();
        input.close();
        resp.setStatus(200);
    }

    private static String load(String path){
        try {
            return Files.readAllLines(Paths.get(path))
                    .stream()
                    .collect(Collectors.joining());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}