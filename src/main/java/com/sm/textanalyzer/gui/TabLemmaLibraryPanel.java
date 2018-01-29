package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.LemmaLibraryItem;
import com.sm.textanalyzer.app.TextLibrary;

import javax.swing.*;
import java.awt.*;

class TabLemmaLibraryPanel extends JPanel {

	TabLemmaLibraryPanel() {
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
            DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();
            JLabel component = (JLabel)defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            component.setText( value.getName() );
            return component;
        });
		JPanel pnlLemmas = new JPanel( new BorderLayout() );
		pnlLemmas.add( new JScrollPane(lemmaList) , BorderLayout.CENTER);
		
		JPanel pnlLemmaOptions = new JPanel( new FlowLayout(FlowLayout.CENTER));
		JButton btnNewLemma = new JButton("New Lemma");
		btnNewLemma.addActionListener(e -> {
            String answer = JOptionPane.showInputDialog(TabLemmaLibraryPanel.this, "Lemma Name:");
            if(answer!=null) {
                LemmaLibraryItem lItem = new LemmaLibraryItem(answer);
                TextLibrary.getInstance().addLemmaItem( lItem );
                lemmaList.setListData( TextLibrary.getInstance().getLemmaLibraryArray() );
                lemmaList.setSelectedValue(lItem, true);
            }
        });
		pnlLemmaOptions.add(btnNewLemma);
		
		JButton btnReloadLemma = new JButton("Reload");
		btnReloadLemma.addActionListener(e -> lemmaList.setListData( TextLibrary.getInstance().getLemmaLibraryArray() ));
		pnlLemmaOptions.add(btnReloadLemma);
		pnlLemmas.add( pnlLemmaOptions , BorderLayout.SOUTH);
		
		JPanel pnlVariations = new JPanel( new BorderLayout() );
		pnlVariations.add( new JScrollPane( variationsList ), BorderLayout.CENTER);
		
		JPanel pnlVariationOptions = new JPanel( new FlowLayout(FlowLayout.CENTER));
		JButton btnNewVariation = new JButton("New Variation");
		btnNewVariation.addActionListener(e -> {
            String answer = JOptionPane.showInputDialog(TabLemmaLibraryPanel.this, "New Variation:");
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
		JButton btnSave = new JButton("Save Lemma Library");
		btnSave.addActionListener(e -> {
            //TODO TextLibrary.getInstance().writeLemmaList();
        });
		JButton btnLoad = new JButton("Load Lemma Library");
		btnLoad.addActionListener(e -> {
            //TODO TextLibrary.getInstance().readLemmaList();
        });
		pnlSaveOrLoad.add(btnSave);
		pnlSaveOrLoad.add(btnLoad);
		add( pnlSaveOrLoad , BorderLayout.NORTH);
	}
}
