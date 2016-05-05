package com.coursework.editor;

import com.coursework.figures.Drawable;

public interface CommandFactory {
	public Command getCommand(Drawable d);
}
