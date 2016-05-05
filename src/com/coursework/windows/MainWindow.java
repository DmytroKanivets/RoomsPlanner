/*
 * Created by JFormDesigner on Tue Apr 12 14:34:20 EEST 2016
 */

package com.coursework.windows;

import com.coursework.editor.FiguresManager;
import com.coursework.editor.Scene;
import com.coursework.files.PackageLoader;
import com.coursework.files.SceneLoader;
import com.coursework.main.Debug;
import com.coursework.main.Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.IOException;

import com.coursework.main.Settings;

/**
 * @author D PUpkin
 */
public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MainWindow() {
	
		initComponents();
		if (Settings.getInstance().get("debug").equals("true")) {
			initDebug();
		}
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
			SceneLoader.loadScene(fileChooser.getSelectedFile().getAbsolutePath());
			Main.redraw();
			/*
			String fileName = fileChooser.getSelectedFile().getAbsolutePath();
			Main.getCurrentScene().saveToFile(fileName + ".scene");*/
		}
		/*
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			Debug.log("Scene file chosen: " + selectedFile.getAbsolutePath());
			//TODO file open
		}*/
	}
	
	private void saveSchemeClick(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Drawings file", "scene");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			String fileName = fileChooser.getSelectedFile().getAbsolutePath();
			Main.getCurrentScene().saveToFile(fileName + ".scene");
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
			FiguresManager.getInstance().addPackage(selectedFile.getAbsolutePath());
		}
	}
	
	public JList<String> getFiguresList() {
		return figuresList;
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
		figuresList = new JList();
		canvas = new Canvas();
		infoPanel = new JPanel();

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

				//---- figuresList ----
				figuresList.setEnabled(false);
				figuresList.setBorder(null);
				figuresList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				figuresScroll.setViewportView(figuresList);
			}

			GroupLayout buttonPanelLayout = new GroupLayout(buttonPanel);
			buttonPanel.setLayout(buttonPanelLayout);
			buttonPanelLayout.setHorizontalGroup(
				buttonPanelLayout.createParallelGroup()
					.addComponent(figuresScroll, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
			);
			buttonPanelLayout.setVerticalGroup(
				buttonPanelLayout.createParallelGroup()
					.addGroup(buttonPanelLayout.createSequentialGroup()
						.addComponent(figuresScroll, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
						.addGap(0, 352, Short.MAX_VALUE))
			);
		}

		//======== infoPanel ========
		{

			GroupLayout infoPanelLayout = new GroupLayout(infoPanel);
			infoPanel.setLayout(infoPanelLayout);
			infoPanelLayout.setHorizontalGroup(
				infoPanelLayout.createParallelGroup()
					.addGap(0, 135, Short.MAX_VALUE)
			);
			infoPanelLayout.setVerticalGroup(
				infoPanelLayout.createParallelGroup()
					.addGap(0, 491, Short.MAX_VALUE)
			);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(canvas, GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(canvas, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
						.addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(infoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		figuresScroll.setBorder(BorderFactory.createEmptyBorder());
		figuresList.setEnabled(true);
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
	private JList figuresList;
	private Canvas canvas;
	private JPanel infoPanel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private JMenu debug;
	private JMenuItem debugShow;
	//private JList<String> figuresList;
}
