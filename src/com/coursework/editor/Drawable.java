package com.coursework.editor;

import java.awt.Graphics2D;

import com.coursework.files.XMLTag;

public interface Drawable {

	public void selfPaint(Graphics2D g);
	public XMLTag getXMLTag(); 
}
