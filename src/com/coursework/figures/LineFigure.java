package com.coursework.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.Drawable;
import com.coursework.files.XMLTag;

public class LineFigure extends ExtensibleFigure {

	public LineFigure(String figurePackage, String figureName) {
		super(figurePackage, figureName);
	}

	public Area getArea() {
		if (mouseDown) {
			//System.out.println("NOT NULL AREA");
			double deltaX = currentX - startX;
			double deltaY = currentY - startY;
			
			double length = Math.sqrt(
					Math.pow(deltaX, 2) +  
					Math.pow(deltaY, 2));
			Area a = new Area(new Rectangle2D.Double(startX - width/2, startY - width/2, length +  width, width));
			//Area a = new Area(new Rectangle2D.Double(startX - width/2, startY + length + width/2,startX + width/2, startY - width/2));
	
			
			AffineTransform transform = new AffineTransform();
			//transform.setToIdentity();
			/*
			double theta = Math.atan2(deltaY, deltaX);
			transform.rotate(theta, startX, startY);*/
			transform.rotate(deltaX, deltaY, startX, startY);
			//transform.translate(startX, startY);
			a.transform(transform);
			//System.out.println(a.getBounds2D().toString());
			return a;
		} else {
			//System.out.println("NULL AREA");
			return new Area();
		}
	}
	
	@Override
	public void selfPaint(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fill(getArea());
	}
	
	@Override
	public XMLTag saveAtScene() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadAtScene(XMLTag t) {
		// TODO Auto-generated method stub
		
	}

	private int startX = 0;
	private int startY = 0;
	
	private int currentX = 0;
	private int currentY = 0;
	
	private double width;
	
	boolean mouseDown = false;
	
	@Override
	public void mouseDown() {
		mouseDown = true;
	}

	private class DrawableRepresentation implements Drawable {
		
		int startX;
		int startY;
		int endX;
		int endY;
		
		Area a;
		List<String> tags;
		
		public DrawableRepresentation(Area area, int startX, int startY, int endX, int endY) {
			a = new Area(area);
			
			//System.out.println(a.getBounds2D().toString());
			
			this.startX = startX;
			this.startY = startY;
			
			this.endX = endX;
			this.endY = endY;

			tags = new LinkedList<>();
			tags.addAll(getTags());
		}
		
		@Override
		public void selfPaint(Graphics2D g) { 
			g.fill(a);
		}

		@Override
		public XMLTag saveAtScene() {
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
		
			XMLTag xsTag = new XMLTag(tag);
			xsTag.setName("startX");
			xsTag.addContent(Integer.toString(startX));
			tag.addInnerTag(xsTag);
		
			XMLTag ysTag = new XMLTag(tag);
			ysTag.setName("startY");
			ysTag.addContent(Integer.toString(startY));
			tag.addInnerTag(ysTag);
			
			XMLTag xeTag = new XMLTag(tag);
			xeTag.setName("endX");
			xeTag.addContent(Integer.toString(endX));
			tag.addInnerTag(xeTag);
		
			XMLTag yeTag = new XMLTag(tag);
			yeTag.setName("endY");
			yeTag.addContent(Integer.toString(endY));
			tag.addInnerTag(yeTag);
			
			return tag;
		}

		@Override
		public boolean hasTag(String tag) {
			return tags.contains(tag);
		}
		
	}
	
	@Override
	public void mouseUp() {
		Area a = getArea();
		//System.out.println(a.getBounds2D().toString());
		commandFactory.getCommand(new DrawableRepresentation(a, startX, startY, currentX, currentY)).execute();
		
		mouseDown = false;
	}

	@Override
	public void mousePositionChanged(int x, int y) {
		if (!mouseDown) {
			startX = x;
			startY = y;
		}

		currentX = x;
		currentY = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
