package com.coursework.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.event.MouseInputAdapter;

import com.coursework.editor.figures.Drawable;
import com.coursework.main.Debug;
import com.coursework.util.PriorityQueue;

class Scene {
	private boolean clearRedo = true;
	
	private int mousePrevX;
	private int mousePrevY;
	
	int sceneShiftX = 0;
	int sceneShiftY = 0;
	
	private Drawable selectedDrawable;
	
	PriorityQueue<Drawable> drawables;
	
	private Stack<Command> commands;
	private Stack<Command> reversedCommands;

	private boolean commandReversed = false;

	private void initMouse() {
		ScenesManager.instance().addMouseListener(new MouseInputAdapter() {
			@Override
		    public void mouseDragged(MouseEvent e) {
				if (!ScenesManager.instance().isFigureSelected()) {
					if (selectedDrawable == null) {
						sceneShiftX += e.getX() - mousePrevX;
						sceneShiftY += e.getY() - mousePrevY;
						mousePrevX = e.getX();
						mousePrevY = e.getY();
					}
				}
			    
				actionExecuted();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (!ScenesManager.instance().isFigureSelected()) {
						selectDrawableAtPosition(e.getX(), e.getY());
						mousePrevX = e.getX();
						mousePrevY = e.getY();
					}
				}
				actionExecuted();
			}		
		});
	}
	
	private void initKeyboard() {
		ScenesManager.instance().addKeyboardListener(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {

				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
						undo();
					}
					
					if ((e.getKeyCode() == KeyEvent.VK_Y) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
						redo();
					}
					
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						selectedDrawable = null;
						actionExecuted();
					}
					
					if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						delete();
					}				
				}
				return false;
			}
		});
	}
	
	protected void delete() {
		if (selectedDrawable != null) {
			getDeleteCommand(selectedDrawable).execute();
			selectedDrawable = null;
			actionExecuted();
		}
	}

	private void selectDrawableAtPosition(int x, int y) {
		x -= sceneShiftX;
		y -= sceneShiftY;
		
		Drawable last = null, newSelected = null;
		boolean found = false;
		
		for (Drawable d : drawables) {
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

	private boolean checkForRules() {
		boolean unchanged = true;

		for (Drawable d : drawables) {		
			Drawable newD = ScenesManager.instance().checkForRules(d);			
			if (newD == null || !newD.getArea().equals(d.getArea())) {
				unchanged = false;
			} 
		}
		
		if (!unchanged) {
			System.out.println("Can not perform action");
			undo();
			Debug.log("Can not perform action");
		}
		
		return unchanged;
	}

	private void init() {
		commands = new Stack<>();
		reversedCommands = new Stack<>();
		drawables = new PriorityQueue<Drawable>();
		
		initMouse();
		initKeyboard();
	}
	
	Scene() {
		init();
		actionExecuted();
	}
	
	void clearCanvas() {
		drawables.clear();		
		repaint();
		
		sceneShiftX = 0;
		sceneShiftY = 0;
	}
	
	private void repaint() {
		ScenesManager.instance().repaint();
	}
	
	void redo() {
		if (reversedCommands.size() > 0) {
			commandReversed = true;
			
			Command c = reversedCommands.pop();
			c.execute();
		}
	}
	
	void undo() {
		if (commands.size() > 0) {
			System.out.println("reverse");
			clearRedo = false;
			Command c = commands.peek();
			reversedCommands.add(c);
			c.reverse();
		}
	} 
	
	public void drawGrid(Graphics2D g) {
		g.setColor(new Color(200, 200, 200));
		
		int width = ScenesManager.instance().getSceneWidth();
		int height = ScenesManager.instance().getSceneHeight();
		
		for (int i = sceneShiftX % 50 -50;i < width; i+=50) {
			g.drawLine(i, 0, i, height);
		}

		for (int i = sceneShiftY % 50 -50;i < height; i+=50) {
			g.drawLine(0, i, width, i);
		}
	} 
	
	public void drawGridNumbers(Graphics2D g) {

		int width = ScenesManager.instance().getSceneWidth();
		int height = ScenesManager.instance().getSceneHeight();

		Color bgColor = new Color(240, 240, 240);

		int maxTextWidth = 0;
		for (int i = sceneShiftY % 50 -50;i < height; i+=50) {
			String s = Integer.toString((i - sceneShiftY) / 50);
			maxTextWidth = Math.max(maxTextWidth, g.getFontMetrics().stringWidth(s));
		}
		maxTextWidth += 8;
		
		g.setColor(bgColor);
		g.fillRect(0, 0, width, 15);
		g.fillRect(0, 0, maxTextWidth, height);
		g.setColor(Color.BLACK);
		
		for (int i = sceneShiftX % 50 -50;i < width; i+=50) {
			String s = Integer.toString((i - sceneShiftX) / 50);
			int size = g.getFontMetrics().stringWidth(s);
			g.drawString(s, i - size/2, 12);
		}

		int textWidth = 0;
		for (int i = sceneShiftY % 50 -50;i < height; i+=50) {
			String s = Integer.toString((i - sceneShiftY) / 50);
			textWidth = g.getFontMetrics().stringWidth(s);
			g.drawString(Integer.toString((i - sceneShiftY) / 50), maxTextWidth - textWidth - 3, i + 5);
		}

		g.setColor(Color.BLACK);
		g.drawLine(maxTextWidth, 15, width, 15);
		g.drawLine(maxTextWidth, 15, maxTextWidth, height);
		g.drawLine(maxTextWidth, height-1, width, height-1);
		g.drawLine(width-1, 15, width-1, height);
		g.setColor(bgColor);
		g.fillRect(0, 0, maxTextWidth, 15);
	} 
	
	public void drawScene(Graphics2D g) {
		drawGrid(g);
		
		for (Drawable d : drawables) {
			d.selfPaint(g, ((d == selectedDrawable) ? Color.BLUE : Color.BLACK), sceneShiftX, sceneShiftY);
		}
		
		drawGridNumbers(g);
	}

	
	public void actionExecuted() {
		checkForRules();
		if (clearRedo && !commandReversed) {
			reversedCommands.clear();
		} else {
			commandReversed = false;
		}
		repaint();
		ScenesManager.instance().setUndoEnabled(commands.size() > 0);
		ScenesManager.instance().setRedoEnabled(reversedCommands.size() > 0);
		ScenesManager.instance().setDeleteEnabled(selectedDrawable != null);
	}

	public Command getAddCommand(Drawable d) {
		Drawable result = ScenesManager.instance().checkForRules(d);
	
		return new Command() {
			
			private Drawable drawable;
			
			@Override
			public void reverse() {
				drawables.remove(drawable);
				commands.remove(this);
				clearRedo = false;
				actionExecuted();
			}	
			
			@Override
			public void execute() {
				this.drawable = result;
				if (drawable != null) {
					commands.add(this);
					drawables.add(drawable, -(drawable.getPriority()+1));
					clearRedo = true;
					actionExecuted();
				} else {
					System.out.println("Deny " + d.toString());
				}
			}
		};
	}

	public Command getDeleteCommand(Drawable drawable) {
		return new Command() {
										
					@Override
					public void reverse() {
						drawables.add(drawable, -(drawable.getPriority()+1));
						commands.remove(this);
						clearRedo = false;
						actionExecuted();
					}
					
					@Override
					public void execute() {
						if (drawable != null) {
							commands.add(this);
							drawables.remove(drawable);

							clearRedo = true;
							actionExecuted();
						}
					}
				};
	}

	public void clearHistory() {
		commands.clear();
		reversedCommands.clear();
	}

	public void clearSelection() {
		selectedDrawable = null;
		actionExecuted();
	}
	
	public Iterable<Drawable> getDrawables() {
		return drawables;
	}
}
