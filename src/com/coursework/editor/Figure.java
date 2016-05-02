package com.coursework.editor;

import java.awt.geom.Area;

import com.coursework.files.XMLTag;

public abstract class Figure implements Drawable {
	
	String figureName;
	String figurePackage;
	String figureClass;
	
	public Figure(String figurePackage, String figureClass, String figureName) {
		this.figurePackage = figurePackage;
		this.figureClass = figureClass;
		this.figureName = figureName;
	}
	
	public String getPackageName() {
		return figurePackage; 
	}

	public String getName() {
		return figureName;
	}
	
	public String get—lassName() {
		return figureClass;				
	}
		
	public abstract void addArea(Area a);
	public abstract void subtractArea(Area a);

	//public abstract Drawable getDrawableComponent();
	public abstract void load(XMLTag t);
	
	//public abstract void mouseClicked();
	public abstract void mouseDown();
	public abstract void mouseUp();
	public abstract void positionChanged(int x, int y);
}
