import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class server {

	public static void main(String[] args) throws IOException {

		Listner listner = new Listner();
		Thread thread = new Thread(listner);
		thread.start();
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

