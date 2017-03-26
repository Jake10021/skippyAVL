import java.time.LocalTime;
import java.time.format.*;



public class Vehicle {

	int ident;
	char status;
	double latitude, longitude, speed, heading;
	String lastSeen;
	
	public Vehicle (int vehicleID, char newStatus, double lat, double lon, double newSpeed, double newHead){
		ident = vehicleID;
		status = newStatus;
		latitude = lat;
		longitude = lon;
		speed = newSpeed;
		heading = newHead;
		lastSeen = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}
	
	public Vehicle (int vehicleID){
		ident = vehicleID;
	}
	
	
	
	public void update(char newStatus, double lat, double lon, double newSpeed, double newHead){
		status = newStatus;
		latitude = lat;
		longitude = lon;
		speed = newSpeed;
		heading = newHead;
		lastSeen = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));		
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
