/*
 * Created by JFormDesigner on Tue Apr 12 14:34:20 EEST 2016
 */

package com.coursework.windows;

import com.coursework.editor.SceneManager;
import com.coursework.figures.FiguresManager;
import com.coursework.main.Debug;
import com.coursework.main.Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author D PUpkin
 */

//TODO remove warnings from jlist
public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEBUG_ENABLED = true;
	
	public MainWindow() {
	
		initComponents();
		if (DEBUG_ENABLED) {
			initDebug();
		}
//		figuresList.setFocusable(false);
		Debug.log("Main window initialized");
	}

	private void helpAboutClick(ActionEvent e) {
		Main.showAboutWindow();
	}
	
	private void debugShowClick(ActionEvent e) {
		Main.showDebugWindow();
	}

	private void fileExitClick(ActionEvent e) {
		Frame[] frames = Frame.getFrames();
		
		for (int i = 0;i < frames.length; i++) {
			frames[i].dispatchEvent(new WindowEvent(frames[i], WindowEvent.WINDOW_CLOSING));
		}
	}

	private void initDebug() {
		
		debug = new JMenu();
		debugShow = new JMenuItem();
		
		debug.setText("Debug");

		debugShow.setText("Show");
		debugShow.addActionListener(e -> debugShowClick(e));
		debug.add(debugShow);
		
		menuBar.add(debug);
	}

	private void fileOpenActionClick(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Drawings file", "scene");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				//Main.getCurrentScene().loadFromFile();

				SceneManager.instance().loadSceneFromFile(fileChooser.getSelectedFile().getAbsolutePath());
			} catch (FileNotFoundException e1) {
				Main.showMessage("File not found");
				Debug.log("File " + fileChooser.getSelectedFile().getAbsolutePath() + " not found");
			}
			Main.redraw();
		}
	}
	
	private void saveSchemeClick(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Drawings file", "scene");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			String fileName = fileChooser.getSelectedFile().getAbsolutePath();
			SceneManager.instance().saveSceneToFile(fileName + ".scene");
			//Main.getCurrentScene().saveToFile(fileName + ".scene");
		}
	}
	
	private void loadPackageActionClick(ActionEvent e) {
		String path = System.getProperty("user.home");
		try {
			path = new File(".").getCanonicalPath() + "/data";
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
		JFileChooser fileChooser = new JFileChooser(path);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Furniture package", "figures");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			Debug.log("Figures file chosen: " + selectedFile.getAbsolutePath());
			try {
				FiguresManager.getInstance().addPackage(selectedFile.getAbsolutePath());
			} catch (FileNotFoundException e1) {
				Main.showMessage("File not found");
			}
		}
	}
	/*
	public JList<String> getFiguresList() {
		return figuresList;
	}
*/
	public JTree getFiguresView() {
		return figuresTree;
	}
	
	private void newFileClick(ActionEvent e) {
		Main.resetScene();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - D Pupkin
		menuBar = new JMenuBar();
		file = new JMenu();
		newFile = new JMenuItem();
		fileOpen = new JMenuItem();
		fileSave = new JMenuItem();
		loadPackage = new JMenuItem();
		fileExit = new JMenuItem();
		help = new JMenu();
		helpAbout = new JMenuItem();
		buttonPanel = new JPanel();
		figuresScroll = new JScrollPane();
		figuresTree = new JTree();
		redoButton = new JButton();
		deleteButton = new JButton();
		undoButton = new JButton();
		canvas = new Canvas();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== menuBar ========
		{

			//======== file ========
			{
				file.setText("File");

				//---- newFile ----
				newFile.setText("New file");
				newFile.addActionListener(e -> newFileClick(e));
				file.add(newFile);
				file.addSeparator();

				//---- fileOpen ----
				fileOpen.setText("Open file");
				fileOpen.addActionListener(e -> fileOpenActionClick(e));
				file.add(fileOpen);

				//---- fileSave ----
				fileSave.setText("Save file");
				fileSave.addActionListener(e -> saveSchemeClick(e));
				file.add(fileSave);
				file.addSeparator();

				//---- loadPackage ----
				loadPackage.setText("Load figures package");
				loadPackage.addActionListener(e -> loadPackageActionClick(e));
				file.add(loadPackage);
				file.addSeparator();

				//---- fileExit ----
				fileExit.setText("Exit");
				fileExit.addActionListener(e -> fileExitClick(e));
				file.add(fileExit);
			}
			menuBar.add(file);

			//======== help ========
			{
				help.setText("Help");

				//---- helpAbout ----
				helpAbout.setText("About");
				helpAbout.addActionListener(e -> helpAboutClick(e));
				help.add(helpAbout);
			}
			menuBar.add(help);
		}
		setJMenuBar(menuBar);

		//======== buttonPanel ========
		{

			// JFormDesigner evaluation mark
			buttonPanel.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
					"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
					javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
					java.awt.Color.red), buttonPanel.getBorder())); buttonPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


			//======== figuresScroll ========
			{
				figuresScroll.setViewportBorder(null);

				//---- figuresTree ----
				figuresTree.setFocusable(false);
				figuresScroll.setViewportView(figuresTree);
			}

			//---- redoButton ----
			redoButton.setText("Redo");
			redoButton.setFocusable(false);

			//---- deleteButton ----
			deleteButton.setText("Delete");
			deleteButton.setFocusable(false);

			//---- undoButton ----
			undoButton.setText("Undo");
			undoButton.setFocusable(false);

			GroupLayout buttonPanelLayout = new GroupLayout(buttonPanel);
			buttonPanel.setLayout(buttonPanelLayout);
			buttonPanelLayout.setHorizontalGroup(
				buttonPanelLayout.createParallelGroup()
					.addGroup(buttonPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(buttonPanelLayout.createParallelGroup()
							.addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(redoButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(figuresScroll, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addComponent(canvas, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
						.addContainerGap())
			);
			buttonPanelLayout.setVerticalGroup(
				buttonPanelLayout.createParallelGroup()
					.addGroup(GroupLayout.Alignment.TRAILING, buttonPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
							.addComponent(canvas, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
							.addGroup(buttonPanelLayout.createSequentialGroup()
								.addComponent(figuresScroll, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
								.addGap(18, 18, 18)
								.addComponent(undoButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(redoButton)
								.addGap(18, 18, 18)
								.addComponent(deleteButton)))
						.addContainerGap())
			);
		}
		contentPane.add(buttonPanel, BorderLayout.WEST);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		figuresScroll.setBorder(BorderFactory.createEmptyBorder());
		//figuresList.setEnabled(true);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - D Pupkin
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem newFile;
	private JMenuItem fileOpen;
	private JMenuItem fileSave;
	private JMenuItem loadPackage;
	private JMenuItem fileExit;
	private JMenu help;
	private JMenuItem helpAbout;
	private JPanel buttonPanel;
	private JScrollPane figuresScroll;
	private JTree figuresTree;
	private JButton redoButton;
	private JButton deleteButton;
	private JButton undoButton;
	private Canvas canvas;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private JMenu debug;
	private JMenuItem debugShow;
	//private JList<String> figuresList;
}
