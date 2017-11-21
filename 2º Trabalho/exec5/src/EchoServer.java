import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class EchoServer {

    private static final int LISTEN_PORT = 8080;

    public static void main(String[] args) {
        String portDef = System.getenv("PORT");
        int port = portDef != null ? Integer.valueOf(portDef) : LISTEN_PORT;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(Factory.getKeyManagers("resources/localhost.pfx"), Factory.getTrustManagers("resources/CA1.jks"), null);
            do {
                try (
                        SSLServerSocket serverSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(port);
                        SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    clientSocket.setWantClientAuth(true);
                    char[] buffer = new char[1024];
                    while (in.read(buffer) > 0) {
                        String body = new String(buffer).trim();
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-type: text/plain");
                        out.println("Content-length: " + body.length() * 2);
                        out.println();
                        out.println(body);
                        out.println();
                    }
                } catch (IOException e) {
                    System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            } while (true);
        } catch (NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException | KeyStoreException | IOException e) {
            e.printStackTrace();
        }
    }
}
