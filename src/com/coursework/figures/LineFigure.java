package com.coursework.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import com.coursework.editor.KeyboardState;
import com.coursework.editor.Scene;
import com.coursework.files.PropertyContainer;
import com.coursework.files.XMLBuilder;
import com.coursework.files.XMLTag;
import com.coursework.rules.RulesManager;

public class LineFigure extends ExtensibleFigure {
/*
	public LineFigure(PropertyContainer container) {
		super(container);
	}
*/
	boolean shiftPressed;
	/*
	public LineFigure(String figurePackage, String figureName) {
		super(figurePackage, figureName);
	}
*/
	private Area getArea() {

		double deltaX = currentX - startX;
		double deltaY = currentY - startY;

		double length = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		
		Area a = new Area(new Rectangle2D.Double(startX - width/2, startY - width/2, length +  width, width));
		
		double theta = Math.atan2(deltaY, deltaX);
		if (shiftPressed) {
			double step = Math.toRadians(Scene.ROTATION_STEP);
			double proportion = theta/step;
			theta = Math.round(proportion) * step;
		}

		AffineTransform transform = new AffineTransform();
		transform.rotate(theta, startX, startY);
		a.transform(transform);
		return a;
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (mouseDown) {
			Area current = getArea();
		
			Drawable newArea = RulesManager.getInstance().processDrawable(new DrawableRepresentation());
			if (newArea == null) {
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

		Drawable d = new DrawableRepresentation();
		addCommandFactory.getCommand(d).execute();
	}

	private int startX = 0;
	private int startY = 0;
	
	private int currentX = 0;
	private int currentY = 0;
	
	private double width;
	
	boolean mouseDown = false;
	
	@Override
	public void drawStart() {
		mouseDown = true;
	}

	private class LineFigureBuilder extends FigureBuilder {
		@Override
		public void build(XMLTag tag) {
			super.build(tag);
			width = Double.parseDouble(tag.getInnerTag("width").getContent());
		}
	}
	
	@Override
	public BuilderFromXML getXMLBuilder() {
		return new LineFigureBuilder();
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
		
		@Override
		public void selfPaint(Graphics2D g, Color primaryColor) { 
			g.setColor(primaryColor);
			g.fill(area);
		}
		
		@Override
		public void save(XMLBuilder builder) {
			builder.addTag("figure");

			builder.addTag("figureName");
			builder.addContent(getName());
			builder.closeTag();
			
			builder.addTag("figurePackage");
			builder.addContent(getPackageName());
			builder.closeTag();
			
			builder.addTag("startX");
			builder.addContent(Integer.toString(startX));
			builder.closeTag();
			
			builder.addTag("startY");
			builder.addContent(Integer.toString(startY));
			builder.closeTag();
			
			builder.addTag("endX");
			builder.addContent(Integer.toString(endX));
			builder.closeTag();
			
			builder.addTag("endY");
			builder.addContent(Integer.toString(endY));
			builder.closeTag();
			
			builder.addTag("isStraighten");
			builder.addContent(Boolean.toString(isStraighten));
			builder.closeTag();
			
			builder.closeTag();
		}

		@Override
		public Area getArea() {
			return area;
		}		
	}
	
	@Override
	public void drawEnd() {
		Drawable d = new DrawableRepresentation();
		
		addCommandFactory.getCommand(d).execute();
		
		mouseDown = false;
	}

	@Override
	public void move(int x, int y) {
		if (!mouseDown) {
			startX = x;
			startY = y;
		}

		currentX = x;
		currentY = y;
	}

	@Override
	public void keyPressed(KeyboardState state) {
		shiftPressed = state.isShiftPressed();
	}

	@Override
	public void rotate(double degree) {
		//Ignore, rotated by mouse dragging
	}
/*
	@Override
	public BuilderFromXML getXMLBuilder() {
		// TODO Auto-generated method stub
		return null;
	}
*/
}
