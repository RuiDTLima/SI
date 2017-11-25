package pt.isel.si.routes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static pt.isel.si.Utils.load;

public class IndexServlet extends HttpServlet {
    private static final String LOGIN = load("./src/pt/isel/si/views/Index.html");
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()))) {
            out.write(LOGIN);
        }
        resp.setStatus(200);
    }
}