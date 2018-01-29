package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.MainClass;
import com.sm.textanalyzer.app.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

class FormattedFilePanel extends JPanel {

    private final ResourceBundle resourceBundle = MainClass.getResourceBundle();
	
	private final AnalyzerWindow parent;
	
	private final FormattedFile file;
	private final boolean createStatistics;
	
	private final JTable statisticsTable = new JTable();
    private final JList<Lemma> lemmaList = new JList<>();
    private final JTable lemmaTable = new JTable();
    private final JTable nTypesTable = new JTable();
    private final JList<Word> nTokensList = new JList<>();
    private final JList<Word> tokensList = new JList<>();
    private final JTextArea originalTextArea = new JTextArea();
    private final JTextArea cleanTextArea = new JTextArea();
    private final JTextArea resultTextArea = new JTextArea();

    private WordTypeTableModel lemmaTableModel = null;
	
	FormattedFilePanel(AnalyzerWindow parent, FormattedFile file, boolean createStatistics) {
		this.parent = parent;
		this.file = file;
		this.createStatistics = createStatistics;
		setLayout(new GridLayout(2, 1));
		initializeComponents();
	}


	private void initializeComponents() {
		JPanel typesAndTokensPanel = initializeTypesAndTokensPanel();
		JPanel textAndResultPanel  = initializeTextAndResultPanel();
		
		add(typesAndTokensPanel);
		add(textAndResultPanel);
	}


