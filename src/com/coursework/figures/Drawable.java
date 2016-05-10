package com.coursework.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.coursework.files.XMLBuilder;

public abstract class Drawable {

	private int priority = Integer.MIN_VALUE;
	
	private Set<String> tags;
	
	public Drawable() {
		tags = new HashSet<>();
	}
	
	public void addTag(String s) {
		tags.add(s);
	}
	
	public void addAllTags(Collection<String> tags) {
		this.tags.addAll(tags);
	}
	
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	public void setPriority(int p) {
		priority = p;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public abstract void move(int x, int y);
	
	public abstract Area getArea();
	
	public abstract void selfPaint(Graphics2D g, Color primaryColor, int shiftX, int shiftY);
	
	public abstract void save(XMLBuilder builder);	
}
