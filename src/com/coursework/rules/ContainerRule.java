package com.coursework.rules;

import java.awt.geom.Area;

import com.coursework.figures.Drawable;

public class ContainerRule extends PlacementRule{

	public ContainerRule(boolean allowed) {
		super(allowed);
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
