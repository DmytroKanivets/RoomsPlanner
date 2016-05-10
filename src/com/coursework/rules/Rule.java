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

	public abstract Drawable processDrawable(Drawable d, Iterable<Drawable> context);
}
