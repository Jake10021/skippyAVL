import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;


public class tester_jake {
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
		
		String response = new String("");
		response = response.concat("{\"status\":\"active\",\"vehicles\":[");
		
		 Gson gson = new GsonBuilder().create();
		 for (Vehicle temp : vehList) {
			 response = response.concat(gson.toJson(temp));
			}
		 response.substring(0,response.length()-1);
	     
		 response = response.concat("]}");
	     
	     System.out.println(response);
	     
		//THIS IS COMMENT
		
	}
}
