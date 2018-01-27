package com.sm.textanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sm.textanalyzer.app.LemmaLibraryItem;
import com.sm.textanalyzer.app.TextLibrary;

public class TabLemmaLibraryPanel extends JPanel {

	/**
	 * 10.12.17
	 */
	private static final long serialVersionUID = -6979780734538507182L;

	public TabLemmaLibraryPanel() {
		setLayout(new BorderLayout());
		initializeComponents();
	}
	
	private void initializeComponents() {
		JList<String> variationsList = new JList<>();
		JList<LemmaLibraryItem> lemmaList = new JList<>( TextLibrary.getInstance().getLemmaLibraryArray() );
		lemmaList.addListSelectionListener(e -> {
            if (lemmaList.getSelectedIndex() != -1) {
                variationsList.setListData( lemmaList.getSelectedValue().getVariations().toArray(new String[lemmaList.getSelectedValue().getVariations().size()]));
            }
        });
		lemmaList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
            JLabel component = (JLabel)dlcr.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            component.setText( value.getName() );
            return component;
        });
		JPanel pnlLemmas = new JPanel( new BorderLayout() );
		pnlLemmas.add( new JScrollPane(lemmaList) , BorderLayout.CENTER);
		
		JPanel pnlLemmaOptions = new JPanel( new FlowLayout(FlowLayout.CENTER));
		JButton btnNewLemma = new JButton("Neues Lemma");
		btnNewLemma.addActionListener(e -> {
            String answer = JOptionPane.showInputDialog(TabLemmaLibraryPanel.this, "Lemma-Name:");
            if(answer!=null) {
                LemmaLibraryItem lItem = new LemmaLibraryItem(answer);
                TextLibrary.getInstance().addLemmaItem( lItem );
                lemmaList.setListData( TextLibrary.getInstance().getLemmaLibraryArray() );
                lemmaList.setSelectedValue(lItem, true);
            }
        });
		pnlLemmaOptions.add(btnNewLemma);
		
		JButton btnReloadLemma = new JButton("Neu Laden");
		btnReloadLemma.addActionListener(e -> lemmaList.setListData( TextLibrary.getInstance().getLemmaLibraryArray() ));
		pnlLemmaOptions.add(btnReloadLemma);
		pnlLemmas.add( pnlLemmaOptions , BorderLayout.SOUTH);
		
		JPanel pnlVariations = new JPanel( new BorderLayout() );
		pnlVariations.add( new JScrollPane( variationsList ), BorderLayout.CENTER);
		
		JPanel pnlVariationOptions = new JPanel( new FlowLayout(FlowLayout.CENTER));
		JButton btnNewVariation = new JButton("Neue Variante");
		btnNewVariation.addActionListener(e -> {
            String answer = JOptionPane.showInputDialog(TabLemmaLibraryPanel.this, "Neue Variation:");
            if(answer!=null) {
                LemmaLibraryItem lItem = lemmaList.getSelectedValue();
                lItem.addVariation( answer );
                lemmaList.clearSelection();
                lemmaList.setSelectedValue(lItem, true);
            }
        });
		pnlVariationOptions.add(btnNewVariation);
		pnlVariations.add( pnlVariationOptions , BorderLayout.SOUTH);
		
		JSplitPane splitter = new JSplitPane();
		splitter.setLeftComponent( pnlLemmas );
		splitter.setRightComponent( pnlVariations );
		add( splitter , BorderLayout.CENTER);
		
		JPanel pnlSaveOrLoad = new JPanel( new FlowLayout(FlowLayout.RIGHT));
		JButton btnSave = new JButton("Lemma-Bibliothek Speichern");
		btnSave.addActionListener(e -> {
            //TODO TextLibrary.getInstance().writeLemmaList();
        });
		JButton btnLoad = new JButton("Lemma-Bibliothek Laden");
		btnLoad.addActionListener(e -> {
            //TODO TextLibrary.getInstance().readLemmaList();
        });
		pnlSaveOrLoad.add(btnSave);
		pnlSaveOrLoad.add(btnLoad);
		add( pnlSaveOrLoad , BorderLayout.NORTH);
	}
}
