/*
 * Created by JFormDesigner on Tue Apr 12 16:27:25 EEST 2016
 */

package com.coursework.windows;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;

import com.coursework.main.Debug;

/**
 * @author D PUpkin
 */
public class DebugWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7246859898236976005L;
	
	public DebugWindow() {
		initComponents();
		Debug.log("Debug window initialized");
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - D PUpkin
		scrollPane = new JScrollPane();
		textArea = new JTextArea();

		//======== this ========
		Container contentPane = getContentPane();

		//======== scrollPane ========
		{

			//---- textArea ----
			textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
			textArea.setEditable(false);
			scrollPane.setViewportView(textArea);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	public void renew() {
		textArea.setText(Debug.getAll());
	}
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - D PUpkin
	private JScrollPane scrollPane;
	private JTextArea textArea;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
