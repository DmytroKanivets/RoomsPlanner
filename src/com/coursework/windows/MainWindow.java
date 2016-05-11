/*
 * Created by JFormDesigner on Tue Apr 12 14:34:20 EEST 2016
 */

package com.coursework.windows;

import com.coursework.editor.ScenesManager;
import com.coursework.editor.figures.FiguresManager;
import com.coursework.main.Debug;
import com.coursework.main.Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author D PUpkin
 */

public class MainWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4577278481266674054L;
	
	private static final boolean DEBUG_ENABLED = true;
	
	public MainWindow() {
	
		initComponents();
		if (DEBUG_ENABLED) {
			initDebug();
		}
		ScenesManager.instance().setDeleteButton(deleteButton);
		ScenesManager.instance().setUndoButton(undoButton);
		ScenesManager.instance().setRedoButton(redoButton);
		
		
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
				ScenesManager.instance().loadSceneFromFile(fileChooser.getSelectedFile().getAbsolutePath());
			} catch (FileNotFoundException e1) {
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
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			ScenesManager.instance().saveSceneToFile(filename + (filename.endsWith(".scene") ? "" : ".scene"));
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
				//Main.showMessage("File not found");
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

	private void redoButtonActionPerformed(ActionEvent e) {
		ScenesManager.instance().redo();
	}

	private void deleteButtonActionPerformed(ActionEvent e) {
		ScenesManager.instance().delete();
	}

	private void undoButtonActionPerformed(ActionEvent e) {
		ScenesManager.instance().undo();
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
		canvas = new Canvas();
		redoButton = new JButton();
		undoButton = new JButton();
		deleteButton = new JButton();
		figuresScroll = new JScrollPane();
		figuresTree = new JTree();

		//======== this ========
		Container contentPane = getContentPane();

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

		//---- redoButton ----
		redoButton.setText("Redo");
		redoButton.setFocusable(false);
		redoButton.addActionListener(e -> redoButtonActionPerformed(e));

		//---- undoButton ----
		undoButton.setText("Undo");
		undoButton.setFocusable(false);
		undoButton.setPreferredSize(new Dimension(58, 23));
		undoButton.addActionListener(e -> undoButtonActionPerformed(e));

		//---- deleteButton ----
		deleteButton.setText("Delete");
		deleteButton.setFocusable(false);
		deleteButton.addActionListener(e -> deleteButtonActionPerformed(e));

		//======== figuresScroll ========
		{
			figuresScroll.setViewportBorder(null);

			//---- figuresTree ----
			figuresTree.setFocusable(false);
			figuresScroll.setViewportView(figuresTree);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(35, 35, 35)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
								.addComponent(redoButton, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
								.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(figuresScroll, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(canvas, GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(10, 10, 10)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(0, 15, Short.MAX_VALUE)
							.addComponent(figuresScroll, GroupLayout.PREFERRED_SIZE, 421, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(redoButton)
							.addGap(18, 18, 18)
							.addComponent(deleteButton))
						.addComponent(canvas, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		figuresScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		canvas.add(label);
		ScenesManager.instance().setCanvalLabel(label);
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
	private Canvas canvas;
	private JButton redoButton;
	private JButton undoButton;
	private JButton deleteButton;
	private JScrollPane figuresScroll;
	private JTree figuresTree;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private JMenu debug;
	private JMenuItem debugShow;
	//private JList<String> figuresList;
}
