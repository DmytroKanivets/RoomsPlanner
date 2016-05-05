package com.coursework.rules;

import java.util.Collection;

import com.coursework.figures.Drawable;
import com.coursework.figures.Figure;

public class ContainerRule extends PlacementRule{

	String current;

	@Override
	public void addTag(String tag) {
		if (current != null) {
			tags.add(current);
		}
		current = tag;
	}

	@Override
	public boolean applicable(Drawable f) {
		return f.hasTag(current);
		
	}
	
	@Override
	public boolean isAllowed(Figure figure, Collection<Figure> context) {
		// TODO Auto-generated method stub
		return false;
	}
}
