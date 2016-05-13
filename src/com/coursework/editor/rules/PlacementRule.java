package com.coursework.editor.rules;

import com.coursework.editor.figures.Drawable;

public class PlacementRule extends Rule {

	private int priority;
	
	public PlacementRule(int priority) {
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
