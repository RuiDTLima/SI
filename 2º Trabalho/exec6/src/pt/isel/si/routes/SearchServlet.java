package pt.isel.si.routes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SearchServlet extends RouteServlet {
    private static final String FILENAME = "./src/pt/isel/si/views/Form.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!validateCookieRedirect(req, resp)){
            return;
        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
        Path path = Paths.get(FILENAME);
        String text = new String(Files.readAllBytes(path));
        writer.write(text);
        writer.close();
        resp.setStatus(200);
    }
}
