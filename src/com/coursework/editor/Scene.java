package com.coursework.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;

import com.coursework.figures.Drawable;
import com.coursework.figures.Figure;
import com.coursework.figures.FiguresManager;
import com.coursework.files.XMLTag;
import com.coursework.files.XMLWriter;
import com.coursework.main.Main;
import com.coursework.rules.Rule;
import com.coursework.rules.RulesManager;
import com.coursework.util.PriorityQueue;

public class Scene {

	public static final double ROTATION_STEP = 5.0;
	
	Figure selectedFigure;
	Drawable selectedDrawable;
	
	PriorityQueue<Drawable> figures;
	boolean mouseOnCanvas = false;
	
	KeyboardState currentKeyboardState;
	
	Stack<Command> commands;
	
	int mouseX;
	int mouseY;
	
	private void initMouse() {
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
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (selectedFigure != null) {
						selectedFigure.mouseDown();
					} else {
						selectDrawableUnderCursor(e.getX(), e.getY());
						redraw();
					}
				}
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
	
	protected void selectDrawableUnderCursor(int x, int y) {
		
		Iterator<Drawable> it = figures.iterator();
		Drawable last =null, newSelected = null;
		boolean found = false;
		
		while (it.hasNext()) {
			Drawable d = it.next();
			if (d.getArea().contains(x, y)) {
				last = d;
				if (d == selectedDrawable) {
					found = true;
				} 
				if (!found) {
					newSelected = d;
				}
			}
		}
		selectedDrawable = newSelected;
		
		if (selectedDrawable == null) {
			selectedDrawable = last;
		}
	}

	private void initKeyboard() {
		Main.addKeyboardEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				//Ctrl z keycode
				int code = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode();
				if  (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == code && e.isControlDown()) {
					undo();
				}
				
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SHIFT) {
					currentKeyboardState.setShiftState(true);
				}
				
				if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_SHIFT) {
					currentKeyboardState.setShiftState(false);
				}
				
				if (selectedFigure != null) {
					if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_Q) {
						System.out.println("try to rotate");
						selectedFigure.rotateLeft(ROTATION_STEP);
					}
					
					if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_E) {
						selectedFigure.rotateRight(ROTATION_STEP);
					}
				}
				
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					FiguresManager.getInstance().clearSelection();
				}
				
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_DELETE) {
					if (selectedDrawable != null) {
						getDeleteCommand(selectedDrawable).execute();
						checkForRules();
						selectedDrawable = null;
					}
				}
				
				if (selectedFigure != null) {
					selectedFigure.keybardEvent(currentKeyboardState);
					redraw();
				}
				
				return false;
			}
		});
	}
	
	protected boolean checkForRules() {
		
		boolean unchanged = true;

		for (Drawable d : figures) {
			Drawable newD = RulesManager.getInstance().processDrawable(d);
			if (newD == null || !newD.getArea().equals(d.getArea())) {
				unchanged = false;
			} 
		}
		
		if (!unchanged) {
			undo();
			System.out.println("You cant perform this action");
		}
		
		return unchanged;
	}
	
	protected Command getDeleteCommand(Drawable drawable) {
		return new Command() {
			
			//Drawable d;
			
			@Override
			public void reverse() {
				figures.add(drawable, -(drawable.getPriority()+1));
				redraw();
			}
			
			@Override
			public void execute() {
				//this.d = drawable;
				if (drawable != null) {
					System.out.println("remove");
					commands.add(this);
					figures.remove(drawable);
					redraw();
				}
			}
		};
	}

	private void init() {
		commands = new Stack<>();
		figures = new PriorityQueue<Drawable>();
		currentKeyboardState = new KeyboardState();
		
		initMouse();
		initKeyboard();
	}

	public void clear() {
		commands.clear();
		figures.clear();
		
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
		selectedDrawable = null;
		redraw();
	}
	
	public void drawScene(Graphics2D g) {
		for (Drawable d : figures) {
			//g.setColor(Color.BLACK);
			d.selfPaint(g, ((d == selectedDrawable) ? Color.BLUE : Color.BLACK));
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

				Drawable result = RulesManager.getInstance().processDrawable(d);
				//Drawable result = d;
				return new Command() {
					
					private Drawable drawable;
					
					@Override
					public void reverse() {
						figures.remove(drawable);
						redraw();
					}	
					
					@Override
					public void execute() {
						this.drawable = result;
						if (drawable != null) {
							System.out.println("add");
							commands.add(this);
							figures.add(drawable, -(drawable.getPriority()+1));
							redraw();
						}
					}
				};
			}
		};
	}

	public void clearUndoStack() {
		commands.clear();
	}

	public Iterable<Drawable> getDrawables() {
		return figures;
	}
}
