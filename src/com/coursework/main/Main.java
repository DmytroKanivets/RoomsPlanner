package com.coursework.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.coursework.windows.AboutWindow;
import com.coursework.windows.DebugWindow;
import com.coursework.windows.MainWindow;

public class Main {

	static JFrame mainWindow;
	static JFrame aboutWindow;
	static DebugWindow debugWindow;
	
	
	public static void main(String[] args) {

		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		Debug.clear();
		
		SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	Settings.init();
	            	Debug.log("Settings loaded");
	            	mainWindow = new MainWindow();
	            	mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	            	mainWindow.setVisible(true);
	            	Debug.log("Application started");
	            }
	        });
		SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	aboutWindow = new AboutWindow();
	            }
	        });
		SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	       		 debugWindow = new DebugWindow();
	            }
	        });
		 

	}


	public static void showAboutWindow() {
		if (aboutWindow != null) {
			aboutWindow.setVisible(true);
		} else {
			//TODO add debug
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
