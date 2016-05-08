package com.coursework.figures;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.DrawCommandFactory;
import com.coursework.editor.KeyboardState;
import com.coursework.files.XMLTag;

public abstract class Figure {
	
	private String figureName;
	private String figurePackage;
	
	private List<String> tags;
	
	protected DrawCommandFactory addCommandFactory;
	
	public Figure(String figurePackage, String figureName) {
		this.figurePackage = figurePackage;
		this.figureName = figureName;
		tags = new LinkedList<>();
	}
	
	public String getPackageName() {
		return figurePackage; 
	}
	
	public String getName() {
		return figureName;
	}
	
	public List<String> getTags() {
		return tags;
	}

	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	public void setAddToSceneOperation(DrawCommandFactory factory) {
		addCommandFactory = factory;
	}

	
	//protected abstract Area ggetArea();
	
	//TODO move it
	public abstract void loadAtScene(XMLTag t);
	
	public void drawStart() {}
	public void drawEnd() {}
	public void move(int x, int y) {}
	public void keyPressed(KeyboardState state) {}

	public abstract void rotate(double degree);
	
	public abstract void draw(Graphics2D g);
}
