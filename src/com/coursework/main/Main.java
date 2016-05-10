package com.coursework.main;

import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;

import com.coursework.editor.Scene;
import com.coursework.editor.SceneManager;
import com.coursework.figures.FiguresManager;
import com.coursework.windows.AboutWindow;
import com.coursework.windows.DebugWindow;
import com.coursework.windows.MainWindow;

public class Main {

	static MainWindow mainWindow;
	static AboutWindow aboutWindow;
	static DebugWindow debugWindow;
	
	//static Scene currentScene;
	
	public static void showMessage(String message) {
		JOptionPane.showMessageDialog(mainWindow, message);
	}
	
	public static void main(String[] args) {
		
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			Debug.log("Can not set system UI style");
		    e.printStackTrace();
		}
		
		//Init settings and debug and start main window
		SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	        		Debug.clear();
	            	
	       		 	debugWindow = new DebugWindow();
	            	aboutWindow = new AboutWindow();
	            	mainWindow = new MainWindow();
	            		            	        	            	
	            	Debug.log("Windows created");
            	
	            	//currentScene = new Scene();
	            	SceneManager.instance().setCanvas(mainWindow.getCanvas());
	            	SceneManager.instance().newScene();
	            	
	            	Debug.log("Scene manager initialised");
	            	
	            	FiguresManager.getInstance().connectView(mainWindow.getFiguresView());
	            	
	            	//TODO move to settings        
	            	try {
						FiguresManager.getInstance().addPackage("data/default.figures");
					} catch (FileNotFoundException e) {
						showMessage("Can not load default pack");
					}
	            	Debug.log("Default figures loaded");
	            	
	            	mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	            	mainWindow.setVisible(true);
	            	Debug.log("Application started");
	            }
	        });		
	}

	public static void addKeyboardEventDispatcher(KeyEventDispatcher k) {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    	manager.addKeyEventDispatcher(k);
	}
	
	public static void redraw() {
		mainWindow.getCanvas().repaint();
	}
	
	public static void resetScene() {
		SceneManager.instance().removeCurrent();
    	SceneManager.instance().newScene();
	}
	/*
	public static void drawScene(Graphics2D g) {
		if (currentScene != null)
			currentScene.drawScene(g);
	}
	
	public static Scene getCurrentScene() {
		return currentScene;
	}
	*/
	public static void showAboutWindow() {
		if (aboutWindow != null) {
			aboutWindow.setVisible(true);
		} else {
			Debug.log("About window");
		}	
	}
	
	public static void showDebugWindow() {
		if (debugWindow != null) {
			debugWindow.renew();
			debugWindow.setVisible(true);
		}		
	}

	public static void addCanvasMouseListener(MouseInputAdapter mouse) {
		mainWindow.getCanvas().addMouseListener(mouse);
		mainWindow.getCanvas().addMouseMotionListener(mouse);
		mainWindow.getCanvas().addMouseWheelListener(mouse);
	}
}
