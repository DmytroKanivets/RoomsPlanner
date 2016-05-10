package com.coursework.files;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.coursework.main.Debug;

public class XMLWriter {
	XMLTag root;

	String filename;
	
	public XMLWriter(String filename) {
		this.filename = filename; 
	}
	
	public void setRoot(XMLTag root) {
		this.root = root;
	}
	
	public XMLTag getRoot() {
		return root;
	}
	
	public void addToRoot(XMLTag tag) {
		root.addInnerTag(tag);
	}
	
	public void write() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename);
			writer.write(root.getAsText(0));
			writer.close();
		} catch (FileNotFoundException e) {
			Debug.log("Can not find file " + filename);
		}
	}
}
