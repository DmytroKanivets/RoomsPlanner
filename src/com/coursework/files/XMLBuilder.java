package com.coursework.files;

import com.coursework.main.Debug;

public class XMLBuilder {
	
	private XMLWriter writer;
	
	XMLTag current;
	
	public XMLBuilder(String filename) {
		writer = new XMLWriter(filename);
	}
	/*
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
	*/
    public void beginProperty(String name) {
        if (current == null) {
            current = new XMLTag(null, name);
            writer.setRoot(current);
        } else {
            XMLTag tag = new XMLTag(current, name);
            current.addInnerTag(tag);
            current = tag;
        }
    }

    public void endProperty(String name) {
        if (name == current.getName()) {
            current = current.getParent();
        } else {
            //Something done wrong
            Debug.log("Incorrect XML use");
        }
    }

	public void addProperty(String name, Object value) {
        beginProperty(name);
        current.addContent(value.toString());
        endProperty(name);
	}

	public void flush() {
		writer.write();
	}
}
