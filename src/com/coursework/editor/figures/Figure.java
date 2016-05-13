package com.coursework.editor.figures;

import com.coursework.editor.KeyboardState;
import com.coursework.files.XMLTag;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Figure {
	public static final double ROTATION_STEP = 5.0;
	
	private String figureName;
	private String figurePackage;
	
	private List<String> tags;
	
	public String getPackageName() {
		return figurePackage; 
	}
	
	public String getName() {
		return figureName;
	}
	
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	List<String> getTags() {
		return tags;
	}
	
	public BuilderFromXML getXMLBuilder() {
		return new FigureBuilder();
	}
	
	public void selected() {}
	
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

	protected abstract class DrawableLoader implements BuilderFromXML {

	}

	public abstract BuilderFromXML getDrawableLoader();
	
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
