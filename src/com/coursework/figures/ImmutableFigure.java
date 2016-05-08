package com.coursework.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import com.coursework.files.XMLBuilder;
import com.coursework.files.XMLTag;
import com.coursework.rules.RulesManager;

public class ImmutableFigure extends Figure {

	private Area area;
	
	private int posX;
	private int posY;
	
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

		transform.translate(posX, posY);
		a.transform(transform);
		
		transform.setToIdentity();
		transform.rotate(Math.toRadians(rotationDegree), posX, posY);
		a.transform(transform);
		
		return a;
	}
	
	@Override
	public void draw(Graphics2D g) {
		Area current = getArea();

		Drawable newArea = RulesManager.getInstance().processDrawable(new DrawableRepresentation(current, posX, posY));

		if (newArea == null) {
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

	private class DrawableRepresentation extends Drawable {
		
		int x;
		int y;
		
		double rotation;
		
		Area a;
		
		public DrawableRepresentation(Area area, int xPos, int yPos) {
			a = new Area(area);
			x = xPos;
			y = yPos;
			rotation = rotationDegree;
			addAllTags(getTags());
		}
		
		@Override
		public void selfPaint(Graphics2D g, Color primaryColor) {
			g.setColor(Color.WHITE);
			g.fill(a);
			g.setColor(primaryColor);
			g.draw(a);
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
			
			builder.addTag("x");
			builder.addContent(Integer.toString(x));
			builder.closeTag();
			
			builder.addTag("y");
			builder.addContent(Integer.toString(y));
			builder.closeTag();
			
			builder.addTag("rotation");
			builder.addContent(Double.toString(rotation));
			builder.closeTag();
			
			builder.closeTag();
		}
/*
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
			
			XMLTag rot = new XMLTag(tag);
			rot.setName("rotation");
			rot.addContent(Double.toString(rotation));
			tag.addInnerTag(rot);
			
			return tag;
		}
*/
		@Override
		public Area getArea() {
			return a;
		}
		
	}
	
	private void addToScene() {
		Drawable d = new DrawableRepresentation(getArea(), posX, posY);
		addCommandFactory.getCommand(d).execute();
	}
	
	@Override
	public void drawStart() {
		this.addToScene();
	}

	@Override
	public void move(int x, int y) {
		this.posX = x;
		this.posY = y;
	}
	
	@Override
	public void rotate(double degree) {
		rotationDegree += degree;
	}
	
	@Override
	public void loadAtScene(XMLTag t) {
		
		rotationDegree = Double.parseDouble(t.getInnerTag("rotation").getContent());
		
		this.move(
				Integer.parseInt(t.getInnerTag("x").getContent()),
				Integer.parseInt(t.getInnerTag("y").getContent()));
		this.addToScene();
	}
}
