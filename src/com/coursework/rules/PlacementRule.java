package com.coursework.rules;

public abstract class PlacementRule extends Rule {
	protected boolean allowed;
	
	public PlacementRule(boolean allowed) {
		this.allowed = allowed;
	}
	
	String containerTag;
	String contentTag;
	
	@Override 
	public void addTag(String tag) {
		if (containerTag == null)
			containerTag = tag;
		else 
			contentTag = tag;	
	}
}
