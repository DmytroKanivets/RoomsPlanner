package com.coursework.rules;

import java.util.Collection;

import com.coursework.figures.Drawable;
import com.coursework.figures.Figure;

public class ContainerRule extends PlacementRule{

	@Override
	public boolean isAllowed(Figure figure, Collection<Figure> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Drawable processDrawable(Drawable d, Collection<Drawable> context) {
		// TODO Auto-generated method stub
		return null;
	}


}
