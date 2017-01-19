import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.ReadHandler;
import org.glassfish.grizzly.filterchain.*;
import org.glassfish.grizzly.http.HttpClientFilter;
import org.glassfish.grizzly.http.HttpContent;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.io.NIOReader;
import org.glassfish.grizzly.http.io.NIOWriter;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.impl.FutureImpl;
import org.glassfish.grizzly.impl.SafeFutureImpl;
import org.glassfish.grizzly.memory.Buffers;
import org.glassfish.grizzly.memory.MemoryManager;
import org.glassfish.grizzly.http.util.Header;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


/**
 * Created by peiyuchao on 16/6/16.
 */


public class GrizzlyHTTPServer {

    public static void main(String[] args) {

        HttpServer server = HttpServer.createSimpleServer();
        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            public void service(Request request, Response response) throws Exception {
                final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
                final String date = format.format(new Date(System.currentTimeMillis()));
                response.setContentType("text/plain");
                response.setContentLength(date.length());
                response.getWriter().write(date);}
        }, "/time");

        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            public void service(Request request, Response response) throws Exception {
                Thread.sleep(10000);
                response.setContentType("text/plain");
                response.setContentLength(request.toString().length());
                response.getWriter().write(request.toString());}
        }, "/slow");

        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            public void service(Request request, Response response) throws Exception {
                Integer sum = Integer.parseInt(request.getParameter("a")) + Integer.parseInt(request.getParameter("b"));
                response.setContentType("text/plain");
                response.setContentLength(sum.toString().length());
                response.getWriter().write(sum.toString());}
        }, "/calc/add");

        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            public void service(Request request, Response response) throws Exception {
                if (request.getMethod().getMethodString().equals("POST")) {
                    System.out.println("POST!");
                    String responseString = "";
                    for (String parameter : request.getParameterNames()) {
                        System.out.println(parameter);
                        System.out.println(request.getParameter(parameter));
                        responseString += "\nvalue of " + parameter + " is:\n" + request.getParameter(parameter);
                    }
                    response.setContentType("text/plain");
                    response.setContentLength(responseString.length());
                    response.getWriter().write(responseString);
                }
            }
        }, "/postform");

        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            public void service(Request request, Response response) throws Exception {
                if (request.getMethod().getMethodString().equals("POST")) {
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(request.getInputStream()));
                    StringBuffer result = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line + "\n");
                    }
                    String resultString = result.toString().trim();
                    resultString = "xml from post: \n" + resultString;
                    System.out.println(resultString);
                    response.setContentType("text/plain");
                    response.setContentLength(resultString.length());
                    response.getWriter().write(resultString);
                }
            }
        }, "/postxml");

        try {
            server.start();
            System.out.println("Press any key to stop the server...");
            System.in.read();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
