package com.coursework.editor.figures;

import com.coursework.editor.ScenesManager;
import com.coursework.editor.rules.RulesManager;
import com.coursework.files.XMLBuilder;
import com.coursework.files.XMLTag;
import com.coursework.main.Debug;

import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;
import java.util.List;

class ImmutableFigure extends Figure {

	ImmutableFigure() {
		super();
		physArea = new Area();
		shapes = new LinkedList<>();
		rotationDegree = 0;
	}


	private List<Shape> shapes;

	private Area physArea;

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

		drawView(g, posX + shiftX, posY + shiftY, rotationDegree);
	}

	private void drawView(Graphics2D g, int x, int y, double rotation) {
		AffineTransform t = new AffineTransform();

		t.translate(x, y);
		t.rotate(Math.toRadians(rotation));

		for (Shape s: shapes) {
			Shape sx = t.createTransformedShape(s);
			g.draw(sx);

		}
	}

	public void addShape(Shape s) {
		if (s != null)
			shapes.add(s);
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

					addShape(s);
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

		DrawableRepresentation(Area area, int xPos, int yPos) {
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
			drawView(g, x + shiftX, y + shiftY, rotation);
		}

		@Override
		public void save(XMLBuilder builder) {
			builder.beginProperty("figure");

			builder.addProperty("figureName", getName());
			builder.addProperty("figurePackage", getPackageName());
			builder.addProperty("x", x);
			builder.addProperty("y", y);
			builder.addProperty("rotation", rotation);

			builder.endProperty("figure");
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
		ScenesManager.instance().getAddCommand(new DrawableRepresentation(getArea(), posX, posY)).execute();
	}

	@Override
	public void selected() {
		rotationDegree = 0;
	}

	private class ImmutableDrawableLoader extends DrawableLoader {

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
