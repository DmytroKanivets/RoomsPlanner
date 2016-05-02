package com.coursework.files;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class XMLWriter {
	XMLTag root;

	
	String fileName;
	
	public XMLWriter(String fileName) {
		this.fileName = fileName; 
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
			writer = new PrintWriter(fileName);
			writer.write(root.getAsText(0));
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
