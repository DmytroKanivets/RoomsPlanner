package com.coursework.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.coursework.rules.ContainerRule;
import com.coursework.rules.PriorityRule;
import com.coursework.rules.Rule;

public class RulesLoader {
	String filename;
	
	List<Rule> rules;
	
	public RulesLoader(String filename) throws FileNotFoundException {
		this.filename = filename;
		
		loadRules();
	}
	
	public Collection<Rule> getRules() {
		return rules;
	}
	
	private void loadRules() throws FileNotFoundException {
		rules = new LinkedList<>();
		
		XMLReader reader = new XMLReader(filename);
		
		Collection<XMLTag> rt = reader.getRoot().getInnerTags();
		System.out.println(new File(filename).getAbsolutePath());
		for (XMLTag t : rt) {
			System.out.println(t.getName());
		}
		
		Collection<XMLTag> rulesTags = reader.getRoot().getInnerTag("rules").getInnerTags();
		
		for (XMLTag tag : rulesTags) {
			if (tag.getName().equals("rule")) {
				
				System.out.println("Loading tag " + tag.getInnerTag("type").getContent());
				
				Rule rule = null;
				
				switch (tag.getInnerTag("type").getContent()) {
				case "priority":
					rule = new PriorityRule(Integer.parseInt(tag.getInnerTag("value").getContent()));
					break;
				
				case "container":
					rule = new ContainerRule(tag.getInnerTag("allowed").getContent().equals("true"));
					
				default:
					break;
				}
				
				Collection<XMLTag> innerTags = tag.getInnerTags();
				
				for (XMLTag inner : innerTags) {
					if (inner.getName().equals("tag")) {
						rule.addTag(inner.getContent());
					}
				}
				
				rules.add(rule);
				
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
