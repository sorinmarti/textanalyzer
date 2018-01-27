package com.sm.textanalyzer.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.sm.textanalyzer.app.WordType;

public class WordTypeTableModel extends AbstractTableModel implements TableModel {

	/**
	 * 10.12.17
	 */
	private static final long serialVersionUID = 7717634321321384518L;
	public static final int SORT_NAME = 0;
	public static final int SORT_OCCURENCE = 1;
	
	private List<WordType> list;
	private int sortingType;
	
	public WordTypeTableModel(List<WordType> list, int initialSort) {
		super();
		
		this.sortingType = initialSort;
		switch(initialSort) {
		case SORT_OCCURENCE:
			list = sortByOccurence( list );
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
		Collections.sort(list, (o1, o2) -> {
            int res = o1.getWord().compareTo(o2.getWord());
            return res;
        });
		return list;
	}
	
	private static List<WordType> sortByOccurence(List<WordType> list) {
		Collections.sort(list, (o1, o2) -> {
            int res = o2.getNumberOfOccurences()-o1.getNumberOfOccurences();
            if(res==0) {
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
			return "# gefunden";
		case 2:
			return "in # Dateien";
		case 3:
			return "in Lemma-Lib?";
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
			return list.get(rowIndex).getNumberOfOccurences();
		case 2:
			return list.get(rowIndex).getNumberOfDifferentFiles();
		case 3:
			return list.get(rowIndex).isGrouped();
		}
		return "";
	}

	public WordType getWordTypeAt(int selectedRow) {
		return list.get(selectedRow);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}

	public void setValues(List<WordType> types) {
		switch(sortingType) {
		case SORT_OCCURENCE:
			this.list = sortByOccurence( types );
			break;
		default:
			this.list = sortByName( types );
		}
		fireTableDataChanged();
		
	}
}
