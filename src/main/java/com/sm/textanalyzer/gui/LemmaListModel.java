package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Lemma;
import com.sm.textanalyzer.app.Type;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;

class LemmaListModel extends AbstractListModel<Lemma> implements ListModel<Lemma> {

	private final List<Lemma> list;
	
	LemmaListModel(List<Lemma> list) {
		super();
		this.list = list;
	}
	
	@Override
	public Lemma getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	void sortName(){
	    list.sort(Comparator.comparing(Lemma::getName));
	    fireContentsChanged(this, 0, list.size());
	}
	
	void sortOccurrences(){
		list.sort((o1, o2) -> {
			Integer o1Files = 0;
			for (Type o1Type : o1.getTypes()) {
				o1Files += o1Type.getNumberOfDifferentFiles();
			}
			Integer o2Files = 0;
			for (Type o2Type : o2.getTypes()) {
				o2Files += o2Type.getNumberOfDifferentFiles();
			}
			return o2Files.compareTo(o1Files);
		});
	    fireContentsChanged(this, 0, list.size());
	}
}
