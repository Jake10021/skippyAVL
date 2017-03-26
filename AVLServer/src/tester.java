import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.*;


public class tester {
	public static void main(String args[]){
		List<Vehicle> vehList = Collections.synchronizedList(new ArrayList<Vehicle>());
		Vehicle newVeh = new Vehicle(5,'A',35.8,-95.6,50.0,120.0);
		System.out.println(newVeh.toString());
		vehList.add(newVeh);
		System.out.print("Added vehicle 1!");
		System.out.println("Added at index: " + vehList.indexOf(newVeh));
		newVeh = new Vehicle(6,'A',35.8,-95.6,50.0,120.0);
		System.out.println(newVeh.toString());
		vehList.add(newVeh);
		System.out.print("Added vehicle 2!");
		System.out.println("Added at index: " + vehList.indexOf(newVeh));
		
		nmeaParse p = new nmeaParse();
		String testP = "$GPRMC,123519,A,4807.038,S,01131.000,W,022.4,084.4,230394,003.1,W,V101*6A";
		Vehicle test = p.parseUDP(testP);
		
		
		Gson gson = new GsonBuilder().create();
	    gson.toJson(test, System.out);
		//THIS IS COMMENT
		
	}
}
