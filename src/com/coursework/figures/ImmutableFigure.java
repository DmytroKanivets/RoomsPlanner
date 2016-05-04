package com.coursework.figures;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import com.coursework.editor.Drawable;
import com.coursework.files.XMLTag;

public class ImmutableFigure extends Figure {

	private Area area;
	
	private int prevX;
	private int prevY;
	
	public ImmutableFigure(String figurePackage, String figureClass, String figureName) {
		super(figurePackage, figureClass, figureName);
	}

	public void addArea(Area a) {
		if (area == null) {
			area = a;
		} else {
			area.add(a);
		}
	}

	public void subtractArea(Area a) {
		if (area == null) {
			//Ignore
		} else {
			area.subtract(a);
		}
	}

	@Override
	public void selfPaint(Graphics2D g) {
		g.draw(area);
	}
	
	@Override
	public void mousePositionChanged(int x, int y) {
		AffineTransform transform = new AffineTransform();;
		transform.setToIdentity();
		transform.translate(x - this.prevX, y - this.prevY);
		this.prevX = x;
		this.prevY = y;
		area.transform(transform);
	}

private class DrawableRepresentation implements Drawable {
		
		int x;
		int y;
		
		Area a;
		
		public DrawableRepresentation(Area area, int xPos, int yPos) {
			a = new Area(area);
			x = xPos;
			y = yPos;
		}
		
		@Override
		public void selfPaint(Graphics2D g) {
			g.draw(a);
		}

		@Override
		public XMLTag getXMLTag() {
			XMLTag tag = new XMLTag(null);
			tag.setName("figure");
			
			XMLTag name = new XMLTag(tag);
			name.setName("figureName");
			name.addContent(getName());
			tag.addInnerTag(name);
		
			XMLTag pack = new XMLTag(tag);
			pack.setName("figurePackage");
			pack.addContent(getPackageName());
			tag.addInnerTag(pack);
		
			XMLTag xTag = new XMLTag(tag);
			xTag.setName("x");
			xTag.addContent(Integer.toString(x));
			tag.addInnerTag(xTag);
		
			XMLTag yTag = new XMLTag(tag);
			yTag.setName("y");
			yTag.addContent(Integer.toString(y));
			tag.addInnerTag(yTag);
			
			return tag;
		}
		
	}
	
	private void addToScene() {
		commandFactory.getCommand().execute(new DrawableRepresentation(area, prevX, prevY));
	}
	
	@Override
	public void mouseDown() {
		this.addToScene();
	}

	@Override
	public void mouseUp() {
		//Ignore
	}

	@Override
	public XMLTag getXMLTag() {
		XMLTag tag = new XMLTag(null);
		tag.setName("figure");
		
		XMLTag name = new XMLTag(tag);
		name.setName("figureName");
		name.addContent(getName());
		tag.addInnerTag(name);
	
		XMLTag pack = new XMLTag(tag);
		pack.setName("figurePackage");
		pack.addContent(getPackageName());
		tag.addInnerTag(pack);
	
		XMLTag xTag = new XMLTag(tag);
		xTag.setName("x");
		xTag.addContent(Integer.toString(prevX));
		tag.addInnerTag(xTag);
	
		XMLTag yTag = new XMLTag(tag);
		yTag.setName("y");
		yTag.addContent(Integer.toString(prevY));
		tag.addInnerTag(yTag);
		
		return tag;
	}

	@Override
	public void loadAtScene(XMLTag t) {
		this.mousePositionChanged(
				Integer.parseInt(t.getInnerTag("x").getContent()),
				Integer.parseInt(t.getInnerTag("y").getContent()));
		this.addToScene();
	}
}
