package com.coursework.files;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PropertyContainer {
	
	private Map<String, List<String>> data;
	
	PropertyContainer() {
		data = new HashMap<String, List<String>>();
	}
	
	void add(String name, String value) {
		if (!data.containsKey(name)) {
			data.put(name, new LinkedList<String>());
		}
		
		data.get(name).add(value);
	}
	
	public String getProperty(String name) {
		if (!data.containsKey(name))
			return null;
	
		return data.get(name).get(0);
	}
	
	public String[] getAllProperties(String name) {
		if (!data.containsKey(name))
			return null;
	
		return data.get(name).toArray(new String[]{});
	}
}
