package com.coursework.editor.figures;

import com.coursework.editor.rules.RulesManager;
import com.coursework.files.XMLReader;
import com.coursework.files.XMLTag;
import com.coursework.main.Debug;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class FiguresLoader {
	
	private String fileName;
	private String packageName;
	
	private List<Figure> figures;
	
	public FiguresLoader(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		figures = new LinkedList<Figure>();

		Debug.log("Loading figures from " + fileName);
		
		loadPackage();
		RulesManager.getInstance().loadRules(fileName);
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
		
		Collection<XMLTag> figs = root.getInnerTag("figures").getInnerTags();
				
		for (XMLTag tag: figs) {
			if (tag.getName().equals("figure")) {
				if (tag.getInnerTag("package") == null) {
					XMLTag pack = new XMLTag(tag, "package");
					pack.addContent(packageName);
					tag.addInnerTag(pack);
				}
				
				Figure f = null;
				
				switch (tag.getInnerTag("type").getContent()) {
				case "immutable":
					f = new ImmutableFigure();
					break;
				case "line":
					f = new LineFigure();
					break;
				default:
					Debug.log("Cant load figure of type " + tag.getInnerTag("type").getContent());
					break;
				}
				
				if (f != null) {
					f.getXMLBuilder().load(tag);
					figures.add(f);
				}				
			}
		}
	}

}
