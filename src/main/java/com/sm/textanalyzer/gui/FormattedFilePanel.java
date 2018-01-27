package com.sm.textanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.sm.textanalyzer.app.FormattedFile;
import com.sm.textanalyzer.app.Lemma;
import com.sm.textanalyzer.app.LemmaLibraryItem;
import com.sm.textanalyzer.app.Occurence;
import com.sm.textanalyzer.app.TextLibrary;
import com.sm.textanalyzer.app.Word;
import com.sm.textanalyzer.app.WordType;

public class FormattedFilePanel extends JPanel {

	RowFilter<Object, Object> lemmaFilter = new RowFilter<Object, Object>() {
	      public boolean include(Entry entry) {
	    	  if(entry.getValueCount()>=4) {
	    		  if((boolean)entry.getValue(3)) {
	    			  return false;
	    		  }
	    	  }
	       return true;
	      }
	    };
	    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final AnalyzerWindow parent;
	
	private final FormattedFile file;
	private boolean createStatistics;
	
	JTable statisticsTable = new JTable();
	JList<Lemma> lemmaList = new JList<>();
	JTable lemmaTable = new JTable();
	JTable nTypesTable = new JTable();
	JTable typesTable = new JTable();
	JList<Word> nTokensList = new JList<>();
	JList<Word> tokensList = new JList<>();
	JTextArea originalTextArea = new JTextArea();
	JTextArea cleanTextArea = new JTextArea();
	JTextArea resultTextArea = new JTextArea();
	
	WordTypeTableModel lemmaTableModel = null;
	
