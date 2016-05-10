package com.coursework.files;

public class XMLBuilder {
	
	private XMLWriter writer;
	
	XMLTag current;
	
	public XMLBuilder(String filename) {
		writer = new XMLWriter(filename);
	}
	
	public void addTag(String name) {
		if (current == null) {
			current = new XMLTag(null, name);
			writer.setRoot(current);
		} else {
			XMLTag tag = new XMLTag(current, name);
			current.addInnerTag(tag);
			current = tag;
		}
	}
	
	public void closeTag() {
		current = current.getParent();
	}
	
	public void addContent(String info) {
		current.addContent(info);
	}
	
	public void flush() {
		writer.write();
	}
}
