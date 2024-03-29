package pt.isel.si.ex6;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pt.isel.si.ex6.routes.*;

public class HttpServer {

   private static final int LISTEN_PORT = 8080;

   public static void main(String[] args) throws Exception {
       String portDef = System.getenv("PORT");
       int port = portDef != null ? Integer.valueOf(portDef) : LISTEN_PORT;
       Server server = new Server(port);

       ServletHandler handler = new ServletHandler();
       server.setHandler(handler);

       handler.addServletWithMapping(new ServletHolder(new IndexServlet()), "/index");

       // make authentication request
       handler.addServletWithMapping(new ServletHolder(new GoogleServlet()), "/google-openid");

       // handle authentication response
       handler.addServletWithMapping(new ServletHolder(new GoogleCallback()), "/google-callback");

       // handle search forms
       handler.addServletWithMapping(new ServletHolder(new SearchServlet()), "/search");

       // handle presentation of results
       handler.addServletWithMapping(new ServletHolder(new MilestonesServlet()), "/milestones");

       // to add event to calendar
       handler.addServletWithMapping(new ServletHolder(new CalendarServlet()), "/calendar");

       // to logout
       handler.addServletWithMapping(new ServletHolder(new LogoutServlet()), "/logout");

       // response from github
       handler.addServletWithMapping(new ServletHolder(new GithubServlet()), "/github-servlet");

       // response from github
       handler.addServletWithMapping(new ServletHolder(new GithubCallback()), "/github-callback");

       server.start();
       server.join();
   }
}