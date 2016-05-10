package com.coursework.editor;

import com.coursework.figures.Drawable;

public interface DrawableCommandFactory {
	public Command getCommand(Drawable d);
}
