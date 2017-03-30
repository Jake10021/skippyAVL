import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;


public class tester_jake {
	public static void main(String args[]) {
		
		
		try{
		String xmltest = new String();
		xmltest = "<AV L><vehicles>active</vehicles></AVL>";
		System.out.println(xmltest);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xmltest));
	    Document doc = builder.parse(is);
	    NodeList nodes = doc.getChildNodes();
	    if(nodes.item(0).getNodeName()=="AVL" && nodes.item(0).getChildNodes().item(0).getNodeName() == "vehicles"){
		    System.out.println(doc.getDocumentElement().getTextContent());
	    }
	    else{
	    	System.out.println("Invalid request from TCP client");
	    }
		}
		catch (SAXException e) {
		    
		} catch (IOException e) {
		
		} catch (ParserConfigurationException e) {
			
		}
	  
	     
		//THIS IS COMMENT
		
	}
}
