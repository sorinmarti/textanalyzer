package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Token;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;

class WordListModel extends AbstractListModel<Token> implements ListModel<Token> {

	private final List<Token> list;
	
	WordListModel(List<Token> list) {
		super();
		this.list = list;
	}
	
	@Override
	public Token getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public int getSize() {
		return list.size();
	}
	
	void sortName(){
	    list.sort(Comparator.comparing(Token::getSortingWord));
	    fireContentsChanged(this, 0, list.size()-1);
	}
	
	void sortOriginal(){
	    list.sort(Comparator.comparingInt(Token::getBeginIndex));
	    fireContentsChanged(this, 0, list.size()-1);
	}
}
