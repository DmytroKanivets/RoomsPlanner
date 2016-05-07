package com.coursework.editor;

public class KeyboardState {
	
	boolean shiftPressed, controlPressed;
	
	public boolean isShiftPressed() {
		return shiftPressed;
	}
	
	public void setShiftState(boolean pressed) {
		shiftPressed = pressed;
	}
	
	public boolean isControlPressed() {
		return controlPressed;
	}
	
	public void setControlState(boolean pressed) {
		controlPressed = pressed;
	}
}
