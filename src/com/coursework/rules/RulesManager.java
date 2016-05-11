package com.coursework.rules;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.Drawable;
import com.coursework.editor.ScenesManager;

public class RulesManager {

	private static RulesManager instance;
	
	private RulesManager() {
		rules = new LinkedList<>();
	}
	
	private List<Rule> rules;
	
	public Drawable processDrawable(Drawable d) {
		Iterable<Drawable> context = ScenesManager.instance().getDrawables();
		Drawable result = d;
		for (Rule rule : rules) {
			result = rule.processDrawable(result, context);
		}
		return result;
	}
	
	public static RulesManager getInstance() {
		if (instance == null)
			instance = new RulesManager();
		
		return instance;		
	}
	
	public void loadRules(String filename) throws FileNotFoundException {
		RulesLoader loader = new RulesLoader(filename);
		
		rules.addAll(loader.getRules());
	}
}
