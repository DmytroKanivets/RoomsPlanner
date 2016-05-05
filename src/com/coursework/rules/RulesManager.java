package com.coursework.rules;

public class RulesManager {

	private static RulesManager instance;
	
	private RulesManager() {};
	
	public static RulesManager getInstance() {
		if (instance == null)
			instance = new RulesManager();
		
		return instance;		
	}
	
	public void loadRules(String filename) {
		
	}
}
