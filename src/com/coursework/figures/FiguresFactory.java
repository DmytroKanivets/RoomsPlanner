package com.coursework.figures;

import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import com.coursework.files.XMLTag;
import com.coursework.main.Debug;
import com.coursework.main.Main;

//TODO rework it
public class FiguresFactory {

	private static FiguresFactory instance;
	
	private FiguresFactory() {
		
	}
	
	public static FiguresFactory getinstance() {
		if (instance == null) {
			instance = new FiguresFactory();
		}
		
		return instance;
	}
	
	private LineFigure createLineFigure(String figurePackage, XMLTag tag) {

		LineFigure result = new LineFigure(figurePackage, tag.getInnerTag("name").getContent());
		
		result.setWidth(Integer.parseInt(tag.getInnerTag("width").getContent()));
		
		return result;
	}
	
	private RectangleFigure createRectangleFigure(String figurePackage, XMLTag tag) {
		//TODO implement
		return null;
	}
	
	private ExtensibleFigure createExtensibleFigure(String figurePackage, XMLTag tag)  {
		ExtensibleFigure result = null;
		
		switch (tag.getInnerTag("extensibleType").getContent()) {
		case "line":
			result = createLineFigure(figurePackage, tag);
			break;
		case "rect":
			result = createRectangleFigure(figurePackage, tag);
			break;
		default:
			Debug.error("Can't load figure, unknown extensible type");
			break;
		}
		
		return result;
	}
	
	private ImmutableFigure createImmutableFigure(String figurePackage, XMLTag tag) {
		ImmutableFigure newFigure = new ImmutableFigure(figurePackage, tag.getInnerTag("name").getContent());
		
		Collection<XMLTag> figureContent = tag.getInnerTags();
		for (XMLTag area: figureContent) {
			if (area.getName().equals("area")) {
				Area a = null;
				
				switch (area.getInnerTag("type").getContent()) {
				case "rect":
					a = new Area(new Rectangle2D.Double(
							Integer.parseInt(area.getInnerTag("x").getContent()), 
							Integer.parseInt(area.getInnerTag("y").getContent()), 
							Integer.parseInt(area.getInnerTag("w").getContent()), 
							Integer.parseInt(area.getInnerTag("h").getContent())));
					break;
					
				case "arc":
					a = new Area(new Arc2D.Double(
							new Rectangle2D.Double(
								Integer.parseInt(area.getInnerTag("x").getContent()), 
								Integer.parseInt(area.getInnerTag("y").getContent()), 
								Integer.parseInt(area.getInnerTag("w").getContent()), 
								Integer.parseInt(area.getInnerTag("h").getContent())), 
							Integer.parseInt(area.getInnerTag("start").getContent()),
							Integer.parseInt(area.getInnerTag("size").getContent()), 
							Arc2D.CHORD));
					break;

				default:
					//Cant load
					break;
				}
				
				if (area.getInnerTag("action").getContent().equals("add")) {
					newFigure.addArea(a);
				}
				
				if (area.getInnerTag("action").getContent().equals("sub")) {
					newFigure.subtractArea(a);
				}
			}
		}
		
		return newFigure;
	}
	
	public Figure loadFromXMLTag(String figurePackage, XMLTag tag) {

		Figure f = null;
		
		XMLTag figureType = tag.getInnerTag("type");
		
		if (figureType == null) {
			Debug.error("Can't load figure, type is null");
		} else {
			switch (tag.getInnerTag("type").getContent()) {
			case "immutable":
				f = createImmutableFigure(figurePackage, tag);
				break;
			case "extensible":
				f = createExtensibleFigure(figurePackage, tag);
				break;
			default:
				Debug.error("Can't load figure, unknown type");
				break;
			}
		}
		
		if (f != null) {
			f.setAddToSceneOperation(Main.getCurrentScene().getAddFactory());
		
			Collection<XMLTag> tags = tag.getInnerTags();
			
			for (XMLTag t : tags) {
				if (t.getName().equals("tag")) {
					f.addTag(t.getContent());
				}
			}
		}
		
		return f;
	}
}
