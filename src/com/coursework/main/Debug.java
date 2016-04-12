package com.coursework.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Debug {
	private static final String logFilename = "debug.log";
	private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	public static void clear() {
		try {
			FileWriter out = new FileWriter(logFilename);
			out.append("");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//cant clear
			e.printStackTrace();
		}
	}
	
	public static void log(String message) {
		FileWriter out;
		try {
			Calendar now = Calendar.getInstance();
			
			String time = "[" + dateFormat.format(now.getTime()) + "] ";
			
			out = new FileWriter(logFilename, true);
			out.append(time + message + "\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//cant write
			e.printStackTrace();
		}
	}
	
	public static String getAll() {
		StringBuilder sb = new StringBuilder();
		try {  
			BufferedReader br = new BufferedReader(new FileReader(logFilename));
			String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.getProperty("line.separator"));
	            line = br.readLine();
	        }
	        
	        br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//open
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//read
			e.printStackTrace();
		} 
        return sb.toString();
	}
	
}
