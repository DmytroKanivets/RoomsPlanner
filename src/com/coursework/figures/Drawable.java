package com.coursework.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.coursework.files.XMLBuilder;
import com.coursework.files.XMLTag;

public abstract class Drawable {

	private int priority = Integer.MIN_VALUE;
	
	private Set<String> tags;
	
	public abstract void selfPaint(Graphics2D g, Color primaryColor);
//	TODO needed?
	public abstract Area getArea();
	
	public abstract void save(XMLBuilder builder);

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
