package com.coursework.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.coursework.main.Debug;

public class MouseListener extends MouseAdapter {
	
	@Override
	public void mouseClicked (MouseEvent e) {
		/*
		if (canPlace()) {
			shapes.add(new Rectangle(e.getX() -size, e.getY() -size, size*2, size*2));
		}*/
		//repaint();
      	 // System.out.println("MD");
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		/*
		tempShape = new Rectangle(e.getX() -size, e.getY() -size, size*2, size*2);
    	  //drawEnd = new Point(e.getX(), e.getY());
      	  System.out.println("MM");
      	  repaint();*/
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {/*
		size += e.getUnitsToScroll();
		tempShape = new Rectangle(e.getX() -size, e.getY() -size, size*2, size*2);
		repaint();	*/
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Debug.log("drag");
	}
}
