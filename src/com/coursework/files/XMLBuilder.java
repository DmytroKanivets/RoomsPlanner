package com.coursework.files;

public class XMLBuilder {
	
	private XMLWriter writer;
	
	/*
	
	XMLWriter writer = new XMLWriter(fileName);
		
		XMLTag root = new XMLTag(null);
		root.setName("scene");
		writer.setRoot(root);
		
		XMLTag shTag = new XMLTag(tag);
			shTag.setName("isStraighten");
			shTag.addContent(Boolean.toString(isStraighten));
			tag.addInnerTag(shTag);
		
		writer.write();
		
		
	
	*/
	XMLTag current;
	
	
	public XMLBuilder(String filename) {
		writer = new XMLWriter(filename);
	}
	
	public void addTag(String name) {
		if (current == null) {
			current = new XMLTag(null);
			writer.setRoot(current);
		} else {
			XMLTag tag = new XMLTag(current);
			current.addInnerTag(tag);
			current = tag;
		}
		current.setName(name);
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
