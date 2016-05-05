package com.coursework.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.event.MouseInputAdapter;

import com.coursework.figures.Figure;
import com.coursework.files.XMLTag;
import com.coursework.files.XMLWriter;
import com.coursework.main.Main;
import com.coursework.rules.PriorityRule;
import com.coursework.rules.RulesManager;
import com.coursework.util.PriorityQueue;

public class Scene {

	Figure selectedFigure;
	
	//List<Drawable> figures;
	PriorityQueue<Drawable> figures;
	boolean mouseOnCanvas = false;
	
	Stack<Command> commands;
	
	int mouseX;
	int mouseY;
	
	private void init() {
		commands = new Stack<>();
		figures = new PriorityQueue<Drawable>();
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
					selectedFigure.mousePositionChanged(e.getX(), e.getY());
				}
				redraw();
//				mouseX = e.getX();
//				mouseY = e.getY();
			}
			@Override
		    public void mouseMoved(MouseEvent e){
				if (selectedFigure != null) {
					selectedFigure.mousePositionChanged(e.getX(), e.getY());
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
		figures = new PriorityQueue<Drawable>();
		commands = new Stack<>();
		
		FiguresManager.getInstance().clearSelection();
	}
	
	public Scene() {
		init();
	}
	
	private void redraw() {
		Main.redraw();
	}
	
	public void undo() {
		if (commands.size() > 0) {
			Command c = commands.pop();
			c.reverse();
		}
	} 
	
	public void selectFigure(Figure f) {
		selectedFigure = f;
	}
	
	public void drawScene(Graphics2D g) {
		for (Drawable d : figures) {
			g.setColor(Color.BLACK);
			d.selfPaint(g);
		}
		
		if (selectedFigure != null && mouseOnCanvas) {
			g.setColor(Color.BLUE);
			selectedFigure.selfPaint(g);
		}
	}
	
	public void saveToFile(String fileName) {
		
		XMLWriter writer = new XMLWriter(fileName);
		
		XMLTag root = new XMLTag(null);
		root.setName("scene");
		writer.setRoot(root);
		
		for (Drawable f : figures) {
			XMLTag tag = f.saveAtScene();
			
			writer.addToRoot(tag);
		}
		
		writer.write();
	}

	
	public CommandFactory getAddFactory() {
		return new CommandFactory() {
			
			@Override
			public Command getCommand(Drawable d) {
				return new Command() {
					
					private Drawable drawable;
					
					@Override
					public void reverse() {
						figures.remove(drawable);
						redraw();
					}	
					
					@Override
					public void execute() {
						this.drawable = d;
						commands.add(this);
						figures.add(d, RulesManager.getInstance().getPriority(d));
						redraw();	
					}
				};
			}
		};
	}
}
