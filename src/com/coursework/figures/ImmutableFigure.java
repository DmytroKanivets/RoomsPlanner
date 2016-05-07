package com.coursework.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import com.coursework.files.XMLTag;
import com.coursework.rules.RulesManager;

public class ImmutableFigure extends Figure {

	private Area area;
	
	private int prevX;
	private int prevY;
	
	private int deltaX;
	private int deltaY;
	
	private double rotationDegree;

	public ImmutableFigure(String figurePackage, String figureName) {
		super(figurePackage, figureName);
		rotationDegree = 0;
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

	private Area getArea() {
		Area a = new  Area(area);
		AffineTransform transform = new AffineTransform();
		System.out.println("deg: " + rotationDegree * 2);
		//transform.rotate(Math.toRadians(rotationDegree));
		
		//transform.rotate(Math.toRadians(rotationDegree), 0, 0);
		//System.out.println(a.getBounds2D().toString());
		
		//System.out.println("Before: " + (a.getBounds2D().getX() + a.getBounds2D().getWidth()/2) + " " + (a.getBounds2D().getY() + a.getBounds2D().getHeight()/2));
		
		transform.translate(prevX, prevY);
		a.transform(transform);
		//System.out.println("Move: " + (a.getBounds2D().getX() + a.getBounds2D().getWidth()/2) + " " + (a.getBounds2D().getY() + a.getBounds2D().getHeight()/2));
		
		transform.setToIdentity();
		transform.rotate(Math.toRadians(rotationDegree), prevX, prevY);
		a.transform(transform);
		//System.out.println("Rotate: " + (a.getBounds2D().getX() + a.getBounds2D().getWidth()/2) + " " + (a.getBounds2D().getY() + a.getBounds2D().getHeight()/2));
		
		//transform.translate(prevX, prevY);
		//System.out.println("Rot  " + rotationDegree);
		//System.out.println(String.format("Coord %d %d", prevX, prevY));
		//transform.rotate(Math.toRadians(rotationDegree), prevX/2, prevY/2);
		a.transform(transform);
		
		return a;
	}
	
	@Override
	public void selfPaint(Graphics2D g) {
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
		
	}
	/*
	public static void drawAreaWithBorder(Graphics2D g, Area a, Color areaColor, Color borderColor) {
		g.setColor(areaColor);
		g.fill(a);
		g.setColor(borderColor);
		g.draw(a);
	}
	*/
	@Override
	public void mousePositionChanged(int x, int y) {
		
		deltaX = x - this.prevX;
		deltaY = y - this.prevY;		

		this.prevX = x;
		this.prevY = y;
		/*
		AffineTransform transform = new AffineTransform();;
		transform.setToIdentity();
		transform.translate(x - this.prevX, y - this.prevY);
		area.transform(transform);*/
	}

	private class DrawableRepresentation extends Drawable {
		
		int x;
		int y;
		
		Area a;
		
		public DrawableRepresentation(Area area, int xPos, int yPos) {
			a = new Area(area);
			x = xPos;
			y = yPos;
			addAllTags(getTags());
		}
		
		@Override
		public void selfPaint(Graphics2D g, Color primaryColor) {
			//drawAreaWithBorder(g, a);
			g.setColor(Color.WHITE);
			g.fill(a);
			g.setColor(primaryColor);
			g.draw(a);
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

		@Override
		public Area getArea() {
			return a;
		}
		
	}
	
	private void addToScene() {
		Drawable d = new DrawableRepresentation(getArea(), prevX, prevY);
		//d.addAllTags(getTags());
		commandFactory.getCommand(d).execute();
	}
	
	@Override
	public void mouseDown() {
		this.addToScene();
	}

	@Override
	public void loadAtScene(XMLTag t) {
		this.mousePositionChanged(
				Integer.parseInt(t.getInnerTag("x").getContent()),
				Integer.parseInt(t.getInnerTag("y").getContent()));
		this.addToScene();
	}

	@Override
	public void rotateLeft(double degree) {
		rotationDegree -= degree;
	}

	@Override
	public void rotateRight(double degree) {
		rotationDegree += degree;
	}

/*
	@Override
	protected Area getArea() {
		return area;
	}*/
}
