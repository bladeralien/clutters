import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.ReadHandler;
import org.glassfish.grizzly.filterchain.*;
import org.glassfish.grizzly.http.HttpClientFilter;
import org.glassfish.grizzly.http.HttpContent;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.io.NIOReader;
import org.glassfish.grizzly.http.io.NIOWriter;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.Header;
import org.glassfish.grizzly.impl.FutureImpl;
import org.glassfish.grizzly.impl.SafeFutureImpl;
import org.glassfish.grizzly.memory.Buffers;
import org.glassfish.grizzly.memory.MemoryManager;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by peiyuchao on 16/6/16.
 */
public class GrizzlyHTTPPOSTClientUnderConstruct {

    //            Client client = new Client();
//            client.run();
//    /**
//     * This handler using non-blocking streams to read POST data and echo it
//     * back to the client.
//     */
//    private static class NonBlockingEchoHandler extends HttpHandler {
//
//        @Override
//        public void service(final Request request,
//                            final Response response) throws Exception {
//
//            final char[] buf = new char[128];
//            final NIOReader in = request.getNIOReader(); // return the non-blocking InputStream
//            final NIOWriter out = response.getNIOWriter();
//
//            response.suspend();
//
//            // If we don't have more data to read - onAllDataRead() will be called
//            in.notifyAvailable(new ReadHandler() {
//
//                //                @Override
//                public void onDataAvailable() throws Exception {
//                    System.out.printf("[onDataAvailable] echoing %d bytes\n", in.readyData());
//                    echoAvailableData(in, out, buf);
//                    in.notifyAvailable(this);
//                }
//
//                //                @Override
//                public void onError(Throwable t) {
//                    System.out.println("[onError]" + t);
//                    response.resume();
//                }
//
//                //                @Override
//                public void onAllDataRead() throws Exception {
//                    System.out.printf("[onAllDataRead] length: %d\n", in.readyData());
//                    try {
//                        echoAvailableData(in, out, buf);
//                    } finally {
//                        try {
//                            in.close();
//                        } catch (IOException ignored) {
//                        }
//
//                        try {
//                            out.close();
//                        } catch (IOException ignored) {
//                        }
//
//                        response.resume();
//                    }
//                }
//            });
//
//        }
//
//        private void echoAvailableData(NIOReader in, NIOWriter out, char[] buf)
//                throws IOException {
//
//            while(in.isReady()) {
//                int len = in.read(buf);
//                out.write("==============================");
//                out.write(buf, 0, len);
//            }
//        }
//
//    } // END NonBlockingEchoHandler
//
//
//    private static final class ClientFilter extends BaseFilter {
//
//        private static final String[] CONTENT = {
//                "contentA-",
//                "contentB-",
//                "contentC-",
//                "contentD"
//        };
//
//        private FutureImpl<String> future;
//
//        private StringBuilder sb = new StringBuilder();
//
//        // ---------------------------------------------------- Constructors
//
//
//        private ClientFilter(FutureImpl<String> future) {
//            this.future = future;
//        }
//
//
//        // ----------------------------------------- Methods from BaseFilter
//
//
//        @SuppressWarnings({"unchecked"})
//        @Override
//        public NextAction handleConnect(FilterChainContext ctx) throws IOException {
//            System.out.println("\nClient connected!\n");
//
//            HttpRequestPacket request = createRequest();
//            System.out.println("Writing request:\n");
//            System.out.println(request.toString());
//            ctx.write(request); // write the request
//
//            // for each of the content parts in CONTENT, wrap in a Buffer,
//            // create the HttpContent to wrap the buffer and write the
//            // content.
//            MemoryManager mm = ctx.getConnection().getTransport().getMemoryManager();
//            for (int i = 0, len = CONTENT.length; i < len; i++) {
//                HttpContent.Builder contentBuilder = request.httpContentBuilder();
//                Buffer b = Buffers.wrap(mm, CONTENT[i]);
//                contentBuilder.content(b);
//                HttpContent content = contentBuilder.build();
//                System.out.printf("(Client writing: %s)\n", b.toStringContent());
//                ctx.write(content);
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            // since the request created by createRequest() is chunked,
//            // we need to write the trailer to signify the end of the
//            // POST data
//            ctx.write(request.httpTrailerBuilder().build());
//
//            System.out.println("\n");
//
//            return ctx.getStopAction(); // discontinue filter chain execution
//
//        }
//
//
//        @Override
//        public NextAction handleRead(FilterChainContext ctx) throws IOException {
//
//            HttpContent c = (HttpContent) ctx.getMessage();
//            Buffer b = c.getContent();
//            if (b.hasRemaining()) {
//                sb.append(b.toStringContent());
//            }
//
//            // Last content from the server, set the future result so
//            // the client can display the result and gracefully exit.
//            if (c.isLast()) {
//                future.result(sb.toString());
//            }
//            return ctx.getStopAction(); // discontinue filter chain execution
//
//        }
//
//
//        // ------------------------------------------------- Private Methods
//
//
//        private HttpRequestPacket createRequest() {
//
//            HttpRequestPacket.Builder builder = HttpRequestPacket.builder();
//            builder.method("POST");
//            builder.protocol("HTTP/1.1");
//            builder.uri("/post");
//            builder.chunked(true);
//            HttpRequestPacket packet = builder.build();
//            packet.addHeader(Header.Host, "localhost" + ':' + "8080");
//            return packet;
//
//        }
//
//    } // END Client
//
//
//    private static final class Client {
//
//        private static final String HOST = "localhost";
//        private static final int PORT = 8080;
//
//        public void run() throws IOException {
//            final FutureImpl<String> completeFuture = SafeFutureImpl.create();
//
//            // Build HTTP client filter chain
//            FilterChainBuilder clientFilterChainBuilder = FilterChainBuilder.stateless();
//            // Add transport filter
//            clientFilterChainBuilder.add(new TransportFilter());
//
//            // Add HttpClientFilter, which transforms Buffer <-> HttpContent
//            clientFilterChainBuilder.add(new HttpClientFilter());
//            // Add ClientFilter
//            clientFilterChainBuilder.add(new ClientFilter(completeFuture));
//
//
//            // Initialize Transport
//            final TCPNIOTransport transport =
//                    TCPNIOTransportBuilder.newInstance().build();
//            // Set filterchain as a Transport Processor
//            transport.setProcessor(clientFilterChainBuilder.build());
//
//            try {
//                // start the transport
//                transport.start();
//
//                Connection connection = null;
//
//                // Connecting to a remote Web server
//                Future<Connection> connectFuture = transport.connect(HOST, PORT);
//                try {
//                    // Wait until the client connect operation will be completed
//                    // Once connection has been established, the POST will
//                    // be sent to the server.
//                    connection = connectFuture.get(10, TimeUnit.SECONDS);
//
//                    // Wait no longer than 30 seconds for the response from the
//                    // server to be complete.
//                    String result = completeFuture.get(30, TimeUnit.SECONDS);
//
//                    // Display the echoed content
//                    System.out.println("\nEchoed POST Data: " + result + '\n');
//                } catch (Exception e) {
//                    if (connection == null) {
////                        LOGGER.log(Level.WARNING, "Connection failed.  Server is not listening.");
//                    } else {
////                        LOGGER.log(Level.WARNING, "Unexpected error communicating with the server.");
//                    }
//                } finally {
//                    // Close the client connection
//                    if (connection != null) {
//                        connection.close();
//                    }
//                }
//            } finally {
//                // stop the transport
//                transport.shutdownNow();
//            }
//        }
}
