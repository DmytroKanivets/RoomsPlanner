package com.coursework.files;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.coursework.editor.Scene;
import com.coursework.editor.SceneManager;
import com.coursework.figures.Figure;
import com.coursework.figures.ImmutableFigure;
import com.coursework.figures.LineFigure;

import com.coursework.rules.RulesManager;

import com.coursework.main.Debug;
import com.coursework.main.Main;

public class FiguresLoader {
	
	String fileName;
	String packageName;
	
	List<Figure> figures;
	
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
				XMLTag pack = new XMLTag(tag, "package");
				pack.addContent(packageName);
				tag.addInnerTag(pack);
				
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
					//System.out.println(tag.getInnerTag("type").getContent());
					f.getXMLBuilder().load(tag);
					//f.setAddToSceneOperation(Main.getCurrentScene().getAddCommandFactory());
					f.setAddToSceneOperation(SceneManager.instance().getAddCommands());
					figures.add(f);
				}				
			}
		}
	}

}
