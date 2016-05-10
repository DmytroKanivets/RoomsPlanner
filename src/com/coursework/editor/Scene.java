package com.coursework.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import javax.swing.event.MouseInputAdapter;

import com.coursework.figures.FiguresManager;
import com.coursework.files.XMLBuilder;
import com.coursework.files.XMLReader;
import com.coursework.files.XMLTag;
import com.coursework.main.Debug;
import com.coursework.main.Main;
import com.coursework.rules.RulesManager;
import com.coursework.util.PriorityQueue;

//TODO optimize house search? + graphs
public class Scene {
	public static final int MOVE_STEP = 2;	
	
	private boolean clearRedo = true;
	
	private int mousePrevX;
	private int mousePrevY;
	
	int sceneShiftX = 0;
	int sceneShiftY = 0;
	
	private Drawable selectedDrawable;
	
	PriorityQueue<Drawable> figures;
	boolean mouseOnCanvas = false;
	
	Stack<Command> commands;
	Stack<Command> reversedCommands;
	
	private DrawableCommandFactory addFactory, deleteFactory;
	
	int mouseX;
	int mouseY;

	private boolean commandReversed = false;
	
	private void initMouse() {
		Main.addCanvasMouseListener(new MouseInputAdapter() {
			@Override
		    public void mouseDragged(MouseEvent e) {
//				System.out.println("move");
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
		Main.addKeyboardEventDispatcher(new KeyEventDispatcher() {
			
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
			getDeleteCommandFactory().getCommand(selectedDrawable).execute();
			selectedDrawable = null;
			actionExecuted();
		}
	}

	private void selectDrawableAtPosition(int x, int y) {
		x -= sceneShiftX;
		y -= sceneShiftY;
		
		Iterator<Drawable> it = figures.iterator();
		Drawable last = null, newSelected = null;
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

	private boolean checkForRules() {
		boolean unchanged = true;

		for (Drawable d : figures) {
			Drawable newD = RulesManager.getInstance().processDrawable(d);
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
	
	private void initFactories() {
		addFactory = new DrawableCommandFactory() {
		
			@Override
			public Command getCommand(Drawable d) {
				System.out.println(sceneShiftX + " " + sceneShiftY);
				Drawable result = RulesManager.getInstance().processDrawable(d);
	
				return new Command() {
					
					private Drawable drawable;
					
					@Override
					public void reverse() {
						figures.remove(drawable);
						commands.remove(this);
						clearRedo = false;
						actionExecuted();
					}	
					
					@Override
					public void execute() {
						this.drawable = result;
						if (drawable != null) {
							commands.add(this);
							figures.add(drawable, -(drawable.getPriority()+1));
							clearRedo = true;
							actionExecuted();
						} else {
							System.out.println("Deny " + d.toString());
						}
					}
				};
			}
		};
		
		deleteFactory = new DrawableCommandFactory() {
			
			@Override
			public Command getCommand(Drawable drawable) {
				return new Command() {
										
					@Override
					public void reverse() {
						figures.add(drawable, -(drawable.getPriority()+1));
						commands.remove(this);
						clearRedo = false;
						actionExecuted();
					}
					
					@Override
					public void execute() {
						if (drawable != null) {
							commands.add(this);
							figures.remove(drawable);

							clearRedo = true;
							actionExecuted();
						}
					}
				};
			}
		};
		
	}

	private void init() {
		commands = new Stack<>();
		reversedCommands = new Stack<>();
		figures = new PriorityQueue<Drawable>();
		
		initMouse();
		initKeyboard();
		initFactories();
	}
	
	Scene() {
		init();
		actionExecuted();
	}
	
	void clearCanvas() {
		figures.clear();		
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


		int maxTextWidth = 0;
		for (int i = sceneShiftY % 50 -50;i < height; i+=50) {
			String s = Integer.toString((i - sceneShiftY) / 50);
			maxTextWidth = Math.max(maxTextWidth, g.getFontMetrics().stringWidth(s));
		}
		maxTextWidth += 8;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, 15);
		g.fillRect(0, 0, maxTextWidth, height);
		g.setColor(Color.BLACK);
		
		
		for (int i = sceneShiftX % 50 -50;i < width; i+=50) {
			String s = Integer.toString((i - sceneShiftX) / 50);
			int size = g.getFontMetrics().stringWidth(s);
			g.drawString(s, i - size/2, 12);
		}

		for (int i = sceneShiftY % 50 -50;i < height; i+=50) {
			g.drawString(Integer.toString((i - sceneShiftY) / 50), 5, i + 5);
		}

		g.setColor(Color.BLACK);
		g.drawLine(0, 15, width, 15);
		g.drawLine(maxTextWidth, 0, maxTextWidth, height);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, maxTextWidth, 15);
	} 
	
	public void drawScene(Graphics2D g) {
		drawGrid(g);
		
		for (Drawable d : figures) {
			d.selfPaint(g, ((d == selectedDrawable) ? Color.BLUE : Color.BLACK), sceneShiftX, sceneShiftY);
		}
		
		drawGridNumbers(g);
	}

	
	public void actionExecuted() {
		checkForRules();
		if (clearRedo && !commandReversed) {
			//System.out.println("redo clear");
			reversedCommands.clear();
		} else {
			commandReversed = false;
//			clearRedo = true;
		}
		repaint();
		//System.out.println("update " + commands.size());
		ScenesManager.instance().setUndoEnbled(commands.size() > 0);
		ScenesManager.instance().setRedoEnbled(reversedCommands.size() > 0);
		ScenesManager.instance().setDeleteEnbled(selectedDrawable != null);
	}
	
	public void loadFromFile(String filename) throws FileNotFoundException {
		clearCanvas();
		
		XMLReader reader = new XMLReader(filename);
		
		for (XMLTag tag : reader.getRoot().getInnerTags()) {
			if (tag.getName().equals("figure")) {
				
				FiguresManager.getInstance().getFigure(
						tag.getInnerTag("figurePackage").getContent(), 
						tag.getInnerTag("figureName").getContent()).getDrawableLoader().load(tag);
			} else {
				Debug.error("Unrecognised tag " + tag.getName());
			}
		}

		clearHistory();
	}
	
	public void saveToFile(String filename) {

		XMLBuilder builder = new XMLBuilder(filename);
		
		builder.addTag("scene");
		
		for (Drawable d : figures) {
			d.save(builder);
		}
		
		builder.closeTag();
		builder.flush();
	}

	public DrawableCommandFactory getAddCommandFactory() {
		return addFactory;
	}

	public DrawableCommandFactory getDeleteCommandFactory() {
		return deleteFactory;
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
		return figures;
	}
}
