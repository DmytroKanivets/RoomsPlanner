package com.coursework.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Arc2D;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.Drawable;
import com.coursework.files.XMLBuilder;
import com.coursework.files.XMLTag;
import com.coursework.main.Debug;
import com.coursework.rules.RulesManager;

public class ImmutableFigure extends Figure {

	public ImmutableFigure() {
		super();
		physArea = new Area();
		drawArea = new DrawArea();
		rotationDegree = 0;
	}
	
	private class DrawArea {
		
		private List<Shape> shapes;
		
		public DrawArea() {
			shapes = new LinkedList<>();
		}
		
		public void draw(Graphics2D g, int x, int y, double rotation) {
			System.out.println(x + " " + y);
			AffineTransform t = new AffineTransform();
			
			//t = new AffineTransform();
			t.translate(x, y);
			t.rotate(Math.toRadians(rotation));
			
			for (Shape s: shapes) {
				Shape sx = t.createTransformedShape(s);
				System.out.println("border: " + sx.getBounds2D().toString());
				g.draw(sx);
			
			}
		}
		
		public void addShape(Shape s) {
			if (s != null)
				shapes.add(s);
		}
	}
	
	private Area physArea;
	private DrawArea drawArea;
	
	
	private int posX;
	private int posY;
	
	private double rotationDegree;

	private Area getArea() {
		Area a = new  Area(physArea);
		AffineTransform transform = new AffineTransform();

		transform.translate(posX, posY);
		a.transform(transform);
		
		transform.setToIdentity();
		transform.rotate(Math.toRadians(rotationDegree), posX, posY);
		a.transform(transform);
		
		//System.out.println(a.getBounds2D());
		
		return a;
	}
	
	@Override
	public void draw(Graphics2D g, int shiftX, int shiftY) {
		Area current = getArea();
		
		Drawable newArea = RulesManager.getInstance().processDrawable(new DrawableRepresentation(current, posX, posY));
		boolean allowed = false;
		if (newArea != null) {
			current = newArea.getArea();
			allowed = true;
		}
		
		AffineTransform transform = new AffineTransform();
		transform.translate(shiftX, shiftY);
		current.transform(transform);
		
		g.setColor(Color.WHITE);
		g.fill(current);
		g.setColor(allowed ? Color.BLUE : Color.RED);
		//g.draw(current);
		drawArea.draw(g, posX + shiftX, posY + shiftY, rotationDegree);
	}

	private class ImmutableFigureBuilder extends FigureBuilder {
		
		private void loadPhysics(XMLTag root) {
			for (XMLTag areaTag: root.getInnerTags()) {
				if (areaTag.getName().equals("area")) {
					Area a = new Area();
					
					switch (areaTag.getInnerTag("type").getContent()) {
					case "rect":
						a = new Area(new Rectangle2D.Double(
								Double.parseDouble(areaTag.getInnerTag("x").getContent()), 
								Double.parseDouble(areaTag.getInnerTag("y").getContent()), 
								Double.parseDouble(areaTag.getInnerTag("w").getContent()), 
								Double.parseDouble(areaTag.getInnerTag("h").getContent())));
						break;
						
					case "arc":
						a = new Area(new Arc2D.Double(
								new Rectangle2D.Double(
									Double.parseDouble(areaTag.getInnerTag("x").getContent()), 
									Double.parseDouble(areaTag.getInnerTag("y").getContent()), 
									Double.parseDouble(areaTag.getInnerTag("w").getContent()), 
									Double.parseDouble(areaTag.getInnerTag("h").getContent())), 
								Double.parseDouble(areaTag.getInnerTag("start").getContent()),
								Double.parseDouble(areaTag.getInnerTag("size").getContent()), 
								Arc2D.CHORD));
						break;
					case "poly":
						LinkedList<Integer> xCoord = new LinkedList<>(), yCoord = new LinkedList<>();
						for (XMLTag t : areaTag.getInnerTags()) {
							if (t.getName().equals("x"))
								xCoord.add(Integer.parseInt(t.getContent()));
							if (t.getName().equals("y"))
								yCoord.add(Integer.parseInt(t.getContent()));
						}
						int[] x = new int[xCoord.size()], y = new int[yCoord.size()];
						int counter = 0;
						for (Integer i : xCoord)  {
							x[counter++] = i;
						}
						counter = 0;
						for (Integer i : yCoord)  {
							y[counter++] = i;
						}
						Polygon p = new Polygon(x, y, xCoord.size());
						a = new Area(p);
						break;
					default:
						//Cant load
						break;
					} 
					
					if (areaTag.getInnerTag("action").getContent().equals("add")) {
						physArea.add(a);
					}
					
					if (areaTag.getInnerTag("action").getContent().equals("sub")) {
						physArea.subtract(a);
					}
				} else {
					Debug.log("Non area tag in areas zone");
				}
			}
		}
		
