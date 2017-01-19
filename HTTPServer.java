import java.io.*;
import java.net.*;
import java.util.*;


public class HTTPServer {

	Socket incoming = null;
	BufferedReader in = null;
	DataOutputStream out = null;

	public HTTPServer(Socket incomingPara) {
		incoming = incomingPara;
	}

	public void sendResponse(String statusString, String responseString) {
		try {
			out.writeBytes("HTTP/1.1 " + statusString + "\r\n");
			out.writeBytes("Server: Java HTTPServer\r\n");
			out.writeBytes("Content-Type: text/html" + "\r\n");
			String response = "<html>" + "<title>HTTP Server in java</title>" + "<body>" + responseString + "</body>" + "</html>";
			out.writeBytes("Content-Length: " + response.length() + "\r\n");
			out.writeBytes("\r\n");
			out.writeBytes(response);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handle() {
		try {
			in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			out = new DataOutputStream(incoming.getOutputStream());
			String requestString = in.readLine();
			String headerLine = requestString;
			StringTokenizer tokenizer = new StringTokenizer(headerLine);
			String httpMethod = tokenizer.nextToken();
			String httpQueryString = tokenizer.nextToken();
			StringBuffer responseBuffer = new StringBuffer();
			while (in.ready()) {
				responseBuffer.append(requestString + "<BR>");
				requestString = in.readLine();
			}
			if (httpMethod.equals("GET")) {
				String[] pathParas = httpQueryString.split("\\?");
				String path = null;
				String paras = null;
				if (pathParas.length == 1) {
					path = pathParas[0];
				}
				if (pathParas.length == 2) {
					path = pathParas[0];
					paras = pathParas[1];
				}
				if (path.equals("/")) {
					sendResponse("200 OK", responseBuffer.toString());
				} else if (path.equals("/calc/add")) {
					Integer sum = 0;
					for (String addend: paras.split("&")) {
						sum += Integer.parseInt(addend.split("=")[1]);
					}
					sendResponse("200 OK", Integer.toString(sum));
				} else if (path.equals("/slow")) {
					Thread.sleep(10000);
					sendResponse("200 OK", responseBuffer.toString());
				} else {
					sendResponse("404 Not Found", "404 Not Found");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendResponse("404 Not Found", "404 Not Found");
		}
	}

	public static void main(String[] args) throws Exception {
		ServerSocket s = new ServerSocket(8008);
		while (true) {
			Socket incoming = s.accept();
			(new HTTPServer(incoming)).handle();
		}
	}
}