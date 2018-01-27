package com.sm.textanalyzer.gui;

import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import com.sm.textanalyzer.app.Word;

public class WordListModel extends AbstractListModel<Word> implements ListModel<Word> {

	/**
	 * 10.12.17
	 */
	private static final long serialVersionUID = 5116014298632213234L;
	private final List<Word> list;
	
	public WordListModel(List<Word> list) {
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
	
	public void sortName(){
	    Collections.sort(list, (word1, word2) -> word1.getSortingWord().compareTo(word2.getSortingWord()));
	    fireContentsChanged(this, 0, list.size()-1);
	}
	
	public void sortOriginal(){
	    Collections.sort(list, (word1, word2) -> word1.getBeginIndex()-word2.getBeginIndex());
	    fireContentsChanged(this, 0, list.size()-1);
	}
}
