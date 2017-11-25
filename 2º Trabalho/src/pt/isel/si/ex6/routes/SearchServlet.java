package pt.isel.si.ex6.routes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static pt.isel.si.ex6.Utils.load;

public class SearchServlet extends RouteServlet {
    private static final String FORM = load("./src/pt/isel/si/ex6/views/Form.html");

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, Cookie cookie) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()))) {
            out.write(FORM);
        }
        resp.setStatus(200);
    }
}