package com.coursework.rules;

import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.Scene;
import com.coursework.figures.Drawable;
import com.coursework.figures.Figure;

public class RulesManager {
	
	//TODO move to settings?
	private static final int DEFAULT_PRIORITY = 10;

	//TODO move to different classes?
	private List<PlacementRule> placementRules;
	private List<PriorityRule> priorityRules;
	
	private static RulesManager instance;
	
	public static RulesManager getInstance() {
		if (instance == null)
			instance = new RulesManager();
		return instance;
	}
	
	private RulesManager() {
		placementRules = new LinkedList<>();
		priorityRules = new LinkedList<>();
	}
	
	public int getPriority(Drawable f) {
		int priority = Integer.MIN_VALUE;
		for (PriorityRule rule : priorityRules) {
			if (rule.applicable(f)) {
				priority = Math.max(priority, rule.getPriority());
			}
		}
		System.out.println(priority == Integer.MIN_VALUE ? DEFAULT_PRIORITY : priority);
		return priority == Integer.MIN_VALUE ? DEFAULT_PRIORITY : priority;
	}
	
	public boolean canPlace(Figure f, Scene s) {
		boolean canPlace = true;
		
		for (PlacementRule rule : placementRules) {
			if (rule.applicable(f)) {
				//TODO
			}
		}
		return canPlace;
	}
	
	public void loadRules(String fileName) {
		
	}
}
