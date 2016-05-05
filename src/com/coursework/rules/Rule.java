package com.coursework.rules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coursework.figures.Figure;

public abstract class Rule {
	protected Set<String> tags;
	
	public Rule() {
		tags = new HashSet<>();
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public boolean containsTag(String tag) {
		if (tags.contains("all")) {
			return true;
		}
		
		return tags.contains(tag);
	}
	
	public boolean applicable(Figure f) {
		
		List<String> tags = f.getTags();
		
		for (String tag : tags) {
			if (this.tags.contains(tag)) {
				return true;
			}
		}
		
		return false;
	}
}