	private JPanel initializeTypesAndTokensPanel() {
		JPanel panel = new JPanel(new GridLayout(1,0));
		
		LemmaListModel lemmaListModel = new LemmaListModel( TextLibrary.getInstance().getLemmas( file ) );
		WordTypeTableModel nTypesTableModel = new WordTypeTableModel( file.getCleanTypes(), WordTypeTableModel.SORT_OCCURRENCE);
		WordListModel nTokensListModel = new WordListModel( file.getCleanTokens() );
		WordListModel tokensListModel = new WordListModel( file.getTokens() );
		
		ListSelectionListener nTokensListener = new TokensListListener(tokensList, cleanTextArea);
		ListSelectionListener tokensListener = new TokensListListener(nTokensList, originalTextArea);

		JPanel pnlLemmas     = createLemmaPanel(lemmaListModel);
		JPanel pnlNTypes     = createTablePanel(nTypesTable,     nTypesTableModel);
		JPanel pnlNTokens    = createListPanel(resourceBundle.getString("tokens.n"),  nTokensList,     nTokensListModel, nTokensListener);
		JPanel pnlTokens     = createListPanel(resourceBundle.getString("tokens"),    tokensList,      tokensListModel,  tokensListener);
		
		final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addToLemmaLibrary = new JMenuItem(resourceBundle.getString("add.to.lemma.library"));
        addToLemmaLibrary.addActionListener(e -> {
            int modelRow = nTypesTable.convertRowIndexToModel(nTypesTable.getSelectedRow());
            boolean isAlreadyPresent = (boolean)nTypesTableModel.getValueAt(modelRow, 3);

            if(modelRow<0) {
                parent.showMessage(resourceBundle.getString("no.line.selected"));
                return;
            }
            if(isAlreadyPresent) {
                int answer = JOptionPane.showConfirmDialog(parent, resourceBundle.getString("this.type.is.already.in.the.lemma.library.add.anyway"));
                if(answer!=JOptionPane.YES_OPTION) {
                    return;
                }
            }
            String typeToAdd = (String)nTypesTableModel.getValueAt(modelRow, 0);

            JPanel panel1 = new JPanel( new BorderLayout() );
            JComboBox<LemmaLibraryItem> existingLemmas = new JComboBox<>(TextLibrary.getInstance().getLemmaLibraryArray());
            AutoCompletion.enable(existingLemmas);
            panel1.add( existingLemmas, BorderLayout.NORTH );

            JTextField newLemma = new JTextField();
            panel1.add( newLemma, BorderLayout.SOUTH );

            int answer = JOptionPane.showConfirmDialog(parent, panel1, String.format(resourceBundle.getString("add.the.type.s.to.lemma.library"),typeToAdd), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(answer==JOptionPane.OK_OPTION) {
                if(!newLemma.getText().isEmpty()) {
                    LemmaLibraryItem item = new LemmaLibraryItem( newLemma.getText() );
                    item.addVariation( typeToAdd );
                    TextLibrary.getInstance().addLemmaItem( item );

                }
                else {	// Text field is empty, we add to combo box item
                    LemmaLibraryItem selected = (LemmaLibraryItem) existingLemmas.getModel().getSelectedItem();
                    selected.addVariation( typeToAdd );
                }
            }

        });
        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int rowAtPoint = nTypesTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), nTypesTable));
                    if (rowAtPoint > -1) {
                        nTypesTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        popupMenu.add(addToLemmaLibrary);
        nTypesTable.setComponentPopupMenu(popupMenu);
		
		if(createStatistics) {
			TableModel statisticsTableModel = new StringArrayTableModel( TextLibrary.getInstance().getStatisticsArray() );
			JPanel pnlStatistics = createTablePanel(statisticsTable, statisticsTableModel);
			panel.add(pnlStatistics);
		}
		JTabbedPane typeTokenPane = new JTabbedPane();
		//JPanel lemmasPanel = new JPanel(new BorderLayout());
		//lemmasPanel.add(pnlLemmas, BorderLayout.CENTER);
		
		JPanel typesPanel = new JPanel(new BorderLayout());
		typesPanel.add(pnlNTypes, BorderLayout.CENTER);
		//typesPanel.add(pnlTypes);
		JPanel tokensPanel = new JPanel(new GridLayout(1, 2));
		tokensPanel.add(pnlNTokens);
		tokensPanel.add(pnlTokens);
		JPanel optionsPanel = createTokensOptionsPanel(nTokensListModel, tokensListModel);
		tokensPanel.add(optionsPanel);
		
		typeTokenPane.addTab(resourceBundle.getString("lemmas"), pnlLemmas);
		typeTokenPane.addTab(resourceBundle.getString("types"), typesPanel);
		typeTokenPane.addTab(resourceBundle.getString("tokens"), tokensPanel);
		panel.add(typeTokenPane);
		return panel;
	}
	
	
	private JPanel createLemmaPanel(LemmaListModel lemmaListModel) {
		JPanel panel = new JPanel(new GridLayout(1, 2));
		
		lemmaTableModel = new WordTypeTableModel( new ArrayList<>(), WordTypeTableModel.SORT_NAME);
		lemmaTable.setModel(lemmaTableModel);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(lemmaTableModel);
		lemmaTable.setRowSorter(sorter);
		
		JPanel pnlLemmaList = new JPanel(new BorderLayout());
		lemmaList.setModel( lemmaListModel );
		lemmaList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
                DefaultListCellRenderer def = new DefaultListCellRenderer();
                JLabel renderer = (JLabel) def.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setText( value.getName());
                return renderer;
              });
		lemmaList.addListSelectionListener(e -> {
            lemmaTable.clearSelection();
            lemmaTableModel.setValues( lemmaList.getSelectedValue().getTypes() );
        });
		
		JPanel lemmaListOptions = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton btnSortName = new JButton(resourceBundle.getString("by.name"));
		btnSortName.addActionListener(e -> lemmaListModel.sortName());
		JButton btnSortOccurrences = new JButton(resourceBundle.getString("by.occurrences"));
		btnSortOccurrences.addActionListener(e -> lemmaListModel.sortOccurrences());
		lemmaListOptions.add( btnSortName);
		lemmaListOptions.add( btnSortOccurrences);
		
		pnlLemmaList.add(new JScrollPane(lemmaList), BorderLayout.CENTER);
		pnlLemmaList.add(lemmaListOptions, BorderLayout.SOUTH);
		
		JPanel pnlLemmaTypesTable = new JPanel(new BorderLayout());
		pnlLemmaTypesTable.add(new JScrollPane(lemmaTable), BorderLayout.CENTER);
		
		JPanel options = new JPanel(new FlowLayout());
		JButton btn = new JButton(resourceBundle.getString("occurrences"));
		btn.addActionListener(e -> {
            StringBuilder foundString = new StringBuilder();
            WordType word = lemmaTableModel.getWordTypeAt( sorter.convertRowIndexToModel(lemmaTable.getSelectedRow()) );
            for(Occurrence o : word.getOccurrences()) {
                FormattedFile file = TextLibrary.getInstance().getFile(o.getFile());
                String s = file.getFilename()+":"+ file.getContext(o.getToken(), 5, 5);
                foundString.append(s).append("\n");
            }
            resultTextArea.setText(foundString.toString());
        });
		options.add(btn);
		pnlLemmaTypesTable.add(options, BorderLayout.SOUTH);
		
		panel.add(pnlLemmaList);
		panel.add(pnlLemmaTypesTable);
		return panel;
	}


	private JPanel createTokensOptionsPanel(WordListModel nTokensListModel, WordListModel tokensListModel) {
		JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		optionsPanel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("options")));
		
		JButton btnSort1 = new JButton(resourceBundle.getString("original.order"));
		btnSort1.addActionListener(e -> {
            nTokensListModel.sortOriginal();
            tokensListModel.sortOriginal();
        });
		JButton btnSort2 = new JButton(resourceBundle.getString("alphabetical"));
		btnSort2.addActionListener(e -> {
            nTokensListModel.sortName();
            tokensListModel.sortName();
        });
		
		optionsPanel.add(btnSort1);
		optionsPanel.add(btnSort2);
		return optionsPanel;
	}


	private JPanel initializeTextAndResultPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 0));
		
		JPanel pnlOriginalText = createTextAreaPanel(resourceBundle.getString("original.source"), originalTextArea, file.getText() );
		JPanel pnlCleanText    = createTextAreaPanel(resourceBundle.getString("normalized.source"), cleanTextArea, file.getCleanText() );
		JPanel pnlSearchResult = createTextAreaPanel(resourceBundle.getString("search.results"), resultTextArea, "");
		
		panel.add(pnlOriginalText);
		panel.add(pnlCleanText);
		panel.add(pnlSearchResult);
		
		return panel;
	}

	private JPanel createTextAreaPanel(String title, JTextArea area, String text) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(title));
		
		area.setText(text);
		panel.add(new JScrollPane( area ));
		return panel;
	}


	private JPanel createListPanel(String title, JList<Word> list, WordListModel listModel, ListSelectionListener listener) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(title));
		
		FilteredListModel filteredListModel = new FilteredListModel( listModel );
		list.setModel(filteredListModel);
		
		JTextField txtSearch = new JTextField(10);
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
		    
			private void performSearch() {
				String filter = txtSearch.getText();
				filteredListModel.setFilter(element -> element.getWord().contains(filter));
		    }
		    
			@Override
			public void insertUpdate(DocumentEvent e) {
				performSearch();
		    }
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				performSearch();
		    }
		      
			@Override
			public void changedUpdate(DocumentEvent e) {}
		    
		});
		
		list.setCellRenderer((list1, value, index, isSelected, cellHasFocus) -> {
                DefaultListCellRenderer def = new DefaultListCellRenderer();
                JLabel renderer = (JLabel) def.getListCellRendererComponent(list1, value, index, isSelected, cellHasFocus);
                renderer.setText( value.getWord());
                return renderer;
              });
		list.addListSelectionListener(listener);
		
		panel.add(new JScrollPane( list ), BorderLayout.CENTER);
		panel.add(txtSearch, BorderLayout.SOUTH);
		return panel;
	}


	private JPanel createTablePanel(JTable table, WordTypeTableModel tableModel) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("types")));
		
		table.setModel(tableModel);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
		table.setRowSorter(sorter);
		
		JTextField txtSearch = new JTextField(10);
		JCheckBox chkOnlyNonLemmas = new JCheckBox(resourceBundle.getString("only.entries.without.lemma"));
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
		    
			private void performSearch() {
		    	String text = txtSearch.getText();
		        if (text.trim().length() == 0) {
		        	sorter.setRowFilter(null);
		        } else {
		        	sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));  // NON-NLS
		        }
		        table.repaint();
		    }
		    
			@Override
			public void insertUpdate(DocumentEvent e) {
				performSearch();
		    }
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				performSearch();
		    }
		      
			@Override
			public void changedUpdate(DocumentEvent e) {}
		    
		});
		    
		panel.add(new JScrollPane( table ), BorderLayout.CENTER);
		
		JPanel options = new JPanel(new FlowLayout());
		JButton btn = new JButton(resourceBundle.getString("occurrences"));
		JTextField tokensBefore = new JTextField("3",5);
		JTextField tokensAfter = new JTextField("3", 5);
		btn.addActionListener(e -> {
            StringBuilder foundString = new StringBuilder();
            WordType word = tableModel.getWordTypeAt( sorter.convertRowIndexToModel(table.getSelectedRow()) );
            for(Occurrence o : word.getOccurrences()) {
                FormattedFile file = TextLibrary.getInstance().getFile(o.getFile());
                String s = file.getFilename()+":"+ file.getContext(o.getToken(), Integer.valueOf(tokensBefore.getText()), Integer.valueOf(tokensAfter.getText()));
                foundString.append(s).append("\n");
            }
            resultTextArea.setText(foundString.toString());
        });
		options.add(btn);
		
		options.add(new JLabel(resourceBundle.getString("search")));
		options.add(txtSearch);
		options.add(new JLabel("     "));
		options.add(btn);
		options.add(new JLabel(resourceBundle.getString("before")));
		options.add(tokensBefore);
		options.add(new JLabel(resourceBundle.getString("after")));
		options.add(tokensAfter);
		options.add(chkOnlyNonLemmas);
		panel.add(options, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel createTablePanel(JTable table, TableModel tableModel) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("statistics")));
		
		table.setModel(tableModel);
		panel.add(new JScrollPane( table ), BorderLayout.CENTER);
		return panel;
	}
}
