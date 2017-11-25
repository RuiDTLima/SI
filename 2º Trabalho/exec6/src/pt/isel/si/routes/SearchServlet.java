package pt.isel.si.routes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SearchServlet extends RouteServlet {
    private static final String FORM = load("./src/pt/isel/si/views/Form.html");

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
        out.write(FORM);
        out.close();
        resp.setStatus(200);
    }
}