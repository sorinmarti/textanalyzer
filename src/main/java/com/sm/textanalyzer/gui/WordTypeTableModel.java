package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.MainClass;
import com.sm.textanalyzer.app.Type;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

class WordTypeTableModel extends AbstractTableModel implements TableModel {

	static final int SORT_NAME = 0;
	static final int SORT_OCCURRENCE = 1;

	private final ResourceBundle resourceBundle = MainClass.getResourceBundle();

	private List<Type> list;
	private final int sortingType;
	
	WordTypeTableModel(List<Type> list, int initialSort) {
		super();
		
		this.sortingType = initialSort;
		switch(initialSort) {
		case SORT_OCCURRENCE:
			list = sortByOccurrence( list );
			break;
		default:
			list = sortByName( list );
		}
		this.list = list;
		
		/*
		List<Type> strippedList = new ArrayList<>();
		for(Type wt : list) {
			if(wt.getNumberOfDifferentFiles()>1) {
				strippedList.add(wt);
			}
		}
		this.list = strippedList;
		*/
		
	}
	
	private static List<Type> sortByName(List<Type> list) {
		list.sort(Comparator.comparing(Type::getWord));
		return list;
	}
	
	private static List<Type> sortByOccurrence(List<Type> list) {
		list.sort((o1, o2) -> {
			int res = o2.getNumberOfOccurrences() - o1.getNumberOfOccurrences();
			if (res == 0) {
				res = o1.getWord().compareTo(o2.getWord());
			}
			return res;
		});
		return list;
	}
	
	@Override
	public String getColumnName(int column) {
		switch(column) {
		case 0:
			return resourceBundle.getString("type");
		case 1:
			return resourceBundle.getString("found");
		case 2:
			return resourceBundle.getString("in.files");
		case 3:
			return resourceBundle.getString("in.lemma.lib");
		default:
			return super.getColumnName(column);
		}
	}

	
	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		switch(colIndex) {
		case 0:
			return list.get(rowIndex).getWord();
		case 1:
			return list.get(rowIndex).getNumberOfOccurrences();
		case 2:
			return list.get(rowIndex).getNumberOfDifferentFiles();
		case 3:
			return list.get(rowIndex).isGrouped();
		}
		return "";
	}

	Type getWordTypeAt(int selectedRow) {
		return list.get(selectedRow);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}

	void setValues(List<Type> types) {
		switch(sortingType) {
		case SORT_OCCURRENCE:
			this.list = sortByOccurrence( types );
			break;
		default:
			this.list = sortByName( types );
		}
		fireTableDataChanged();
		
	}
}
