import java.io.*;
import java.net.*;


public class EchoClient {
	public static void main(String[] args) {
		try {
			String host;
			if (args.length > 0) {
				host = args[0];
			} else {
				host = "localhost";
			}
			Socket socket = new Socket(host, 8008);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			// send data to the server
			for (int i = 1; i <= 10; i++) {
				System.out.println("Sending: line " + i);
				out.println("line " + i);
				out.flush();
			}
			out.println("BYE");
			out.flush();
			// receive data from the server
			while (true) {
				String str = in.readLine();
				if (str == null) {
					break;
				} else {
					System.out.println(str);
				}
			}
		} catch (Exception e) {}
	}
}