package com.coursework.figures;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import com.coursework.main.Main;
import com.coursework.editor.KeyboardState;
import com.coursework.editor.SceneManager;
import com.coursework.files.FiguresLoader;

public class FiguresManager {
	
	private static FiguresManager instance;
	
	//private JList<String> figuresViewList;
	
	private JTree figuresView;
	
	private Set<String> loadedPackages;
	private List<Figure> figures;

	public static final double ROTATION_STEP = 5.0;

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
				SceneManager.instance().repaint();
			}
			@Override
		    public void mouseExited(MouseEvent e) {
				mouseOnCanvas = false;
				SceneManager.instance().repaint();
			}
			@Override
		    public void mouseDragged(MouseEvent e){
				if (selectedFigure != null) {
					selectedFigure.move(e.getX() - SceneManager.instance().getOffsetX(), e.getY() - SceneManager.instance().getOffsetX());
				}
				SceneManager.instance().repaint();
			}
			@Override
		    public void mouseMoved(MouseEvent e){
				if (selectedFigure != null) {
					selectedFigure.move(e.getX() - SceneManager.instance().getOffsetX(), e.getY() - SceneManager.instance().getOffsetX());
				}
				SceneManager.instance().repaint();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (selectedFigure != null) {
						selectedFigure.drawStart();
					}
				}
				SceneManager.instance().repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (selectedFigure != null) {
					if (e.getButton() == MouseEvent.BUTTON1 )
						selectedFigure.drawEnd();
				}
				SceneManager.instance().repaint();
			}

		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	if (selectedFigure != null) {
		    		selectedFigure.rotate(e.getWheelRotation() * ROTATION_STEP);
		    		SceneManager.instance().repaint();
		    	}
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
	
	private void initKeyboard() {
		currentKeyboardState = new KeyboardState();
		
		Main.addKeyboardEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {

				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (selectedFigure != null) {
						if (e.getKeyCode() == KeyEvent.VK_Q) {
							selectedFigure.rotate(-FiguresManager.ROTATION_STEP);
						}
						
						if (e.getKeyCode() == KeyEvent.VK_E) {
							selectedFigure.rotate(FiguresManager.ROTATION_STEP);
						}
						SceneManager.instance().repaint();
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
						SceneManager.instance().repaint();
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
		
	/*
	 * Connect list from swing view
	 */
	/*
	public void connectList(JList<String> figuresList) {
		this.figuresViewList = figuresList;

		figuresList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedIndex = (((JList<String>)e.getSource()).getSelectedIndex());
				if (selectedIndex != -1) {
					//Main.getCurrentScene().selectFigure(figures.get(((JList<String>)e.getSource()).getSelectedIndex()));
					selectedFigure = figures.get(((JList<String>)e.getSource()).getSelectedIndex());
				} else {
					//Main.getCurrentScene().selectFigure(null);
					selectedFigure = null;
				}
			}
		});
	}*/
	
	public void updateView() {
		//TODO add
		System.out.println("update");
//		DefaultMutableTreeNode root = new DefaultMutableTreeNode("figures_root");
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)figuresView.getModel().getRoot();
		root.removeAllChildren();
		
		DefaultMutableTreeNode currentNode = null;
		String currentPackage = "";
		
		for (Figure f : figures) {
			System.out.println("add");
			if (!f.getPackageName().equals(currentPackage)) {
				currentNode = new DefaultMutableTreeNode(f.getPackageName());
				root.add(currentNode);
				currentPackage = f.getPackageName();
			}
			currentNode.add(new DefaultMutableTreeNode(f));
		}
		/*
		DefaultTreeModel model = (DefaultTreeModel) figuresView.getModel();
		model.nodeChanged(root);*/
		
		DefaultTreeModel model = new DefaultTreeModel(root);
		figuresView.setModel(model);
		
		/*
DefaultListModel<String> model = new DefaultListModel<>();
		
		for (Figure f : figures) {
			model.addElement(f.getName());
		}

		figuresViewList.setModel(model);*/
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
				}
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
		
		//RulesManager.getInstance().loadRules(fileName);
		
		updateView();		
	}
	
	/*
	 * Remove package with specified name
	 */
	public void removePackage(String packageName) {
		Iterator<Figure> it = figures.iterator();
		
		while (it.hasNext()) {
			Figure f = it.next();
			if (f.getPackageName().equals(packageName)) {
				it.remove();
			}
		}
	}

	/*
	 * Update swing list view 
	 */
	/*
	private void updateListView() {
		DefaultListModel<String> model = new DefaultListModel<>();
		
		for (Figure f : figures) {
			model.addElement(f.getName());
		}

		figuresViewList.setModel(model);
	}*/
	
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
			selectedFigure.draw(graphics, SceneManager.instance().getOffsetX(), SceneManager.instance().getOffsetX());
		}
	}
}
