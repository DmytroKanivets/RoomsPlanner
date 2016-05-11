package com.coursework.editor.figures;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.DrawableCommandFactory;
import com.coursework.editor.KeyboardState;
import com.coursework.files.XMLTag;

public abstract class Figure {
	
	private String figureName;
	private String figurePackage;
	
	private List<String> tags;
	
	protected DrawableCommandFactory addCommandFactory;
	
	public String getPackageName() {
		return figurePackage; 
	}
	
	public String getName() {
		return figureName;
	}
	
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setAddToSceneOperation(DrawableCommandFactory factory) {
		addCommandFactory = factory;
	}
	
	public BuilderFromXML getXMLBuilder() {
		return new FigureBuilder();
	}
	
	protected class FigureBuilder implements BuilderFromXML {

		@Override
		public void load(XMLTag tag) {
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
	
	public BuilderFromXML getDrawableLoader() {
		return new FigureBuilder();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public void drawStart() {}
	public void drawEnd() {}
	public void move(int x, int y) {}
	public void keyPressed(KeyboardState state) {}

	public abstract void rotate(double degree);
	
	public abstract void draw(Graphics2D g, int shiftX, int shiftY);
}
