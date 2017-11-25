package pt.isel.si.routes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class IndexServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()))) {
            out.write("<a href=/google-openid> Login </a>");
        }
        resp.setStatus(200);
    }
}