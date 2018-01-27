package com.sm.textanalyzer.gui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import com.sm.textanalyzer.app.Lemma;
import com.sm.textanalyzer.app.WordType;

public class LemmaListModel extends AbstractListModel<Lemma> implements ListModel<Lemma> {

	/**
	 * 10.12.17
	 */
	private static final long serialVersionUID = 6004745862483604816L;
	private final List<Lemma> list;
	
	public LemmaListModel(List<Lemma> list) {
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

	public void sortName(){
	    Collections.sort(list, (word1, word2) -> word1.getName().compareTo(word2.getName()));
	    fireContentsChanged(this, 0, list.size());
	}
	
	public void sortOccurences(){
		Collections.sort(list, (o1, o2) -> {
            Integer o1Files = 0;
            for(WordType o1Type : o1.getTypes()) {
                o1Files += o1Type.getNumberOfDifferentFiles();
            }
            Integer o2Files = 0;
            for(WordType o2Type : o2.getTypes()) {
                o2Files += o2Type.getNumberOfDifferentFiles();
            }
            return o2Files.compareTo(o1Files);
        });
	    fireContentsChanged(this, 0, list.size());
	}
}
