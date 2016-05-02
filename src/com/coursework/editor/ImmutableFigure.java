package com.coursework.editor;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import com.coursework.files.XMLTag;
import com.coursework.main.Main;

public class ImmutableFigure extends Figure {

//	public static 
		
	public ImmutableFigure(String figurePackage, String figureClass, String figureName) {
		super(figurePackage, figureClass, figureName);
		transform = new AffineTransform();
	}

	Area area;
	AffineTransform transform;
	
	int x;
	int y;
	/*
	public ImmutableFigure(String pack, String name) {
		this.name = name;
		this.pack = pack;
		transform = new AffineTransform();
	}
	*/
	
	
	@Override
	public void addArea(Area a) {
		if (area == null) {
			area = a;
		} else {
			area.add(a);
		}

	}

	@Override
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
	public void positionChanged(int x, int y) {
		transform.setToIdentity();
		transform.translate(x - this.x, y - this.y);
		this.x = x;
		this.y = y;
		area.transform(transform);
	}
/*
	@Override
	public void mouseClicked() {
	}
*/
	private void addToScene() {
		Main.getCurrentScene().addDrawable(new DrawableRepresentation(area, x, y));
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
		xTag.addContent(Integer.toString(x));
		tag.addInnerTag(xTag);
	
		XMLTag yTag = new XMLTag(tag);
		yTag.setName("y");
		yTag.addContent(Integer.toString(y));
		tag.addInnerTag(yTag);
		
		return tag;
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

	@Override
	public void load(XMLTag t) {
		this.positionChanged(
				Integer.parseInt(t.getInnerTag("x").getContent()),
				Integer.parseInt(t.getInnerTag("y").getContent()));
		this.addToScene();
	}



}
