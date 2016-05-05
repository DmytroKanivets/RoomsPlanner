package com.coursework.files;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.coursework.rules.PlacementRule;
import com.coursework.rules.PriorityRule;

public class RulesLoader {
	String filename;
	
	List<PriorityRule> priorityRules;
	List<PlacementRule> placementRules;
	
	public RulesLoader(String filename) throws FileNotFoundException {
		this.filename = filename;
		
		loadRules();
	}
	
	public Collection<PriorityRule> getPriorityRules() {
		return priorityRules;
	}
	
	public Collection<PlacementRule> getPlacementRules() {
		return placementRules;
	}
	
	private void loadRules() throws FileNotFoundException {
		priorityRules = new LinkedList<>();
		placementRules = new LinkedList<>();
		
		XMLReader reader = new XMLReader(filename);
		
		Collection<XMLTag> rulesTags = reader.getRoot().getInnerTag("rules").getInnerTags();
		
		for (XMLTag tag : rulesTags) {
			if (tag.getName().equals("rule")) {
				//Collection<XMLTag> innerTags = tag.getInnerTags();
				/*
				Rule rule = null;
				
				switch (tag.getInnerTag("type").getContent()) {
				case "priority":
					rule = createPriorityRule(tag);
					priorityRules.add((PriorityRule)rule);
					break;
				case "container":
					rule = createContainerRule(tag);
					break;
				case "intersection":
					rule = createIntersectionRule(tag);
					break;
				default:
					Debug.log("Can't create rule with type " + tag.getInnerTag("type").getContent());
					break;
				}
				
				for (XMLTag inner : innerTags) {
					if (inner.getName().equals("tag")) {
						
					}
				}		*/		
			}
		}
	}
}
