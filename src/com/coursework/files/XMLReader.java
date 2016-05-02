package com.coursework.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Scanner;

public class XMLReader {
	
	//TODO remove debug
	void printTag(XMLTag t, int depth) {
		String prefix = "";
		for (int i = 0;i < depth; i++) {
			prefix += '\t';
		}
		System.out.println(prefix + "<"+t.getName()+">");

		System.out.println(prefix + '\t' + t.getContent());
		Collection<XMLTag> tags = t.getInnerTags();
		for (XMLTag tag : tags) {
			printTag(tag, depth + 1);
		}
		System.out.println(prefix + "</"+t.getName()+">");
	}
	
	
	public XMLReader(String filename) throws FileNotFoundException {
		Scanner s = new Scanner(new File(filename));
		
		current = new XMLTag(null);
		current.setName("root");
		while (s.hasNextLine()) {
			processString(s.nextLine());
		}
		s.close();
	}	
	
	XMLTag current;
	XMLTag root;
	
	private static final char[] space = {' ', '\t'};
	
	private String trim(String source) {
		int startIndex = -1, lastIndex = source.length();

		boolean spacing;
		do {
			startIndex++;
			spacing = false;
			for (int i = 0;i < space.length; i++) {
				if (space[i] == source.charAt(startIndex)) {
					spacing = true;
				}
			}
		} while (spacing);
		
		do {
			lastIndex--;
			spacing = false;
			for (int i = 0;i < space.length; i++) {
				if (space[i] == source.charAt(lastIndex)) {
					spacing = true;
				}
			}
		} while (spacing);
		
		return source.substring(startIndex, lastIndex + 1);
	}
	
	private void processString(String s) {
		s = trim(s);
		if (s.charAt(0) == '<' && s.charAt(s.length()-1) == '>') {
			if (s.charAt(1) == '/') {
				current = current.getParent();
			} else {
				XMLTag newTag = new XMLTag(current);
				newTag.setName(s.substring(1, s.length()-1));
				current.addInnerTag(newTag);
				current = newTag;
				if (root == null) {
					root = current;
				}
			}
		} else {
			current.addContent(s);
		}
	}
	
	public XMLTag getRoot() {
		//printTag(root, 0);
		return root;
	}
}
