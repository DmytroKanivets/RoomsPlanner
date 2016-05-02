package com.coursework.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.MouseInputAdapter;

import com.coursework.files.XMLReader;
import com.coursework.files.XMLTag;
import com.coursework.files.XMLWriter;
import com.coursework.main.Main;

public class SceneManager {

	Figure selectedFigure;
	
	List<Drawable> figures;
	boolean mouseOnCanvas = false;
	
	int mouseX;
	int mouseY;
	
	private void init() {
		figures = new LinkedList<Drawable>();
		Main.addCanvasMouseListener(new MouseInputAdapter() {
			@Override
		    public void mouseEntered(MouseEvent e) {
				mouseOnCanvas = true;
				redraw();
			}
			@Override
		    public void mouseExited(MouseEvent e) {
				mouseOnCanvas = false;
				redraw();
			}
			@Override
		    public void mouseDragged(MouseEvent e){
				if (selectedFigure != null) {
					selectedFigure.positionChanged(e.getX(), e.getY());
				}
				redraw();
//				mouseX = e.getX();
//				mouseY = e.getY();
			}
			@Override
		    public void mouseMoved(MouseEvent e){
				if (selectedFigure != null) {
					selectedFigure.positionChanged(e.getX(), e.getY());
				}
				redraw();
//				mouseX = e.getX();
//				mouseY = e.getY();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && selectedFigure != null)
					selectedFigure.mouseDown();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && selectedFigure != null)
				selectedFigure.mouseUp();
				
			}
			/*
			  	public void mouseClicked(MouseEvent e)
			    public void mousePressed(MouseEvent e) {}
			    public void mouseReleased(MouseEvent e) {}
			    public void mouseEntered(MouseEvent e) {}
			    public void mouseExited(MouseEvent e) {}
			    public void mouseWheelMoved(MouseWheelEvent e){}
			    public void mouseDragged(MouseEvent e){}
			    public void mouseMoved(MouseEvent e){}
			 */			
		});
	}
	
	public void clear() {
		figures = new LinkedList<Drawable>();
		FiguresManager.getInstance().clearSelection();
	}
	
	public SceneManager() {
		init();
	}
	
	private void redraw() {
		Main.redraw();
	}
		
	public void selectFigure(Figure f) {
		selectedFigure = f;
	}
	
	public void drawScene(Graphics2D g) {
		g.setColor(Color.BLACK);
		for (Drawable d : figures) {
			d.selfPaint(g);
		}
		
		if (selectedFigure != null && mouseOnCanvas) {
			g.setColor(Color.BLUE);
			selectedFigure.selfPaint(g);
		}
	}
	
	public void addDrawable(Drawable d) {
		figures.add(d);
	}
	
	public void saveToFile(String fileName) {
		
		XMLWriter writer = new XMLWriter(fileName);
		
		XMLTag root = new XMLTag(null);
		root.setName("scene");
		writer.setRoot(root);
		
		for (Drawable f : figures) {
			XMLTag tag = f.getXMLTag();
			
			writer.addToRoot(tag);
		}
		
		writer.write();
	}
}
