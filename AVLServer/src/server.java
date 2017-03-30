import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalTime;
import java.time.*;

public class server {
	private static int maxConnections = 5;
	public static List<Vehicle> vehList = Collections.synchronizedList(new ArrayList<Vehicle>());

	public static void main(String[] args) throws IOException {

		Listner listner = new Listner(vehList);// working with listner
												// constructor
		Thread thread = new Thread(listner);

		thread.start();

		int i = 0;

		try {
			ServerSocket listener = new ServerSocket(9099);
			Socket server;

			while ((i++ < maxConnections) || (maxConnections == 0)) {
				System.out.println("Waiting for TCP connection...");
				doComms connection;
				server = listener.accept();
				doComms conn_c = new doComms(server, vehList);
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

	// public List<Vehicle> vehList = Collections.synchronizedList(new
	// ArrayList<Vehicle>());
	List<Vehicle> vehList;

	public Listner(List<Vehicle> vl) {
		vehList = vl;
	}

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
			nmeaParse nmeaP = new nmeaParse();
			// System.out.println("message = " + text);
			// System.out.println(calcChecksum(text));

			String[] tokens = text.split(",");
			String chckSum = tokens[12].substring(4).trim();
			// check for valid checksum
			if (calcChecksum(text).equals(chckSum)) {

				Vehicle v = nmeaP.parseUDP(text);
				boolean contains = false;

				synchronized (vehList) {
					for (Vehicle vehicle : vehList) {
						if (vehicle.ident == v.ident) {
							contains = true;

							// vehicle = nmeaP.parseUDP(text);
							vehicle.update(v.status, v.latitude, v.longitude, v.speed, v.heading);
							// System.out.println(v.time);
							System.out.println("updated vehicle");
							break;
						}
					}
					if (contains == false) {
						vehList.add(v);
						System.out.println("vehicle added.");
					}

				}

				System.out.println(vehList + "\n");

			} else {
				System.out.println("Invalid checksum : " + chckSum + "\n");
			}

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
	private String line, input;
	LocalTime currentTime;
	List<Vehicle> vehList;

	doComms(Socket server, List<Vehicle> vl) {
		this.server = server;
		vehList = vl;
	}

	public void run() {

		input = "";

		try {
			// set timeout to 30 seconds, socket closes if no request for update
			// after 30 seconds
			server.setSoTimeout(30000);

			// Get input from the client
			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			PrintStream out = new PrintStream(server.getOutputStream());
			
			//read messages as they are received
			while ((line = in.readLine()) != null) {

				input = input + line;
				System.out.println("Received Message:" + line);

				//Update vehicle timeouts before sending any reply
				List<Vehicle> tempRemove = new ArrayList<Vehicle>();
				synchronized (vehList) {
					Duration seconds;
					//Find timeout vehicles and add to list for removal
					for (Vehicle vehicle : vehList) {
						currentTime = LocalTime.now();
						seconds = Duration.between(vehicle.lastSeen, currentTime);

						if (seconds.getSeconds() >= 10) {
							tempRemove.add(vehicle);
							// vehList.remove(vehicle);
							System.out.println("vehicle timeout Ident:" + vehicle.ident + "\n");
						}
					}
				}
				//remove all timeout vehicles at one time
				vehList.removeAll(tempRemove);

				// XML PARSING
				String filter = new String();
				try {
					//Setup XML parases
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					factory.setValidating(false);
					factory.setNamespaceAware(true);
					DocumentBuilder builder = factory.newDocumentBuilder();
					builder.setErrorHandler(null);
					//Read XML from received message as string
					InputSource is = new InputSource(new StringReader(line));
					Document doc = builder.parse(is);
					//Get nodes
					NodeList nodes = doc.getChildNodes();
					//Check for proper format
					if (nodes.item(0).getNodeName() == "AVL"
							&& nodes.item(0).getChildNodes().item(0).getNodeName() == "vehicles") {
						//apply filter if valid
						if (doc.getDocumentElement().getTextContent().equals("all")
								|| doc.getDocumentElement().getTextContent().equals("active")) {
							filter = doc.getDocumentElement().getTextContent();
						} else {
							filter = "invalid";
						}
					} else {
						filter = "invalid";
					}
				} catch (Throwable e) {
					filter = "invalid";
				}

				String response = new String("");
				response = response.concat("{\"status\":\"" + filter + "\",\"vehicles\":[");
				
				if(!filter.equals("invalid")){
				//Build JSON for each vehicle in list
				Gson gson = new GsonBuilder().create();
				synchronized (vehList) {
					for (Vehicle temp : vehList) {
						if (filter.equals("all")) {
							response = response.concat(gson.toJson(temp)) + ",";
						} else if (filter.equals("active") && temp.status == 'A') {
							response = response.concat(gson.toJson(temp)) + ",";
						}
					}

					if (!vehList.isEmpty()) {
						response = response.substring(0, response.length() - 1);
					}
				}
				}
				response = response.concat("]}");

				out.println(response);
				System.out.println(response);
			}

			server.close();
			System.out.println("Client disconnected...");
		} catch (SocketTimeoutException ste) {					//Catch TCP server timeout
			System.out.println("Client timed out...");
			try {
				//gracefully close connection on timeout
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException ioe) {
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
	}
}
