package com.coursework.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.event.MouseInputAdapter;

import com.coursework.figures.Drawable;
import com.coursework.figures.Figure;
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
	//public static final double ROTATION_STEP = 5.0;
	public static final int MOVE_STEP = 2;	
	
	private boolean lastCommandReversed = false;
	
	private int mousePrevX;
	private int mousePrevY;
	
	int sceneShiftX = 0;
	int sceneShiftY = 0;
	
	//Figure selectedFigure;
	Drawable selectedDrawable;
	
	PriorityQueue<Drawable> figures;
	boolean mouseOnCanvas = false;
	
	Stack<Command> commands;
	Stack<Command> reversedCommands;
	
	int mouseX;
	int mouseY;
	
	private void initMouse() {
		Main.addCanvasMouseListener(new MouseInputAdapter() {
			@Override
		    public void mouseDragged(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (!SceneManager.instance().isFigureSelected()) {
						if (selectedDrawable == null) {
							sceneShiftX += e.getX() - mousePrevX;
							sceneShiftY += e.getY() - mousePrevY;
							mousePrevX = e.getX();
							mousePrevY = e.getY();
						}
					}
			    }
				repaint();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (!SceneManager.instance().isFigureSelected()) {
						selectDrawableUnderCursor(e.getX(), e.getY());
						mousePrevX = e.getX();
						mousePrevY = e.getY();
					}
				}
				repaint();
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
					
					if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						if (selectedDrawable != null) {
							getDeleteCommandFactory().getCommand(selectedDrawable).execute();
							checkForRules();
							selectedDrawable = null;
						}
					}				
				}
				return false;
			}
		});
	}
	
	private void selectDrawableUnderCursor(int x, int y) {
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
		System.out.println("rules");

		for (Drawable d : figures) {
			Drawable newD = RulesManager.getInstance().processDrawable(d);
			if (newD == null || !newD.getArea().equals(d.getArea())) {
				unchanged = false;
			} 
		}
		
		if (!unchanged) {
			System.out.println("undo");
			undo();
			//Main.showMessage("You cant perform this action");
			Debug.log("Can not perform action");
		}
		
		return unchanged;
	}

	private void init() {
		commands = new Stack<>();
		reversedCommands = new Stack<>();
		figures = new PriorityQueue<Drawable>();
		
		initMouse();
		initKeyboard();
	}

	void clearCanvas() {
		figures.clear();		
		repaint();
		
		sceneShiftX = 0;
		sceneShiftY = 0;
	}
	
	public Scene() {
		init();
	}
	
	private void repaint() {
		SceneManager.instance().repaint();
	}
	
	private void redo() {
		if (reversedCommands.size() > 0) {
			lastCommandReversed = true;
			Command c = reversedCommands.pop();
			c.execute();
		}
	}
	
	public void undo() {
		if (commands.size() > 0) {
			Command c = commands.peek();
			c.reverse();
			reversedCommands.add(c);
		}
	} 
	
	public void drawScene(Graphics2D g) {
		for (Drawable d : figures) {
			d.selfPaint(g, ((d == selectedDrawable) ? Color.BLUE : Color.BLACK), sceneShiftX, sceneShiftY);
		}
	}
	
	private void walls() {
		//System.out.println("walls");
		/*
		//note: bad algorytm
		Area a = new Area();
		for (Drawable d : figures)
			if (d.hasTag("wall")) {
				a.add(d.getArea());
			}
		//g.setColor(Color.magenta);
		//g.draw(a);
		
		// Use unsigned arithmetics for screens more with dimensions more than 2^16 (=65536)
		Rectangle2D rect = a.getBounds2D();
		long minX = Math.round(rect.getX()) -1;
		long minY = Math.round(rect.getY()) -1;
		
		long maxX = Math.round(rect.getX() + rect.getWidth()) +1;
		long maxY = Math.round(rect.getY() + rect.getHeight()) +1;
		
		//Change for 2^32 for long
		long yShift = (long) Math.pow(2, 16);
		
		
		//create border
		Stack<Long> stack = new Stack<>();
		Set<Long> checked = new HashSet<>();
		stack.push(minX + minY * yShift);
		
		while (!stack.empty()) {
			long point = stack.pop();
			checked.add(point);
			
			long x = point % yShift;
			long y = point / yShift;
			
			long[] adjacent = new long[4];
			adjacent[0] = x > minX ? (x - 1) + (y) * yShift : point;
			adjacent[1] = x < maxX ? (x + 1) + (y) * yShift : point;
			adjacent[2] = y > minY ? (x) + (y - 1) * yShift : point;
			adjacent[3] = y < maxY ? (x) + (y + 1) * yShift : point;
			
			for (int i = 0;i < adjacent.length; i++)
				if (!checked.contains(adjacent[i]) && !a.contains((adjacent[i] % yShift), (adjacent[i] / yShift))) {
					stack.push(adjacent[i]);
				}
			
		}

		boolean found = false;
		long i = minX, j = minY;
		while (i < maxX && !found) {
			if (!a.contains(i, j) && !checked.contains(i + j * yShift)) {
				found = true;
			}
			
			if (j < maxY) {
				j++;
			} else {
				j = minY;
				i++;
			}
		}*/
	}
	
	public void actionExecuted() {
		checkForRules();
		if (!lastCommandReversed)
			reversedCommands.clear();
		else 
			lastCommandReversed = false;
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
		return new DrawableCommandFactory() {
			
			@Override
			public Command getCommand(Drawable d) {

				Drawable result = RulesManager.getInstance().processDrawable(d);
				//Drawable result = d;
				return new Command() {
					
					private Drawable drawable;
					
					@Override
					public void reverse() {
						figures.remove(drawable);
						repaint();
						commands.remove(this);
						if (drawable.hasTag("wall"))
							walls();
					}	
					
					@Override
					public void execute() {
						this.drawable = result;
						if (drawable != null) {
							commands.add(this);
							System.out.println("Adding with " + (-(drawable.getPriority()+1)));
							figures.add(drawable, -(drawable.getPriority()+1));
							repaint();
							if (drawable.hasTag("wall"))
								walls();
							actionExecuted();
						} else {
							System.out.println("Deny " + d.toString());
						}
					}
				};
			}
		};
	}

	public DrawableCommandFactory getDeleteCommandFactory() {
		
		return new DrawableCommandFactory() {
			
			@Override
			public Command getCommand(Drawable drawable) {
				return new Command() {
										
					@Override
					public void reverse() {
						figures.add(drawable, -(drawable.getPriority()+1));
						//System.out.println("remove reverse");
						commands.remove(this);
						repaint();
					}
					
					@Override
					public void execute() {
						if (drawable != null) {
							//System.out.println("remove");
							//System.out.println(drawable.toString());
							commands.add(this);
							figures.remove(drawable);
							repaint();
							actionExecuted();
						}
					}
				};
			}
		};
	}

	public void clearHistory() {
		commands.clear();
		reversedCommands.clear();
	}

	public Iterable<Drawable> getDrawables() {
		return figures;
	}
}