	int lastSortCommand = 0;
	
	
	public FormattedFilePanel(AnalyzerWindow parent, FormattedFile file, boolean createStatistics) {
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
		//TableModel lemmaTableModel = new DefaultTableModel();
		WordTypeTableModel nTypesTableModel = new WordTypeTableModel( file.getCleanTypes(), WordTypeTableModel.SORT_OCCURENCE ); 
		//WordTypeTableModel typesTableModel = new WordTypeTableModel( file.getTypes() );
		WordListModel nTokensListModel = new WordListModel( file.getCleanTokens() );
		WordListModel tokensListModel = new WordListModel( file.getTokens() );
		
		ListSelectionListener nTokensListener = new TokensListListener(tokensList, cleanTextArea);
		ListSelectionListener tokensListener = new TokensListListener(nTokensList, originalTextArea);
		
		
		JPanel pnlLemmas     = createLemmaPanel(lemmaListModel);
		JPanel pnlNTypes     = createTablePanel("N-Types",   nTypesTable,     nTypesTableModel);
		//JPanel pnlTypes      = createTablePanel("Types",     typesTable,      typesTableModel);
		JPanel pnlNTokens    = createListPanel( "N-Tokens",  nTokensList,     nTokensListModel, nTokensListener);
		JPanel pnlTokens     = createListPanel( "Tokens",    tokensList,      tokensListModel,  tokensListener);
		
		final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addToLemmaLibrary = new JMenuItem("Zu Lemma-Bibliothek hinzuf�gen");
        addToLemmaLibrary.addActionListener(e -> {
            int modelRow = nTypesTable.convertRowIndexToModel(nTypesTable.getSelectedRow());
            boolean isAlreadyPresent = (boolean)nTypesTableModel.getValueAt(modelRow, 3);

            if(modelRow<0) {
                parent.showMessage("Keine Zeile ausgew�hlt");
                return;
            }
            if(isAlreadyPresent) {
                int answer = JOptionPane.showConfirmDialog(parent, "Dieser Type ist bereits in der Lemma-Bibliothek. Trotzdem hinzuf�gen?");
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

            int answer = JOptionPane.showConfirmDialog(parent, panel1, "F�gen Sie den Type '"+typeToAdd+"' zur Lemma-Bibliothek hinzu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(answer==JOptionPane.OK_OPTION) {
                if(!newLemma.getText().isEmpty()) {
                    LemmaLibraryItem item = new LemmaLibraryItem( newLemma.getText() );
                    item.addVariation( typeToAdd );
                    TextLibrary.getInstance().addLemmaItem( item );

                }
                else {	// Textfield is empty, we add to combo box item
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
			JPanel pnlStatistics = createTablePanel("Statistik", statisticsTable, statisticsTableModel);
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
		
		typeTokenPane.addTab("Lemmas", pnlLemmas);
		typeTokenPane.addTab("Types", typesPanel);
		typeTokenPane.addTab("Tokens", tokensPanel);
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
                renderer.setText( ((Lemma)value).getName());
                return renderer;
              });
		lemmaList.addListSelectionListener(e -> {
            lemmaTable.clearSelection();
            lemmaTableModel.setValues( lemmaList.getSelectedValue().getTypes() );
        });
		
		JPanel lemmaListOptions = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton btnSortName = new JButton("Name");
		btnSortName.addActionListener(e -> lemmaListModel.sortName());
		JButton btnSortOccurences = new JButton("Vorkommnisse");
		btnSortOccurences.addActionListener(e -> lemmaListModel.sortOccurences());
		lemmaListOptions.add( btnSortName);
		lemmaListOptions.add( btnSortOccurences);
		
		pnlLemmaList.add(new JScrollPane(lemmaList), BorderLayout.CENTER);
		pnlLemmaList.add(lemmaListOptions, BorderLayout.SOUTH);
		
		JPanel pnlLemmaTypesTable = new JPanel(new BorderLayout());
		pnlLemmaTypesTable.add(new JScrollPane(lemmaTable), BorderLayout.CENTER);
		
		JPanel options = new JPanel(new FlowLayout());
		JButton btn = new JButton( "Nachweise" );
		btn.addActionListener(e -> {
            String foundString = "";
            WordType word = lemmaTableModel.getWordTypeAt( sorter.convertRowIndexToModel(lemmaTable.getSelectedRow()) );
            for(Occurence o : word.getOccurences()) {
                FormattedFile file = TextLibrary.getInstance().getFile(o.getFile());
                String s = file.getFilename().toString()+":"+ file.getContext(o.getToken(), 5, 5);
                foundString += s + "\n";
            }
            resultTextArea.setText(foundString);
        });
		options.add(btn);
		pnlLemmaTypesTable.add(options, BorderLayout.SOUTH);
		
		panel.add(pnlLemmaList);
		panel.add(pnlLemmaTypesTable);
		return panel;
	}


	private JPanel createTokensOptionsPanel(WordListModel nTokensListModel, WordListModel tokensListModel) {
		JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Optionen"));
		
		JButton btnSort1 = new JButton("Originalreihenf.");
		btnSort1.addActionListener(e -> {
            nTokensListModel.sortOriginal();
            tokensListModel.sortOriginal();
        });
		JButton btnSort2 = new JButton("Alphabetisch");
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
		
		JPanel pnlOriginalText = createTextAreaPanel("Originaltext", originalTextArea, file.getText() );
		JPanel pnlCleanText    = createTextAreaPanel("Normalisierter Text", cleanTextArea, file.getCleanText() );
		JPanel pnlSearchResult = createTextAreaPanel("Suchresultate", resultTextArea, "");
		
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
				filteredListModel.setFilter(element -> {
					return ((Word) element).getWord().contains(filter);
				});
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


	private JPanel createTablePanel(String title, JTable table, WordTypeTableModel tableModel) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(title));
		
		table.setModel(tableModel);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
		table.setRowSorter(sorter);
		
		JTextField txtSearch = new JTextField(10);
		JCheckBox chkOnlyNonLemmas = new JCheckBox("Nur Eintr�ge ohne Lemma");
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
		    
			private void performSearch() {
		    	String text = txtSearch.getText();
		        if (text.trim().length() == 0) {
		        	sorter.setRowFilter(null);
		        } else {
		        	RowFilter f = RowFilter.regexFilter("(?i)" + text);
		        	List<RowFilter<Object,Object>> ls = new ArrayList<>();
		        	ls.add( f );
		        	ls.add(lemmaFilter);
		        	sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
		        	//sorter.setRowFilter( RowFilter.andFilter( ls ) );
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
		JButton btn = new JButton( "Nachweise" );
		JTextField tokensBefore = new JTextField("3",5);
		JTextField tokensAfter = new JTextField("3", 5);
		btn.addActionListener(e -> {
            String foundString = "";
            WordType word = tableModel.getWordTypeAt( sorter.convertRowIndexToModel(table.getSelectedRow()) );
            for(Occurence o : word.getOccurences()) {
                FormattedFile file = TextLibrary.getInstance().getFile(o.getFile());
                String s = file.getFilename().toString()+":"+ file.getContext(o.getToken(), Integer.valueOf(tokensBefore.getText()), Integer.valueOf(tokensAfter.getText()));
                foundString += s + "\n";
            }
            resultTextArea.setText(foundString);
        });
		options.add(btn);
		
		options.add(new JLabel("Suche"));
		options.add(txtSearch);
		options.add(new JLabel("     "));
		options.add(btn);
		options.add(new JLabel("Vor:"));
		options.add(tokensBefore);
		options.add(new JLabel("Nach:"));
		options.add(tokensAfter);
		options.add(chkOnlyNonLemmas);
		panel.add(options, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel createTablePanel(String title, JTable table, TableModel tableModel) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(title));
		
		table.setModel(tableModel);
		panel.add(new JScrollPane( table ), BorderLayout.CENTER);
		return panel;
	}
}
