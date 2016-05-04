package com.coursework.files;

import java.io.FileNotFoundException;

import com.coursework.editor.FiguresManager;
import com.coursework.figures.Figure;
import com.coursework.main.Debug;
import com.coursework.main.Main;

public class SceneLoader {
	public static void loadScene(String filename) {		
		try {
			Main.resetScene();
			
			XMLReader reader = new XMLReader(filename);
			
			for (XMLTag tag : reader.getRoot().getInnerTags()) {
				if (tag.getName().equals("figure")) {
					Figure f = FiguresManager.getInstance().getFigure(
							tag.getInnerTag("figurePackage").getContent(), 
							tag.getInnerTag("figureName").getContent());
					f.loadAtScene(tag);
				} else {
					Debug.error("Unrecognised tag " + tag.getName());
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
