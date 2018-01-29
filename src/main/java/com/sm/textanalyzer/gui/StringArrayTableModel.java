package com.sm.textanalyzer.gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

class StringArrayTableModel extends AbstractTableModel implements TableModel {

	private final String[][] stringArray;
	
	StringArrayTableModel(String[][] stringArray) {
		this.stringArray = stringArray;
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return stringArray.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return stringArray[rowIndex][columnIndex];
	}

}
