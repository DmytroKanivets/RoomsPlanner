package com.coursework.rules;

import com.coursework.editor.Drawable;

public class PriorityRule extends Rule {

	int priority;
	
	public PriorityRule(int priority) {
		this.priority = priority;
	}
	
	private boolean isApplicable(Drawable d) {
		if (d == null)
			return false;
		
		boolean result = false;
		for (String tag : tags) {
			result |= d.hasTag(tag);
		}
		return result;
	}
	
	@Override
	public Drawable processDrawable(Drawable d, Iterable<Drawable> context) {
		if (isApplicable(d) && priority > d.getPriority()) {
			d.setPriority(priority);
		}
		return d;
	}

}
