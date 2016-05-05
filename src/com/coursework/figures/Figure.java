package com.coursework.figures;

import java.awt.geom.Area;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.CommandFactory;
import com.coursework.files.XMLTag;

public abstract class Figure extends Drawable {
	
	private String figureName;
	private String figurePackage;
	
	private List<String> tags;
	
	protected CommandFactory commandFactory;
	
	public Figure(String figurePackage, String figureName) {
		this.figurePackage = figurePackage;
		this.figureName = figureName;
		tags = new LinkedList<>();
	}
	
	public String getPackageName() {
		return figurePackage; 
	}
	
	public List<String> getTags() {
		return tags;
	}

	public String getName() {
		return figureName;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	public void setAddToSceneOperation(CommandFactory factory) {
		commandFactory = factory;
	}

	public abstract Area getArea();
	
	//TODO remove it
	public abstract void loadAtScene(XMLTag t);
	
	public abstract void mouseDown();
	public abstract void mouseUp();
	public abstract void mousePositionChanged(int x, int y);
}
