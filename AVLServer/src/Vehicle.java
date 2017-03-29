import java.time.LocalTime;
import java.time.format.*;



public class Vehicle {

	int ident;
	char status;
	double latitude, longitude, speed, heading;
	String lastSeen;
	LocalTime time;
	
	public Vehicle (int vehicleID, char newStatus, double lat, double lon, double newSpeed, double newHead){
		ident = vehicleID;
		status = newStatus;
		latitude = lat;
		longitude = lon;
		speed = newSpeed;
		heading = newHead;
		lastSeen = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		time = LocalTime.now();
	}
	
	public Vehicle (int vehicleID){
		ident = vehicleID;
		time = LocalTime.now();
	}
	
	
	
	public void update(char newStatus, double lat, double lon, double newSpeed, double newHead){
		status = newStatus;
		latitude = lat;
		longitude = lon;
		speed = newSpeed;
		heading = newHead;
		lastSeen = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		time = LocalTime.now();
	}
	
	
	
	public String toString(){
		return "Ident: " + ident + ", Last Seen: " + lastSeen;
	}

	public boolean equals(Vehicle o){
		if (o.ident == ident) {
			return true;
		}
		else {
			return false;
		}
	}
	
	

}
