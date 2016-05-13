package com.coursework.editor.rules;

import java.awt.geom.Area;

import com.coursework.editor.figures.Drawable;

public class ContainerRule extends Rule{

	private boolean allowed;
	
	private String containerTag;
	private String contentTag;
	
	
	
	public ContainerRule(boolean allowed) {
		this.allowed = allowed;
	}

	@Override 
	public void addTag(String tag) {
		if (containerTag == null)
			containerTag = tag;
		else 
			contentTag = tag;	
	}
	
	@Override
	public String toString() {
		String result = super.toString();
		return result + ':' + (allowed ? "allowed" : "disallowed " + containerTag + " " + contentTag);
	}
	
	@Override
	public Drawable processDrawable(Drawable drawable, Iterable<Drawable> context) {
		if (drawable != null) {
			if (allowed) {
				if (drawable.hasTag(contentTag)) {
					boolean canPlace = false;
					for (Drawable d : context) {
						if (d.hasTag(containerTag)) {
							Area a = new Area(drawable.getArea());
							a.subtract(d.getArea());
							if (a.isEmpty()) {
								canPlace = true;
							}
						}
					}
					return canPlace ? drawable : null;
				}
			} else {
				String pair = null;
				
				if (drawable.hasTag(containerTag)) {
					pair = contentTag;
				}
				if (drawable.hasTag(contentTag)) {
					pair = containerTag;
				}
				
				if (pair != null) {
					boolean hasIntersection = false;
					for (Drawable d : context) {
						if (d.hasTag(pair) && d != drawable) {
							Area a = new Area(drawable.getArea());
							a.intersect(d.getArea());
							if (!a.isEmpty()) {
								hasIntersection = true;
							}
						}
					}
					return hasIntersection ? null : drawable;
				}
			}
		}
			
		return drawable;
	}


}
