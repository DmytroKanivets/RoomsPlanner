package com.coursework.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.coursework.figures.FiguresManager;
import com.coursework.windows.Canvas;

public class ScenesManager {
	
	private static ScenesManager instance;
	
	public static ScenesManager instance() {
		if (instance == null)
			instance = new ScenesManager();
		return instance;
	}
	
	private ScenesManager() {
		scenes = new LinkedList<>();
	}
	
	private List<Scene> scenes;
	private Scene currentScene;
	
	private JButton deleteButton, undoButton, redoButton; 
	
	private Canvas canvas;
	private JLabel canvasLabel;	
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	public void setDeleteButton(JButton button) {
		this.deleteButton = button;
	}
	
	public void setRedoButton(JButton button) {
		this.redoButton = button;
	}

	public void setUndoButton(JButton button) {
		this.undoButton = button;
	}
	
	public void setDeleteEnbled(boolean enabled) {
		deleteButton.setEnabled(enabled);
	}
	
	public void setUndoEnbled(boolean enabled) {
		undoButton.setEnabled(enabled);
	}
	
	public void setRedoEnbled(boolean enabled) {
		redoButton.setEnabled(enabled);
	}
	
	public void removeScene(int id) {
		scenes.remove(id);
	}
	
	public void removeCurrent() {
		scenes.remove(currentScene);
	}
	
	public void selectScene(int id) {
		currentScene = scenes.get(id);
		FiguresManager.getInstance().renewFactories();
	}
	
	public int newScene() {
		scenes.add(new Scene());
	
		return scenes.size() - 1;
	}
	
	public int getCurrentId() {
		return scenes.indexOf(currentScene);
	}
	
	public void repaint() {
		canvas.repaint();
	}
	
	
	public void clearDrawings() {
		currentScene.clearCanvas();
	}
	
	public void clearHistory() {
		currentScene.clearHistory();
	}
	
	public void clearSelectionAtScene() {
		currentScene.clearSelection();
	}

	public DrawableCommandFactory getAddCommands() {
		return currentScene.getAddCommandFactory();
	}

	public void draw(Graphics2D graphics) {
		currentScene.drawScene(graphics);
		FiguresManager.getInstance().drawSelectedFigure(graphics);
	}

	public void saveSceneToFile(String filename) {
		currentScene.saveToFile(filename);
		
	}

	public void loadSceneFromFile(String path) throws FileNotFoundException {
		currentScene.loadFromFile(path);
	}

	public Iterable<Drawable> getDrawables() {
		return currentScene.getDrawables();
	}
	
	public boolean isFigureSelected() {
		return FiguresManager.getInstance().getSelectedFigure() != null;
	}

	public int getOffsetX() {
		return currentScene.sceneShiftX;
	}
	
	public int getOffsetY() {
		return currentScene.sceneShiftY;
	}
	
	public void undo() {
		currentScene.undo();
	}
	
	public void redo() {
		currentScene.redo();
	}
	
	public void delete() {
		currentScene.delete();
		
	}

	public void setCanvalLabel(JLabel label) {
		this.canvasLabel = label;
	}
	
	public void writeOnCanvas(String text, Color color) {
		canvasLabel.setText(text);
		canvasLabel.setForeground(color);
	}

	public int getSceneWidth() {
		return canvas.getWidth();
	}
	
	public int getSceneHeight() {
		return canvas.getHeight();
	}
}
