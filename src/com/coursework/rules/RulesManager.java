package com.coursework.rules;

import java.util.List;

import com.coursework.editor.Scene;
import com.coursework.figures.Figure;

public class RulesManager {
	
	//TODO move to settings?
	private static final int DEFAULT_PRIORITY = 10;

	//TODO move to different classes?
	private List<PlacementRule> placementRules;
	private List<PriorityRule> priorityRules;
	
	public int getPriority(Figure f) {
		int priority = Integer.MIN_VALUE;
		for (PriorityRule rule : priorityRules) {
			
			if (rule.applicable(f)) {
				priority = Math.max(priority, rule.getPriority());
			}
			/*
			 * 
			List<String> tags = f.getTags();
			for (String tag: tags) {
				if (rule.containsTag(tag)) {
					priority = Math.max(priority, rule.getPriority());
				}
			}*/
		}
		return priority == Integer.MIN_VALUE ? DEFAULT_PRIORITY : priority;
	}
	
	public boolean canPlace(Figure f, Scene s) {
		boolean canPlace = true;
		
		for (PlacementRule rule : placementRules) {
			if (rule.applicable(f)) {
				
			}
		}
		return canPlace;
	}
}
