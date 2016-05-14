package com.coursework.editor.rules;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.coursework.files.XMLReader;
import com.coursework.files.XMLTag;
import com.coursework.main.Debug;

public class RulesLoader {
	String filename;
	
	List<Rule> rules;
	
	public RulesLoader(String filename) throws FileNotFoundException {
		this.filename = filename;
		
		loadRules();

		Debug.log("Loading rules from " + filename);
	}
	
	public Collection<Rule> getRules() {
		return rules;
	}
	
	private void loadRules() throws FileNotFoundException {
		rules = new LinkedList<>();
		
		XMLReader reader = new XMLReader(filename);
		
		Collection<XMLTag> rulesTags = reader.getRoot().getInnerTag("rules").getInnerTags();
	
		for (XMLTag tag : rulesTags) {
			if (tag.getName().equals("rule")) {
				
				Rule rule = null;
				
				switch (tag.getInnerTag("type").getContent()) {
				case "priority":
					rule = new PriorityRule(Integer.parseInt(tag.getInnerTag("value").getContent()));
					break;
				
				case "container":
					rule = new ContainerRule(tag.getInnerTag("allowed").getContent().equals("true"));
					
				default:
					Debug.log("Can not load rule of type " + tag.getInnerTag("type").getContent());
					break;
				}
				
				Collection<XMLTag> innerTags = tag.getInnerTags();
				
				for (XMLTag inner : innerTags) {
					if (inner.getName().equals("tag")) {
						rule.addTag(inner.getContent());
					}
				}
				
				rules.add(rule);		
			}
		}
	}
}
