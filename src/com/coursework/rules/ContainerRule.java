package com.coursework.rules;

import java.awt.geom.Area;

import com.coursework.figures.Drawable;

public class ContainerRule extends PlacementRule{

	public ContainerRule(boolean allowed) {
		super(allowed);
	}

	@Override
	public Drawable processDrawable(Drawable drawable, Iterable<Drawable> context) {
		if (drawable != null && allowed && drawable.hasTag(contentTag)) {
			//System.out.println("allow");
			boolean canPlace = false;
			for (Drawable d : context) {
				if (d.hasTag(containerTag)) {
					Area a = new Area(drawable.getArea());
					a.subtract(d.getArea());
					//System.out.println(a.isEmpty());
					//System.out.println(a.getBounds2D().toString());
					//System.out.println("Found wall");
					if (a.isEmpty()) {
						
						//System.out.println("try");//try
						canPlace = true;
					} else {
						//System.out.println(a.getBounds().toString());
					}
				}
			}
			if (!canPlace) {
				//System.out.println("Deny " + drawable.toString());
			}
			return canPlace ? drawable : null;
				
		}
		if (drawable != null && !allowed) {
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
					if (d.hasTag(pair)) {
						Area a = new Area(drawable.getArea());
						a.intersect(d.getArea());
						//System.out.println(a.isEmpty());
						//System.out.println(a.getBounds2D().toString());
						
						if (!a.isEmpty()) {
							//System.out.println("Can place");
							hasIntersection = true;
						}
					}
				}
				return hasIntersection ? null : drawable;
			}
		
		}
			
		return drawable;
	}


}
