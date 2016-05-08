package com.coursework.figures;

import java.awt.Graphics2D;

import com.coursework.files.XMLTag;

public abstract class RectangleFigure extends ExtensibleFigure {

	public RectangleFigure(String figurePackage, String figureName) {
		super(figurePackage, figureName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}
/*
	@Override
	public XMLTag saveAtScene() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	@Override
	public void loadAtScene(XMLTag t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(int x, int y) {
		// TODO Auto-generated method stub

	}

}
