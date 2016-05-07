package com.coursework.main;

import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;

import com.coursework.editor.KeyboardState;
import com.coursework.editor.Scene;
import com.coursework.figures.FiguresManager;
import com.coursework.windows.AboutWindow;
import com.coursework.windows.DebugWindow;
import com.coursework.windows.MainWindow;

import sun.management.snmp.jvmmib.JvmCompilationMeta;

public class Main {

	static MainWindow mainWindow;
	static AboutWindow aboutWindow;
	static DebugWindow debugWindow;
	
	static Scene currentScene;
	
	static KeyboardState currentKeyboardState;	
	
	public static void main(String[] args) {
		
		//Set window style
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		//Init settings and debug and start main window
		SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	        		Debug.clear();
	            	Settings.init();
	            	Debug.log("Settings loaded");
	            	
	       		 	debugWindow = new DebugWindow();
	            	aboutWindow = new AboutWindow();
	            	mainWindow = new MainWindow();
	            	
	            	currentKeyboardState = new KeyboardState();
	            		            	        	            	
	            	Debug.log("Windows created");
            	
	            	currentScene = new Scene();
	            	Debug.log("Scene manager initialised");
	            	
	            	FiguresManager.getInstance().initList(mainWindow.getFiguresList());
	            	//TODO move to settings
	            	FiguresManager.getInstance().addPackage("data/default.figures");
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
	
	public static KeyboardState getKeyboardState() {
		return currentKeyboardState;
	}
	
	public static void redraw() {
		mainWindow.getCanvas().repaint();
	}
	
	public static void resetScene() {
		currentScene.clear();
		redraw();
	}
	
	public static void drawFigures(Graphics2D g) {
		if (currentScene != null)
			currentScene.drawScene(g);
	}
	
	public static void addCanvasMouseListener(MouseInputAdapter mouse) {
		mainWindow.getCanvas().addMouseListener(mouse);
		mainWindow.getCanvas().addMouseMotionListener(mouse);
		mainWindow.getCanvas().addMouseWheelListener(mouse);
	}
	
	public static Scene getCurrentScene() {
		return currentScene;
	}
	
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
		} else {
			//TODO add debug
		}
		
	}
	
	/*
	 
class CustomKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}
	
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_SHIFT) {
				System.out.println("Shift down");
			}
		}
	
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	} 
	 */
}
