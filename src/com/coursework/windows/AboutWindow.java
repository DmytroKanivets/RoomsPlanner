/*
 * Created by JFormDesigner on Tue Apr 12 14:49:59 EEST 2016
 */

package com.coursework.windows;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;

import com.coursework.main.Debug;

/**
 * @author D PUpkin
 */
public class AboutWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1243187947111396972L;

	public AboutWindow() {
		initComponents();
		setResizable(false);
		Debug.log("About window initialized");
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - D PUpkin
		label1 = new JLabel();

		//======== this ========
		Container contentPane = getContentPane();

		//---- label1 ----
		label1.setText("<html>\n\tBuildings planner\n\t<br><br>\n\tCreated by Kanivets Dmytro\n</html>");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(label1, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(label1, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - D PUpkin
	private JLabel label1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
