package pt.isel.si.ex5;

import javax.net.ssl.*;
import java.io.*;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;

public class EchoClient {

    private static final int LISTEN_PORT = 8080;

    public static void main(String[] args) throws IOException, KeyStoreException {
        if (args.length != 1) {
            System.out.println("Usage: java EchoClient <host name>");
            return;
        }
        String hostName = args[0];
        String portDef = System.getenv("PORT");
        int port = portDef != null ? Integer.valueOf(portDef) : LISTEN_PORT;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(Factory.getKeyManagers("resources/Bob_1.pfx"), Factory.getTrustManagers("resources/CA1.jks"), null);
            try (
                    SSLSocket echoSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(hostName, port);
                    PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
            ) {
                echoSocket.setNeedClientAuth(true);
                char[] buffer = new char[1024];
                while (!stdIn.readLine().equalsIgnoreCase("end")) {
                    out.println(httpRequest());
                    in.read(buffer);
                    System.out.println("echo: " + new String(buffer).trim());
                }
            } catch (UnknownHostException e) {
                System.out.println("Don't know about host " + hostName);
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Couldn't get I/O for the connection to " + hostName);
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private static String httpRequest(){
        return "GET / HTTP/1.0\r\n\r\n";
    }
}