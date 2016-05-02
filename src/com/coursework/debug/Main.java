package com.coursework.debug;

import java.io.FileNotFoundException;
import java.util.Collection;

import com.coursework.files.XMLReader;
import com.coursework.files.XMLTag;

public class Main {

	public static void printTag(XMLTag t, int depth) {
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
	
	public static void main(String[] args) {
		try {
			XMLReader r = new XMLReader("data/debug.xml");
			XMLTag t = r.getRoot();
			printTag(t, 0);
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
