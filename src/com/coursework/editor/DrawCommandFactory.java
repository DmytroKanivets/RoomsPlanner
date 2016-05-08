package com.coursework.editor;

import com.coursework.figures.Drawable;

public interface DrawCommandFactory {
	public Command getCommand(Drawable d);
}
