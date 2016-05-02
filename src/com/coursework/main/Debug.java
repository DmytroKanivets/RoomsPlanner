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
	
	private static void writeMessage(String message, String prefix) {
		FileWriter out;
		try {
			out = new FileWriter(logFilename, true);
			out.append(prefix + message + "\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//cant write
			e.printStackTrace();
		}
	}
	
	public static void error(String message) {
		Calendar now = Calendar.getInstance();
			
		String time = "[" + dateFormat.format(now.getTime()) + "]";
			
		writeMessage(message, time + "[ERR] ");
	}
	
	public static void log(String message) {
		Calendar now = Calendar.getInstance();
		
		String time = "[" + dateFormat.format(now.getTime()) + "]";
			
		writeMessage(message, time + "[LOG] ");
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
