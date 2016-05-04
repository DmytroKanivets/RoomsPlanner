package com.coursework.figures;

import com.coursework.editor.AddToSceneCommand;
import com.coursework.editor.CommandFactory;
import com.coursework.editor.Drawable;
import com.coursework.files.XMLTag;

public abstract class Figure implements Drawable {
	
	private String figureName;
	private String figurePackage;
	private String figureClass;
	
	protected CommandFactory commandFactory;
	
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
	
	public void setAddToSceneOperation(CommandFactory factory) {
		commandFactory = factory;
	}

	//TODO remove it
	public abstract void loadAtScene(XMLTag t);
	
	public abstract void mouseDown();
	public abstract void mouseUp();
	public abstract void mousePositionChanged(int x, int y);
}