		private void loadGraphics(XMLTag root) {
			for (XMLTag areaTag: root.getInnerTags()) {
				if (areaTag.getName().equals("area")) {
					Shape s = null;
					switch (areaTag.getInnerTag("type").getContent()) {
					case "line":
						s = new Line2D.Double(
								Double.parseDouble(areaTag.getInnerTag("xStart").getContent()), 
								Double.parseDouble(areaTag.getInnerTag("yStart").getContent()), 
								Double.parseDouble(areaTag.getInnerTag("xEnd").getContent()), 
								Double.parseDouble(areaTag.getInnerTag("yEnd").getContent())); 
						break;
					case "poly":
						LinkedList<Integer> xCoord = new LinkedList<>(), yCoord = new LinkedList<>();
						for (XMLTag t : areaTag.getInnerTags()) {
							if (t.getName().equals("x"))
								xCoord.add(Integer.parseInt(t.getContent()));
							if (t.getName().equals("y"))
								yCoord.add(Integer.parseInt(t.getContent()));
						}
						int[] x = new int[xCoord.size()], y = new int[yCoord.size()];
						int counter = 0;
						for (Integer i : xCoord)  {
							x[counter++] = (int)i;
						}
						counter = 0;
						for (Integer i : yCoord)  {
							y[counter++] = (int)i;
						}
						s = new Polygon(x, y, xCoord.size());
						break;
					case "arc":
						s = new Arc2D.Double(
								new Rectangle2D.Double(
										Double.parseDouble(areaTag.getInnerTag("x").getContent()), 
										Double.parseDouble(areaTag.getInnerTag("y").getContent()), 
										Double.parseDouble(areaTag.getInnerTag("w").getContent()), 
										Double.parseDouble(areaTag.getInnerTag("h").getContent())), 
									Double.parseDouble(areaTag.getInnerTag("start").getContent()),
									Double.parseDouble(areaTag.getInnerTag("size").getContent()), 
									Arc2D.OPEN);
						break;
					default:
						Debug.log("Can not load shape " + areaTag.getInnerTag("type").getContent());
						break;
					}
					
					drawArea.addShape(s);
				}
			}
		}
		
		@Override
		public void load(XMLTag root) {
			super.load(root);
			loadPhysics(root.getInnerTag("physAreas"));
			loadGraphics(root.getInnerTag("graphAreas"));
		}
	}

	@Override
	public BuilderFromXML getXMLBuilder() {
		return new ImmutableFigureBuilder();
	}
	
	private class DrawableRepresentation extends Drawable {
		
		int x;
		int y;
		
		double rotation;
		
		Area a;
		
		public DrawableRepresentation(Area area, int xPos, int yPos) {
			//super();
			
			a = new Area(area);
			x = xPos;
			y = yPos;
			rotation = rotationDegree;
			addAllTags(getTags());
		}
		
		@Override
		public void selfPaint(Graphics2D g, Color primaryColor, int shiftX, int shiftY) {
			Area area = new Area(a);
			AffineTransform transform = new AffineTransform();
			transform.translate(shiftX, shiftY);
			area.transform(transform);
			
			g.setColor(Color.WHITE);
			g.fill(area);
			g.setColor(primaryColor);
			//g.draw(area);
			drawArea.draw(g, x + shiftX, y + shiftY, rotation);
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

		@Override
		public Area getArea() {
			return a;
		}

		@Override
		public void move(int x, int y) {
			AffineTransform t = new AffineTransform();
			t.translate(x, y);
			a.transform(t);
		}
		
	}
	
	private void addToScene() {
		Drawable d = new DrawableRepresentation(getArea(), posX, posY);
		addCommandFactory.getCommand(d).execute();
	}
	
	private class ImmutableDrawableLoader implements BuilderFromXML {

		@Override
		public void load(XMLTag tag) {
			rotationDegree = Double.parseDouble(tag.getInnerTag("rotation").getContent());
			
			move(
					Integer.parseInt(tag.getInnerTag("x").getContent()),
					Integer.parseInt(tag.getInnerTag("y").getContent()));
			addToScene();
		}
	}
	
	@Override
	public BuilderFromXML getDrawableLoader() {
		return new ImmutableDrawableLoader();
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
}
