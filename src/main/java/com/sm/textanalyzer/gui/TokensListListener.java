package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Word;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

class TokensListListener implements ListSelectionListener {

	private final JList<Word> twinList;
	private final JTextArea textAreaToHighlight;
	
	TokensListListener(JList<Word> twinList, JTextArea textAreaToHighlight) {
		this.twinList = twinList;
		this.textAreaToHighlight = textAreaToHighlight;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		@SuppressWarnings("unchecked")
		JList<Word> list = (JList<Word>)e.getSource();
		int selectedIndex =  list.getSelectedIndex();
		twinList.setSelectedIndex(selectedIndex);
		twinList.ensureIndexIsVisible(selectedIndex);
		
		Highlighter h = textAreaToHighlight.getHighlighter();
		Word word = list.getSelectedValue();
		h.removeAllHighlights();
		try {
			//System.out.println("Highlight: "+word.getBeginIndex()+"/"+ (word.getBeginIndex()+word.getWord().length()));
			h.addHighlight(word.getBeginIndex(), word.getBeginIndex()+word.getWord().length(), DefaultHighlighter.DefaultPainter);
			 Rectangle viewRect = textAreaToHighlight.modelToView(word.getBeginIndex());
			 textAreaToHighlight.scrollRectToVisible(viewRect);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

}
