/*
 * Created by JFormDesigner on Tue Apr 12 14:34:20 EEST 2016
 */

package com.coursework.windows;

import com.coursework.main.Debug;
import com.coursework.main.Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.io.File; 

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
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			Debug.log("File chosen: " + selectedFile.getName());
			//TODO file open
		}
	}
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - D PUpkin
		menuBar = new JMenuBar();
		file = new JMenu();
		fileOpen = new JMenuItem();
		fileSave = new JMenuItem();
		fileExit = new JMenuItem();
		help = new JMenu();
		helpAbout = new JMenuItem();
		buttonPanel = new JPanel();
		drawWall = new JToggleButton();
		drawDelete = new JToggleButton();
		drawDoor = new JToggleButton();
		canvas = new Canvas();

		//======== this ========
		Container contentPane = getContentPane();

		//======== menuBar ========
		{

			//======== file ========
			{
				file.setText("File");

				//---- fileOpen ----
				fileOpen.setText("Open");
				fileOpen.addActionListener(e -> fileOpenActionClick(e));
				file.add(fileOpen);

				//---- fileSave ----
				fileSave.setText("Save");
				file.add(fileSave);
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


			//---- drawWall ----
			drawWall.setText("Wall");

			//---- drawDelete ----
			drawDelete.setText("Delete");

			//---- drawDoor ----
			drawDoor.setText("Door");

			GroupLayout buttonPanelLayout = new GroupLayout(buttonPanel);
			buttonPanel.setLayout(buttonPanelLayout);
			buttonPanelLayout.setHorizontalGroup(
				buttonPanelLayout.createParallelGroup()
					.addGroup(buttonPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(buttonPanelLayout.createParallelGroup()
							.addComponent(drawWall, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addComponent(drawDoor, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addComponent(drawDelete))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			buttonPanelLayout.setVerticalGroup(
				buttonPanelLayout.createParallelGroup()
					.addGroup(buttonPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(drawWall)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(drawDoor)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 324, Short.MAX_VALUE)
						.addComponent(drawDelete)
						.addContainerGap())
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
					.addComponent(canvas, GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(canvas, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE))
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - D PUpkin
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem fileOpen;
	private JMenuItem fileSave;
	private JMenuItem fileExit;
	private JMenu help;
	private JMenuItem helpAbout;
	private JPanel buttonPanel;
	private JToggleButton drawWall;
	private JToggleButton drawDelete;
	private JToggleButton drawDoor;
	private Canvas canvas;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private JMenu debug;
	private JMenuItem debugShow;
}
