package com.coursework.main;

import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;

import com.coursework.editor.FiguresManager;
import com.coursework.editor.Scene;
import com.coursework.windows.AboutWindow;
import com.coursework.windows.DebugWindow;
import com.coursework.windows.MainWindow;

public class Main {

	static MainWindow mainWindow;
	static AboutWindow aboutWindow;
	static DebugWindow debugWindow;
	
	static Scene currentScene;
	
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
	            	
	            	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	            	manager.addKeyEventDispatcher(new KeyEventDispatcher() {
						
						@Override
						public boolean dispatchKeyEvent(KeyEvent e) {
							//Ctrl z keycode
							int code = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode();
							if  (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == code && e.isControlDown()) {
								currentScene.undo();
								return true;
							}
							return false;
						}
					});
	            	
	            	Debug.log("Windows created");

	            	
	            	
	            	currentScene = new Scene();
	            	Debug.log("Scene manager initialised");
	            	
	            	FiguresManager.getInstance().initList(mainWindow.getFiguresList());
	            	Debug.log("Default figures loaded");
	            	
	            	mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	            	mainWindow.setVisible(true);
	            	Debug.log("Application started");
	            }
	        });		
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

}
