import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class server {
	private static int maxConnections=5;
	
	public static void main(String[] args) throws IOException {
		
		List<Vehicle> vehList = Collections.synchronizedList(new ArrayList<Vehicle>());
		Listner listner = new Listner();
		Thread thread = new Thread(listner);
		thread.start();
		
		int i=0;

	    try{
	      ServerSocket listener = new ServerSocket(9099);
	      Socket server;

	      while((i++ < maxConnections) || (maxConnections == 0)){
	    	System.out.println("Waiting for TCP connection...");
	        doComms connection;
	        server = listener.accept();
	        doComms conn_c= new doComms(server);
	        Thread t = new Thread(conn_c);
	        t.start();
	        System.out.println("New client connected.");
	      }
	    } catch (IOException ioe) {
	      System.out.println("IOException on socket listen: " + ioe);
	      ioe.printStackTrace();
	    }
	  }
	}
	


class Listner implements Runnable {
	public void run() {
		String text = null;
		while (true) {
			text = null;
			int server_port = 9099;
			byte[] message = new byte[1500];
			DatagramPacket p = new DatagramPacket(message, message.length);
			DatagramSocket s = null;
			try {
				s = new DatagramSocket(server_port);
			} catch (SocketException e) {
				e.printStackTrace();
				System.out.println("Socket excep");
			}
			try {
				s.receive(p);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IO EXcept");
			}
			text = new String(message, 0, p.getLength());
			System.out.println("message = " + text);
			System.out.println(calcChecksum(text));
			s.close();

		}
	}
	

	private static String calcChecksum(String in) {
	    int checksum = 0;
	    if (in.startsWith("$")) {
	        in = in.substring(1, in.length());
	    }

	    int end = in.indexOf('*');
	    if (end == -1)
	        end = in.length();
	    for (int i = 0; i < end; i++) {
	        checksum = checksum ^ in.charAt(i);
	    }
	    String hex = Integer.toHexString(checksum);
	    if (hex.length() == 1)
	        hex = "0" + hex;
	    return hex.toUpperCase();
	}
}



class doComms implements Runnable {
    private Socket server;
    private String line,input;

    doComms(Socket server) {
      this.server=server;
    }

    public void run () {

      input="";

      try {
        // Get input from the client
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        PrintStream out = new PrintStream(server.getOutputStream());

        while((line = in.readLine()) != null) {
          input=input + line;
          System.out.println("I got:" + line);
          out.println("Overall message is:" + input);
        }
   

        server.close();
      } catch (IOException ioe) {
        System.out.println("IOException on socket listen: " + ioe);
        ioe.printStackTrace();
      }
    }
}
