package com.coursework.editor;

import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.coursework.figures.Drawable;
import com.coursework.figures.FiguresManager;
import com.coursework.windows.Canvas;

public class SceneManager {
	
	
	
	private static SceneManager instance;
	
	public static SceneManager instance() {
		if (instance == null)
			instance = new SceneManager();
		return instance;
	}
	
	
	private List<Scene> scenes;
	private Scene current;
	
	private Canvas canvas;
	//int current;	
	
	private SceneManager() {
		scenes = new LinkedList<>();
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	public void repaint() {
		canvas.repaint();
	}
	
	
	public void clearDrawings() {
		current.clearCanvas();
	}
	
	public void clearHistory() {
		current.clearHistory();
	}
	
	public void removeScene(int id) {
		scenes.remove(id);
	}
	
	public void removeCurrent() {
		scenes.remove(current);
	}
	
	public void selectScene(int id) {
		current = scenes.get(id);
	}
	
	public int newScene() {
		current = new Scene();
		scenes.add(current);
	
		return scenes.size() - 1;
	}
	
	public int getCurrentId() {
		return scenes.indexOf(current);
	}

	public DrawableCommandFactory getAddCommands() {
		return current.getAddCommandFactory();
	}

	public void draw(Graphics2D graphics) {
		current.drawScene(graphics);
		FiguresManager.getInstance().drawSelectedFigure(graphics);
	}

	public void saveSceneToFile(String filename) {
		current.saveToFile(filename + ".scene");
		
	}

	public void loadSceneFromFile(String path) throws FileNotFoundException {
		current.loadFromFile(path);
	}

	public Iterable<Drawable> getDrawables() {
		return current.getDrawables();
	}
	
	public boolean isFigureSelected() {
		return FiguresManager.getInstance().getSelectedFigure() != null;
	}

	public int getOffsetX() {
		return current.sceneShiftX;
	}
	
	public int getOffsetY() {
		return current.sceneShiftY;
	}
	
}
