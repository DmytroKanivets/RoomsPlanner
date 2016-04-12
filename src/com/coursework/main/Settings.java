package com.coursework.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
	
	private static Settings instance;
	
	private Properties properties;
	
	private static final String configFilename = "config.properties";
	
	private Settings() {

		properties = new Properties();

		try {
			FileInputStream file = new FileInputStream(configFilename);
			properties.load(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//from file.open
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//from prop.load
			e.printStackTrace();
		}
	}
	
	public static void init() {
		if (instance == null)
			instance = new Settings();
	}
	
	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}
	
	public void set(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public String get(String key) {
		return properties.getProperty(key, "");
	}
	
	public void save() {
		try {
			FileOutputStream out = new FileOutputStream(configFilename);
			properties.store(out, null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//open
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//write
			e.printStackTrace();
		}
	}
}
