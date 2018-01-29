package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Word;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;

class WordListModel extends AbstractListModel<Word> implements ListModel<Word> {

	private final List<Word> list;
	
	WordListModel(List<Word> list) {
		super();
		this.list = list;
	}
	
	@Override
	public Word getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public int getSize() {
		return list.size();
	}
	
	void sortName(){
	    list.sort(Comparator.comparing(Word::getSortingWord));
	    fireContentsChanged(this, 0, list.size()-1);
	}
	
	void sortOriginal(){
	    list.sort(Comparator.comparingInt(Word::getBeginIndex));
	    fireContentsChanged(this, 0, list.size()-1);
	}
}
