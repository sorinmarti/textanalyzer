package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.CorpusFile;
import com.sm.textanalyzer.app.CorpusFileType;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CorpusFileDialog extends JDialog {
    static final int CANCEL = 0;
    static final int OK = 1;

    private CorpusFile file;
    private int closeAction;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox languageComboBox;
    private JComboBox fileTypeComboBox;
    private JTextField textFieldName;
    private JLabel lblFilePath;
    private JComboBox comboBox1;
    private JTextField textField1;

    public CorpusFileDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        fileTypeComboBox.setModel(new ComboBoxModel<CorpusFileType>() {

            CorpusFileType selected;

            @Override
            public void setSelectedItem(Object anItem) {
                selected = (CorpusFileType)anItem;
            }

            @Override
            public Object getSelectedItem() {
                return selected;
            }

            @Override
            public int getSize() {
                return 3;
            }

            @Override
            public CorpusFileType getElementAt(int index) {
                switch (index) {
                    case 0:
                        return CorpusFileType.ALL;
                    case 1:
                        return CorpusFileType.GENERIC_TEXT;
                    case 2:
                        return CorpusFileType.WHATSAPP;
                }
                return CorpusFileType.ALL;
            }

            @Override
            public void addListDataListener(ListDataListener l) {}

            @Override
            public void removeListDataListener(ListDataListener l) {}
        });
        fileTypeComboBox.setRenderer((ListCellRenderer<CorpusFileType>) (list, value, index, isSelected, cellHasFocus) -> {
            DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
            JLabel item = (JLabel) defaultRenderer.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            if(value==CorpusFileType.ALL) {
                item.setText("undefined");
            }
            else {
                item.setText(value.getName());
            }
            return item;
        });

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        file.setName(textFieldName.getText());
        file.setFileType( (CorpusFileType)fileTypeComboBox.getSelectedItem() );
        if(textField1.getText().isEmpty()) {
            file.setFileDate(null);
        }
        else {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = formatter.parse(textField1.getText());
                file.setFileDate(date);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(contentPane, "Invalid date format.");
                return;
            }
        }
        //TODO Language
        closeAction = OK;
        dispose();
    }

    private void onCancel() {
        closeAction = CANCEL;
        dispose();
    }

    public void setFile(CorpusFile file) {
        lblFilePath.setText( file.getPath().toString() );
        textFieldName.setText( file.getName() );
        fileTypeComboBox.setSelectedItem( file.getFileType() );
        textField1.setText(file.getFileDateString());

        // TODO Language
        if(file.getAuthor()!=null) {
            //TODO Author
        }
        this.file = file;
    }

    public int getCloseAction() {
        return closeAction;
    }
}
