package com.coursework.rules;

import java.util.HashSet;
import java.util.Set;

import com.coursework.figures.Drawable;

public abstract class Rule {
	protected Set<String> tags;
	
	public Rule() {
		tags = new HashSet<>();
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	/*
	public boolean isApplicable(Drawable d) {
		if (d == null)
			return false;
		
		boolean result = false;
		for (String tag : tags) {
			result |= d.hasTag(tag);
		}
		return result;
	}
	*/
	public abstract Drawable processDrawable(Drawable d, Iterable<Drawable> context);
}
