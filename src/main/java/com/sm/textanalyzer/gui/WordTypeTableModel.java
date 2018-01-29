package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.WordType;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.Comparator;
import java.util.List;

class WordTypeTableModel extends AbstractTableModel implements TableModel {

	static final int SORT_NAME = 0;
	static final int SORT_OCCURRENCE = 1;
	
	private List<WordType> list;
	private final int sortingType;
	
	WordTypeTableModel(List<WordType> list, int initialSort) {
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
		List<WordType> strippedList = new ArrayList<>();
		for(WordType wt : list) {
			if(wt.getNumberOfDifferentFiles()>1) {
				strippedList.add(wt);
			}
		}
		this.list = strippedList;
		*/
		
	}
	
	private static List<WordType> sortByName(List<WordType> list) {
		list.sort(Comparator.comparing(WordType::getWord));
		return list;
	}
	
	private static List<WordType> sortByOccurrence(List<WordType> list) {
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
			return "Type";
		case 1:
			return "# found";
		case 2:
			return "in # files";
		case 3:
			return "in Lemma lib?";
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

	WordType getWordTypeAt(int selectedRow) {
		return list.get(selectedRow);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}

	void setValues(List<WordType> types) {
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
