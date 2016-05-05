package com.coursework.editor;

import java.awt.Graphics2D;
import java.util.List;

import com.coursework.files.XMLTag;

public interface Drawable {

	public void selfPaint(Graphics2D g);
	
//	public List<String> getTags();
//	TODO remove?
	public XMLTag saveAtScene();

	public boolean hasTag(String tag);
}
