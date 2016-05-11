package com.coursework.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class XMLReader {
	
	public XMLReader(String filename) throws FileNotFoundException {
		Scanner s = new Scanner(new File(filename));
		
		current = new XMLTag(null, "root");
		while (s.hasNextLine()) {
			processString(s.nextLine());
		}
		s.close();
		
	}	
	
	XMLTag current;
	XMLTag root;
	
	private static final char[] space = {' ', '\t'};
	
	private String trim(String source) {
		if (source == null || source.length() == 0) {
			return source;
		}
		
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
		} while (spacing && startIndex < source.length() -1);
		
		do {
			lastIndex--;
			spacing = false;
			for (int i = 0;i < space.length; i++) {
				if (space[i] == source.charAt(lastIndex)) {
					spacing = true;
				}
			}
		} while (spacing && lastIndex > 0);
		
		return source.substring(startIndex, lastIndex + 1);
	}
	
	private void processString(String s) {
		s = trim(s);
		
		int tagStart = s.indexOf('<');
		int tagEnd =  s.indexOf('>');
		if (tagStart != -1 && tagEnd > tagStart) {
			if (tagStart != 0) {
				current.addContent(s.substring(0, tagStart));
			}
			if (s.charAt(tagStart + 1) == '/') {
				current = current.getParent();
			} else {
				XMLTag newTag = new XMLTag(current, s.substring(1, tagEnd));
				current.addInnerTag(newTag);
				current = newTag;
				if (root == null)
					root = current;
			}
			if (tagEnd != s.length() - 1) {
				processString(s.substring(tagEnd + 1, s.length()));
			}
		} else {
			current.addContent(s);
		}
		/*
		if (s.charAt(0) == '<' && s.charAt(s.length()-1) == '>') {
			if (s.charAt(1) == '/') {
				current = current.getParent();
			} else {
				XMLTag newTag = new XMLTag(current, s.substring(1, s.length()-1));
				current.addInnerTag(newTag);
				current = newTag;
				if (root == null) {
					root = current;
				}
			}
		} else {
			current.addContent(s);
		}*/
	}
	
	public XMLTag getRoot() {
		//printTag(root, 0);
		return root;
	}
}
