import java.time.LocalTime;
import java.time.format.*;



public class Vehicle {

	int ident;
	char lastStatus;
	double lastLat, lastLon, lastSpeed, lastHeading;
	LocalTime lastSeen;
	
	public Vehicle (int vehicleID, char status, double lat, double lon, double speed, double heading){
		ident = vehicleID;
		lastStatus = status;
		lastLat = lat;
		lastLon = lon;
		lastSpeed = speed;
		lastHeading = heading;
		lastSeen = LocalTime.now();
	}
	
	public Vehicle (int vehicleID){
		ident = vehicleID;
	}
	
	
	
	public void update(char status, double lat, double lon, double speed, double heading){
		lastStatus = status;
		lastLat = lat;
		lastLon = lon;
		lastSpeed = speed;
		lastHeading = heading;
		lastSeen = LocalTime.now();		
	}
	
	
	
	public String toString(){
		return "Ident: " + ident + ", Last Seen: " + lastSeen.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
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
