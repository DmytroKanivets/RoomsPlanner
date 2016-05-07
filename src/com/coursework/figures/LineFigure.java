package com.coursework.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import com.coursework.editor.KeyboardState;
import com.coursework.editor.Scene;
import com.coursework.files.XMLTag;
import com.coursework.rules.RulesManager;

public class LineFigure extends ExtensibleFigure {

	boolean shiftPressed;
	
	public LineFigure(String figurePackage, String figureName) {
		super(figurePackage, figureName);
	}

	public Area getArea() {

		double deltaX = currentX - startX;
		double deltaY = currentY - startY;
		/*
		if (shiftPressed) {
			//System.out.println("try");
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				deltaY = 0;
			} else {
				deltaX = 0;
			}
		}
		*/
		double length = Math.sqrt(
				Math.pow(deltaX, 2) +  
				Math.pow(deltaY, 2));
		
		System.out.println("l: " + length);
		Area a = new Area(new Rectangle2D.Double(startX - width/2, startY - width/2, length +  width, width));
		
		AffineTransform transform = new AffineTransform();
		//transform.rotate(deltaX, deltaY, startX, startY);
		double theta = Math.atan2(deltaY, deltaX);
		if (shiftPressed) {
			//TODO Remove magic *2
			double step = Math.toRadians(Scene.ROTATION_STEP);
			double proportion = theta/step;
			theta = Math.round(proportion) * step;
			//System.out.println("Angle: " + Math.toDegrees(theta));
		}
		transform.rotate(theta, startX, startY);
		a.transform(transform);
		return a;
	}
	
	@Override
	public void selfPaint(Graphics2D g) {
		
		/*
		//Drawable newArea = null;
		Area current = getArea();

		Drawable newArea = RulesManager.getInstance().processDrawable(new DrawableRepresentation(current, prevX, prevY));
				//newArea = null;
		
		//System.out.println("try");
		if (newArea == null) {
			//System.out.println("null");
			g.setColor(Color.WHITE);
			g.fill(current);
			g.setColor(Color.RED);
			g.draw(current);
		} else {
			g.setColor(Color.WHITE);
			g.fill(newArea.getArea());
			g.setColor(Color.BLUE);
			g.draw(newArea.getArea());
		}
		*/
		if (mouseDown) {
			Area current = getArea();
		
			Drawable newArea = RulesManager.getInstance().processDrawable(new DrawableRepresentation());
			//System.out.println(newArea);
			if (newArea == null) {
				//System.out.println("null");
				g.setColor(new Color(128, 0, 0));
				g.fill(current);
			} else {
				g.setColor(Color.GRAY);
				g.fill(newArea.getArea());
			}
		}
		/*
		g.setColor(Color.GRAY);
		g.fill(getArea());*/
	}

	@Override
	public void loadAtScene(XMLTag t) {

		startX = Integer.parseInt(t.getInnerTag("startX").getContent());
		startY = Integer.parseInt(t.getInnerTag("startY").getContent());
		currentX = Integer.parseInt(t.getInnerTag("endX").getContent());
		currentY = Integer.parseInt(t.getInnerTag("endY").getContent());
		
		shiftPressed = Boolean.parseBoolean(t.getInnerTag("isStraighten").getContent());
		/*
		double deltaX = currentX - startX;
		double deltaY = currentY - startY;
		
		double length = Math.sqrt(
				Math.pow(deltaX, 2) +  
				Math.pow(deltaY, 2));
		Area a = new Area(new Rectangle2D.Double(startX - width/2, startY - width/2, length +  width, width));
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(deltaX, deltaY, startX, startY);
		a.transform(transform);
		*/
		Drawable d = new DrawableRepresentation();
		//d.addAllTags(getTags());
		commandFactory.getCommand(d).execute();
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

	private class DrawableRepresentation extends Drawable {
		
		int startX;
		int startY;
		int endX;
		int endY;
		
		Area area;
		
		boolean isStraighten;
		
		public DrawableRepresentation() {
			area = new Area(LineFigure.this.getArea());
			startX = LineFigure.this.startX;
			startY = LineFigure.this.startY;
			endX = LineFigure.this.currentX;
			endY = LineFigure.this.currentY;
			isStraighten = LineFigure.this.shiftPressed;
			addAllTags(LineFigure.this.getTags());
		}
		
		/*
		public DrawableRepresentation(Area area, int startX, int startY, int endX, int endY) {
			a = new Area(area);
			
			this.startX = startX;
			this.startY = startY;
			
			this.endX = endX;
			this.endY = endY;
		}
		+*/
		
		
		@Override
		public void selfPaint(Graphics2D g, Color primaryColor) { 
			g.setColor(primaryColor);
			g.fill(area);
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
			
			XMLTag shTag = new XMLTag(tag);
			shTag.setName("isStraighten");
			shTag.addContent(Boolean.toString(isStraighten));
			tag.addInnerTag(shTag);
			
			return tag;
		}



		@Override
		public Area getArea() {
			return area;
		}		
	}
	
	@Override
	public void mouseUp() {
		//Area a = getArea();
		//Drawable d = new DrawableRepresentation(a, startX, startY, currentX, currentY);
		Drawable d = new DrawableRepresentation();
		//d.addAllTags(getTags());
		commandFactory.getCommand(d).execute();
		
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
	
	@Override
	public void keybardEvent(KeyboardState state) {
		shiftPressed = state.isShiftPressed();
		/*
		if (!shiftPressed)
			//System.out.println("ev");*/
	}

	@Override
	public void rotate(double degree) {
		//Ignore, rotated by mouse dragging
	}

}
