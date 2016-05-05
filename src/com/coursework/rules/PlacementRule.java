package com.coursework.rules;

import java.util.Collection;

import com.coursework.figures.Figure;

public abstract class PlacementRule extends Rule {
	public abstract boolean isAllowed(Figure figure, Collection<Figure> context);
}
