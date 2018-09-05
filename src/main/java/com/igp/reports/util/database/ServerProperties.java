package com.igp.reports.util.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ServerProperties {
	public  static Map<String,String >  properties;

	public static void initiate() throws IOException {
		properties=new HashMap<String, String>();
		BufferedReader in = new BufferedReader(new FileReader("/opt/properties/start.ini"));
		//BufferedReader in = new BufferedReader(new FileReader("/home/nikhil/nikhil/pem/api.ini"));
		//BufferedReader in = new BufferedReader(new FileReader("/home/nikhil/nikhil/pem/apicopy.ini"));
		String line = "";
		while ((line = in.readLine()) != null) {
			if(!(line.contains("#")) && (!(line.equals("")))){
				String parts[] = line.split("=");
				if(parts[0].equals("ALLOWED_AUTH_KEYS")){
					String pp[]=parts[1].split(",");
					properties.put("HEADER_KEY", pp[0]);
				}
				else{
					properties.put(parts[0], parts[1]);
				}
			}
		}
		in.close();
	}
	public static String getPropertyValue(String value){
		String propertyValue="";
		propertyValue=properties.get(value);
		return propertyValue;
	}

}
