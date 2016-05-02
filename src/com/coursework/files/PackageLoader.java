package com.coursework.files;

import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.Figure;
import com.coursework.editor.ImmutableFigure;
import com.coursework.main.Debug;

public class PackageLoader {
	
	String fileName;
	String packageName;
	
	List<Figure> figures;
	
	public PackageLoader(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		figures = new LinkedList<Figure>();

		Debug.log("Loading " + fileName);
		
		loadPackage();
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	
	public List<Figure> getFigures() {
		return figures;
	}		
	
	private void loadPackage() throws FileNotFoundException {
		XMLReader reader = new XMLReader(fileName);
		
		//TODO remove hardcode
		XMLTag root = reader.getRoot();
		
		Collection<XMLTag> figs = root.getInnerTags();
		
		packageName = root.getInnerTag("name").getContent();
		
		for (XMLTag tag: figs) {
			if (tag.getName().equals("figure")) {
				ImmutableFigure newFigure = new ImmutableFigure(packageName, tag.getInnerTag("class").getContent(),tag.getInnerTag("name").getContent());
				
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
				
				figures.add(newFigure);
			}
		}
	}

}
