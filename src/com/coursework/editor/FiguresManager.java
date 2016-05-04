package com.coursework.editor;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.coursework.figures.Figure;
import com.coursework.files.PackageLoader;
import com.coursework.main.Main;
import com.coursework.main.Settings;

public class FiguresManager {
	
	private static FiguresManager instance;
	
	private JList<String> figuresList;
	private Set<String> loadedPackages;
	private List<Figure> figures;

	private static final String DEFAULT_PACKAGE_NAME = "data/default.figures";
	
	public static FiguresManager getInstance() {
		if (instance == null)
			instance = new FiguresManager();
		return instance;
	}
		
	public void clearSelection() {
		figuresList.clearSelection();
	}
	
	private FiguresManager() {
		loadedPackages = new HashSet<String>();
		figures = new LinkedList<Figure>();
	}
		
	/*
	 * Connect list from swing view
	 * */
	public void initList(JList<String> figuresList) {
		this.figuresList = figuresList;
		String name = Settings.getInstance().get("defaultPackageName");
		if (name.equals(""))
			name = DEFAULT_PACKAGE_NAME;
		
		addPackage(name);
		
		figuresList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ((((JList<String>)e.getSource()).getSelectedIndex()) != -1) {
					Main.getCurrentScene().selectFigure(figures.get(((JList<String>)e.getSource()).getSelectedIndex()));
				} else {
					Main.getCurrentScene().selectFigure(null);
				}
			}
		});
	}
	
	/*
	 * Load package from specified file
	 * */
	public void addPackage(String fileName) {

		try {
			PackageLoader loader;
			loader = new PackageLoader(fileName);
			String name = loader.getPackageName();
			
			if (loadedPackages.contains(name)) {
				removePackage(name);
			} else {
				loadedPackages.add(name);
			}
		
			figures.addAll(loader.getFigures());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateListView();		
	}
	
	/*
	 * Remove package with specified name
	 * */
	public void removePackage(String packageName) {
		Iterator<Figure> it = figures.iterator();
		
		while (it.hasNext()) {
			Figure f = it.next();
			if (f.getPackageName().equals(packageName)) {
				System.out.println("Remove");
				it.remove();
			}
		}
	}
	
	/*
	 * Update swing list view 
	 * */
	private void updateListView() {
		DefaultListModel<String> model = new DefaultListModel<>();
		
		for (Figure f : figures) {
			model.addElement(f.getName());
		}

		figuresList.setModel(model);
	}
	
	public Figure getFigure(String pack, String name) {		
		for (Figure f : figures) {
			if (f.getPackageName().equals(pack) && f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}
}
