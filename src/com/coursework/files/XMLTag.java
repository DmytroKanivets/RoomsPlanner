package com.coursework.files;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class XMLTag {
	private List<XMLTag> inner;
	
	private XMLTag parent;
	
	private String name;
	private String content;
	
	public XMLTag(XMLTag parent, String name) {
		this.parent = parent;
		this.name = name;
		inner = new LinkedList<XMLTag>();
	}
	
	public XMLTag getParent() {
		return parent;
	}
	/*
	public void setName(String name) {
		this.name = name;
	}*/
	
	public String getName() {
		return name;
	}
	
	public String getContent() {
		return content;
	}
	
	public void addContent(String s) {
		if (content == null)
			content = s;
		else 
			content += '\n' + s;
	}
	
	public void addInnerTag(XMLTag tag) {
		inner.add(tag);
	}
	
	public XMLTag getInnerTag(String name) {
		for (XMLTag tag : inner) {
			if (tag.name.equals(name)) {
				return tag;
			}
		}
		return null;
	}
	
	public Collection<XMLTag> getInnerTags() {
		return inner;
	}
	
	public String getAsText(int depth) {
		String prefix = "";
		for (int i = 0;i < depth; i++)
			prefix += '\t';
		String result = "";

		result += prefix + "<" + name + ">\n";
		if (content != null) {
			result += prefix + '\t' + content +  '\n';
		}
		for (XMLTag t : inner) {
			result += t.getAsText(depth + 1);
		}
		result += prefix + "</" + name + ">\n";
		
		return result;
	}
	
}
