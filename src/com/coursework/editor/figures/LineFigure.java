package com.coursework.editor.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import com.coursework.editor.Drawable;
import com.coursework.editor.KeyboardState;
import com.coursework.editor.ScenesManager;
import com.coursework.editor.rules.RulesManager;
import com.coursework.files.XMLBuilder;
import com.coursework.files.XMLTag;

public class LineFigure extends Figure {
	
	private int startX = 0;
	private int startY = 0;
	
	private int currentX = 0;
	private int currentY = 0;
	
	private double width;
	
	boolean mouseDown = false;
	boolean shiftPressed = false;

	private Area getArea() {

		double deltaX = currentX - startX;
		double deltaY = currentY - startY;

		double length = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		
		Area a = new Area(new Rectangle2D.Double(startX - width/2, startY - width/2, length +  width, width));
		
		double theta = Math.atan2(deltaY, deltaX);
		if (shiftPressed) {
			double step = Math.toRadians(Figure.ROTATION_STEP);
			double proportion = theta/step;
			theta = Math.round(proportion) * step;
		}

		AffineTransform transform = new AffineTransform();
		transform.rotate(theta, startX, startY);
		a.transform(transform);
		return a;
	}
	
	@Override
	public void draw(Graphics2D g, int shiftX, int shiftY) {
		if (mouseDown) {
			Area current = getArea();

			Drawable newArea = RulesManager.getInstance().processDrawable(new DrawableRepresentation());
			
			boolean allowed = false;
			if (newArea != null) {
				current = newArea.getArea();
				allowed = true;
			}
			
			AffineTransform transform = new AffineTransform();
			transform.translate(shiftX, shiftY);
			current.transform(transform);

			g.setColor(allowed ? Color.GRAY : new Color(128, 0, 0));
			g.fill(current);
		}
	}

	private class LineFigureBuilder extends FigureBuilder {
		@Override
		public void load(XMLTag tag) {
			super.load(tag);
			width = Double.parseDouble(tag.getInnerTag("width").getContent());
		}
	}
	
	@Override
	public BuilderFromXML getXMLBuilder() {
		return new LineFigureBuilder();
	}
	
	private class LineDrawableLoader implements BuilderFromXML {

		@Override
		public void load(XMLTag tag) {
			startX = Integer.parseInt(tag.getInnerTag("startX").getContent());
			startY = Integer.parseInt(tag.getInnerTag("startY").getContent());
			currentX = Integer.parseInt(tag.getInnerTag("endX").getContent());
			currentY = Integer.parseInt(tag.getInnerTag("endY").getContent());
			
			shiftPressed = Boolean.parseBoolean(tag.getInnerTag("isStraighten").getContent());

			//Drawable d = ;
			ScenesManager.instance().getAddCommand(new DrawableRepresentation()).execute();
			//addCommandFactory.getCommand(d).execute();
		}
		
	}
	
	@Override
	public BuilderFromXML getDrawableLoader() {
		return new LineDrawableLoader();
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
		public void selfPaint(Graphics2D g, Color primaryColor, int shiftX, int shiftY) { 
			Area a = new Area(area);
			AffineTransform transform = new AffineTransform();
			transform.translate(shiftX, shiftY);
			a.transform(transform);
			
			
			g.setColor(primaryColor);
			g.fill(a);
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

		@Override
		public void move(int x, int y) {
			AffineTransform t = new AffineTransform();
			t.translate(x, y);
			area.transform(t);
		}	
	}
	
	@Override
	public void drawEnd() {
		ScenesManager.instance().getAddCommand(new DrawableRepresentation()).execute();
		
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
	
	@Override
	public void drawStart() {
		mouseDown = true;
	}
}
