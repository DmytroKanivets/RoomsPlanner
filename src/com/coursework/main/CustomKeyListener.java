package com.coursework.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CustomKeyListener implements KeyListener {
	@Override
	public void keyTyped(KeyEvent e) {
		//System.out.println("Event");
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_Z && Main.getKeyboardState().isControlPressed()) {
			System.out.println("Z");
			Main.getCurrentScene().undo();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Event");
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_SHIFT) {
			System.out.println("Shift down");
			Main.getKeyboardState().setShiftState(true);
		}
		if (keyCode == KeyEvent.VK_CONTROL) {
			System.out.println("Ctrl down");
			Main.getKeyboardState().setControlState(true);
		}
		
		//TODO here?
		if (keyCode == KeyEvent.VK_Z && Main.getKeyboardState().isControlPressed()) {
			System.out.println("Z");
			Main.getCurrentScene().undo();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println("Event");
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_SHIFT) {
			System.out.println("Shift up");
			Main.getKeyboardState().setShiftState(false);
		}
		if (keyCode == KeyEvent.VK_CONTROL) {
			System.out.println("Ctrl up");
			Main.getKeyboardState().setControlState(false);
		}
	}
} 