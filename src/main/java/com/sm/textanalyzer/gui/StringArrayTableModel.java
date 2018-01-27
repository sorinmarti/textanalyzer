package com.sm.textanalyzer.gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class StringArrayTableModel extends AbstractTableModel implements TableModel {

	/**
	 * 10.12.17
	 */
	private static final long serialVersionUID = 4800835933125508338L;
	private final String[][] stringArray;
	
	public StringArrayTableModel(String[][] stringArray) {
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
