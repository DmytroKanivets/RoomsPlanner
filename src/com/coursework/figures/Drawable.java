package com.coursework.figures;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.coursework.files.XMLTag;

public abstract class Drawable {

	private int priority;
	
	private Set<String> tags;
	
	public abstract void selfPaint(Graphics2D g);
	
//	TODO remove?
	public abstract XMLTag saveAtScene();

	public Drawable() {
		tags = new HashSet<>();
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	public void addTag(String s) {
		tags.add(s);
	}
	
	public void addAllTags(Collection<String> tags) {
		this.tags.addAll(tags);
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int p) {
		priority = p;
	}
}
