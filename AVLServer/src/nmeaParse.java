import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class nmeaParse {
	
	
	public double latitudeToDecimal(String latitude, String dir){
		//check gitHub for how to store this 
		double num = Double.parseDouble(latitude.substring(2))/100.0;
		num += Double.parseDouble(latitude.substring(0, 2));
		
		//South numbers are negative
		if(dir.startsWith("S"))
			num = -num;
		
		return num;
	}
	public double longitudeToDecimal(String longitude, String dir){
		double num = Double.parseDouble(longitude.substring(3))/100.0;
		num += Double.parseDouble(longitude.substring(0, 3));
		
		//West numbers are negative
		if(dir.startsWith("W"))
			num = -num;
		
		return num;
	}
	public Vehicle parseUDP(String text){
		
		String[] tokens = text.split(",");
		
		Vehicle v = new Vehicle(Integer.parseInt(tokens[12].substring(0, 3)));
		//Vehicle v = new Vehicle(15);
		
		v.status = tokens[2].charAt(0);
		v.latitude = latitudeToDecimal(tokens[3], tokens[4]);
		v.longitude = longitudeToDecimal(tokens[5],tokens[6]);
		v.speed = Double.parseDouble(tokens[7]);
		v.heading = Double.parseDouble(tokens[8]);
		v.lastSeen = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		return v;
	}
	

}
