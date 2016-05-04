package com.coursework.files;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.coursework.figures.Figure;
import com.coursework.figures.FiguresFactory;
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
		
		XMLTag root = reader.getRoot();
		

		packageName = root.getInnerTag("name").getContent();
		
		Collection<XMLTag> figs = root.getInnerTags();
				
		for (XMLTag tag: figs) {
			if (tag.getName().equals("figure")) {
				Figure newFigure = FiguresFactory.getinstance().loadFromXMLTag(packageName, tag);
				if (newFigure == null) {
					Debug.error("Can't load figure");
				} else {
					figures.add(newFigure);
				}
			}
		}
	}

}
