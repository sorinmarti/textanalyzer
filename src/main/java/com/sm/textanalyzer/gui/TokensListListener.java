package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Token;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

class TokensListListener implements ListSelectionListener {

	private final JList<Token> twinList;
	private final JTextArea textAreaToHighlight;
	
	TokensListListener(JList<Token> twinList, JTextArea textAreaToHighlight) {
		this.twinList = twinList;
		this.textAreaToHighlight = textAreaToHighlight;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		@SuppressWarnings("unchecked")
		JList<Token> list = (JList<Token>)e.getSource();
		int selectedIndex =  list.getSelectedIndex();
		twinList.setSelectedIndex(selectedIndex);
		twinList.ensureIndexIsVisible(selectedIndex);
		
		Highlighter h = textAreaToHighlight.getHighlighter();
		Token token = list.getSelectedValue();
		h.removeAllHighlights();
		try {
			//System.out.println("Highlight: "+token.getBeginIndex()+"/"+ (token.getBeginIndex()+token.getWord().length()));
			h.addHighlight(token.getBeginIndex(), token.getBeginIndex()+ token.getWord().length(), DefaultHighlighter.DefaultPainter);
			 Rectangle viewRect = textAreaToHighlight.modelToView(token.getBeginIndex());
			 textAreaToHighlight.scrollRectToVisible(viewRect);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

}
