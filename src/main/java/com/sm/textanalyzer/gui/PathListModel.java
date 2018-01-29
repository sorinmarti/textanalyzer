package com.sm.textanalyzer.gui;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

class PathListModel extends AbstractListModel<Path> implements ListModel<Path> {

	private final List<Path> list;
	
	PathListModel(List<Path> list) {
		super();
		this.list = list;
	}
	
	@Override
	public Path getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	/*
	public void addPath(Path path) {
		this.list.add(path);
		fireIntervalAdded(this, list.indexOf(path)-1, list.indexOf(path));
	}
	*/
	
	void removePath(Path path) {
		int idx = list.indexOf( path );
		this.list.remove(path);
		fireIntervalRemoved(this, idx, idx);
	}
}
