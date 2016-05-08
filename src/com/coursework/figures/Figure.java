package com.coursework.figures;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.DrawCommandFactory;
import com.coursework.editor.KeyboardState;
import com.coursework.files.PropertyContainer;
import com.coursework.files.XMLTag;
import com.coursework.rules.ContainerRule;

public abstract class Figure {
	
	private String figureName;
	private String figurePackage;
	
	private List<String> tags;
	
	protected DrawCommandFactory addCommandFactory;
	/*
	public Figure(String figurePackage, String figureName) {
		this.figurePackage = figurePackage;
		this.figureName = figureName;
		tags = new LinkedList<>();
	}
	*/
	/*
	public Figure(PropertyContainer container) {
		figurePackage = container.getProperty("figurePackage");
		figureName = container.getProperty("figureName");
		tags = new LinkedList<>();
		tags.addAll(Arrays.asList(container.getAllProperties("tag")));		
	}
	*/
	
	public String getPackageName() {
		return figurePackage; 
	}
	
	public String getName() {
		return figureName;
	}
	
	public List<String> getTags() {
		return tags;
	}
/*
	public void addTag(String tag) {
		tags.add(tag);
	}
*/	
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	public void setAddToSceneOperation(DrawCommandFactory factory) {
		addCommandFactory = factory;
	}

	
	//protected abstract Area ggetArea();
	
	//TODO change to builder
	public abstract void loadAtScene(XMLTag t);
	
	public BuilderFromXML getXMLBuilder() {
		//System.out.println("return builder");
		return new FigureBuilder();
	}
	
	protected class FigureBuilder implements BuilderFromXML {

		@Override
		public void build(XMLTag tag) {
			figurePackage = tag.getInnerTag("package").getContent();
			figureName = tag.getInnerTag("name").getContent();
			
			tags = new LinkedList<>();
			
			Collection<XMLTag> inner = tag.getInnerTags();
			for (XMLTag t : inner) {
				if (t.getName().equals("tag")) {
					tags.add(t.getContent());
				}
			}
		}
		
	} 
	
	public void drawStart() {}
	public void drawEnd() {}
	public void move(int x, int y) {}
	public void keyPressed(KeyboardState state) {}

	public abstract void rotate(double degree);
	
	public abstract void draw(Graphics2D g);
}
