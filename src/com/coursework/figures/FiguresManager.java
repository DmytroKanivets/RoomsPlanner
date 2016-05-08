package com.coursework.figures;

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

import com.coursework.main.Main;
import com.coursework.files.FiguresLoader;
import com.coursework.rules.RulesManager;

public class FiguresManager {
	
	private static FiguresManager instance;
	
	private JList<String> figuresViewList;
	
	private Set<String> loadedPackages;
	private List<Figure> figures;

	public static FiguresManager getInstance() {
		if (instance == null)
			instance = new FiguresManager();
		return instance;
	}
	
	private FiguresManager() {
		loadedPackages = new HashSet<String>();
		figures = new LinkedList<Figure>();
	}
		
	public void clearSelection() {
		figuresViewList.clearSelection();
	}
		
	/*
	 * Connect list from swing view
	 */
	public void connectList(JList<String> figuresList) {
		this.figuresViewList = figuresList;

		figuresList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//TODO move to commands
				int selectedIndex = (((JList<String>)e.getSource()).getSelectedIndex());
				if (selectedIndex != -1) {
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
	public void addPackage(String fileName) throws FileNotFoundException {

		FiguresLoader loader;
		loader = new FiguresLoader(fileName);
		String name = loader.getPackageName();
		
		if (loadedPackages.contains(name)) {
			removePackage(name);
		} else {
			loadedPackages.add(name);
		}
	
		figures.addAll(loader.getFigures());
		
		RulesManager.getInstance().loadRules(fileName);
		
		updateListView();		
	}
	
	/*
	 * Remove package with specified name
	 */
	public void removePackage(String packageName) {
		Iterator<Figure> it = figures.iterator();
		
		while (it.hasNext()) {
			Figure f = it.next();
			if (f.getPackageName().equals(packageName)) {
				it.remove();
			}
		}
	}

	/*
	 * Update swing list view 
	 */
	private void updateListView() {
		DefaultListModel<String> model = new DefaultListModel<>();
		
		for (Figure f : figures) {
			model.addElement(f.getName());
		}

		figuresViewList.setModel(model);
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
