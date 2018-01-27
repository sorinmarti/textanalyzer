package com.sm.textanalyzer.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sm.textanalyzer.app.FormattedFile;
import com.sm.textanalyzer.app.TextLibrary;

public class TabSingleCorpusFilesPanel extends JSplitPane {
	
	private final AnalyzerWindow parent;
	
	public TabSingleCorpusFilesPanel(AnalyzerWindow parent) {
		this.parent = parent;
		initialize();
	}
	
	private void initialize() {
		JList<FormattedFile> list_1 = new JList<>();
		list_1.setModel(new ListModel<FormattedFile>() {

			@Override
			public FormattedFile getElementAt(int arg0) {
				return TextLibrary.getInstance().getFile( arg0 );
			}

			@Override
			public int getSize() {
				return TextLibrary.getInstance().getNumFiles();
			}

			@Override
			public void addListDataListener(ListDataListener arg0) {}
			
			@Override
			public void removeListDataListener(ListDataListener arg0) {}
			
		});
		list_1.setCellRenderer(new DefaultListCellRenderer() {
			/**
			 * 10.12.17
			 */
			private static final long serialVersionUID = 7595664670202303521L;

			@Override 
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,  boolean isSelected, boolean cellHasFocus) {
				JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				renderer.setText( ((FormattedFile)value).getFilename() );
				return renderer;
			}
		});
		list_1.addListSelectionListener(new ListSelectionListener() {
			private FormattedFilePanel rightComponentPanel;
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int loc = getDividerLocation();
				rightComponentPanel = new FormattedFilePanel(parent, list_1.getSelectedValue(), false);
				setRightComponent(rightComponentPanel);
				setDividerLocation( loc );
			}
		});
		setLeftComponent(new JScrollPane( list_1 ));
	}
}
