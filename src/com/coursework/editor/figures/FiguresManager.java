package com.coursework.editor.figures;

import com.coursework.editor.KeyboardState;
import com.coursework.editor.ScenesManager;
import com.coursework.main.Main;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class FiguresManager {
	
	private static FiguresManager instance;
	
	private JTree figuresView;
	
	private Set<String> loadedPackages;
	private List<Figure> figures;

	private KeyboardState currentKeyboardState;
	private boolean mouseOnCanvas = false;
	private Figure selectedFigure;
	
	public static FiguresManager getInstance() {
		if (instance == null)
			instance = new FiguresManager();
		return instance;
	}

	private void initMouse() {
		Main.addCanvasMouseListener(new MouseInputAdapter() {
			@Override
		    public void mouseEntered(MouseEvent e) {
				mouseOnCanvas = true;
				ScenesManager.instance().repaint();
			}
			@Override
		    public void mouseExited(MouseEvent e) {
				mouseOnCanvas = false;
				ScenesManager.instance().repaint();
			}
			@Override
		    public void mouseDragged(MouseEvent e){
				if (selectedFigure != null) {
					selectedFigure.move(e.getX() - ScenesManager.instance().getOffsetX(), e.getY() - ScenesManager.instance().getOffsetY());
				}
				ScenesManager.instance().repaint();
			}
			@Override
		    public void mouseMoved(MouseEvent e){
				if (selectedFigure != null) {
					selectedFigure.move(e.getX() - ScenesManager.instance().getOffsetX(), e.getY() - ScenesManager.instance().getOffsetY());
				}
				ScenesManager.instance().repaint();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (selectedFigure != null) {
						selectedFigure.drawStart();
					}
				}
				ScenesManager.instance().repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (selectedFigure != null) {
					if (e.getButton() == MouseEvent.BUTTON1 )
						selectedFigure.drawEnd();
				}
				ScenesManager.instance().repaint();
			}

		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	if (selectedFigure != null) {
		    		selectedFigure.rotate(e.getWheelRotation() * Figure.ROTATION_STEP);
		    		ScenesManager.instance().repaint();
		    	}
		    }	
		});
	}
	
	private void initKeyboard() {
		currentKeyboardState = new KeyboardState();
		
		Main.addKeyboardEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {

				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (selectedFigure != null) {
						if (e.getKeyCode() == KeyEvent.VK_Q) {
							selectedFigure.rotate(-Figure.ROTATION_STEP);
						}
						
						if (e.getKeyCode() == KeyEvent.VK_E) {
							selectedFigure.rotate(Figure.ROTATION_STEP);
						}
						ScenesManager.instance().repaint();
					}					
				}
				
				if (e.getKeyCode() == KeyEvent.VK_SHIFT)  {
					if (e.getID() == KeyEvent.KEY_PRESSED){
						currentKeyboardState.setShiftState(true);
					}
					
					if (e.getID() == KeyEvent.KEY_RELEASED) {
						currentKeyboardState.setShiftState(false);
					}
					
					if (selectedFigure != null) {
						selectedFigure.keyPressed(currentKeyboardState);
						ScenesManager.instance().repaint();
					}
				}
				
				return false;
			}
		});
	}
	
	private FiguresManager() {
		initMouse();
		initKeyboard();
		
		loadedPackages = new HashSet<String>();
		figures = new LinkedList<Figure>();
		
		Main.addKeyboardEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					figuresView.clearSelection();
				}
				return false;
			}
			
		});
	}

	private void updateView() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)figuresView.getModel().getRoot();
		root.removeAllChildren();
		
		DefaultMutableTreeNode currentNode = null;
		String currentPackage = "";
		
		for (Figure f : figures) {
			if (!f.getPackageName().equals(currentPackage)) {
				currentNode = new DefaultMutableTreeNode(f.getPackageName());
				root.add(currentNode);
				currentPackage = f.getPackageName();
			}
			currentNode.add(new DefaultMutableTreeNode(f));
		}
		
		DefaultTreeModel model = new DefaultTreeModel(root);
		figuresView.setModel(model);

	}
	
	public void connectView(JTree figuresView) {
		this.figuresView = figuresView;
		figuresView.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		figuresView.setRootVisible(false);
		figuresView.setShowsRootHandles(true);
		
		figuresView.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) figuresView.getLastSelectedPathComponent();
				if (node == null) {
					selectedFigure = null;
				} else {
					if (node.isLeaf())
					selectedFigure = (Figure) node.getUserObject();
					selectedFigure.selected();
				}
				ScenesManager.instance().clearSelectionAtScene();
			}
		});
	}
	
	/*
	 * Load package from specified file
	 * */
	public void addPackage(String fileName) throws FileNotFoundException {

		FiguresLoader loader;
		loader = new FiguresLoader(fileName);
		String name = loader.getPackageName();
		
		if (loadedPackages.contains(name)) {
			removePackage(name);
		} else {
			loadedPackages.add(name);
		}
	
		figures.addAll(loader.getFigures());
		
		updateView();	
		//renewFactories();
	}
	
	/*
	 * Remove package with specified name
	 */
	private void removePackage(String packageName) {
		Iterator<Figure> it = figures.iterator();
		
		while (it.hasNext()) {
			Figure f = it.next();
			if (f.getPackageName().equals(packageName)) {
				it.remove();
			}
		}
	}
	
	public Figure getFigure(String pack, String name) {		
		for (Figure f : figures) {
			if (f.getPackageName().equals(pack) && f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}

	public Figure getSelectedFigure() {
		return selectedFigure;
	}

	public void drawSelectedFigure(Graphics2D graphics) {
		if (selectedFigure != null && mouseOnCanvas) {
			selectedFigure.draw(graphics, ScenesManager.instance().getOffsetX(), ScenesManager.instance().getOffsetY());
		}
	}
	/*
	public void renewFactories() {
		for (Figure f: figures)
			f.setAddToSceneOperation(ScenesManager.instance().getAddCommands());
	}*/
}
