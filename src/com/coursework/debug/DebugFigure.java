package com.coursework.debug;

import java.awt.Graphics2D;
import java.awt.geom.Area;

import com.coursework.figures.Figure;

public class DebugFigure  {

	String name;
	String pack;
	
	public DebugFigure(String pack, String name) {
		this.name = name;
		this.pack = pack;
	}
	

	public String getPackageName() {
		return pack; 
	}

	
	public String getName() {
		return name;
	}

	
	public void selfPaint(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	
	public void addArea(Area a) {
		// TODO Auto-generated method stub
		
	}

	
	public void subtract(Area a) {
		// TODO Auto-generated method stub
		
	}
	
}
