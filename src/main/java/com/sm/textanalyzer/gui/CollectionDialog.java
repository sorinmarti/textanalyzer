package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.CorpusCollection;
import com.sm.textanalyzer.app.CorpusFileType;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;

public class CollectionDialog extends JDialog {
    static final int CANCEL = 0;
    static final int OK = 1;

    private CorpusCollection collection;
    private int closeAction;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JComboBox comboBox1;

    public CollectionDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        comboBox1.setModel(new ComboBoxModel<CorpusFileType>() {

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
        comboBox1.setRenderer(new ListCellRenderer<CorpusFileType>() {
            @Override
            public Component getListCellRendererComponent(JList list, CorpusFileType value, int index, boolean isSelected, boolean cellHasFocus) {
                DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
                JLabel item = (JLabel) defaultRenderer.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                item.setText( value.getName() );
                return item;
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        collection.setName( textField1.getText() );
        collection.setAcceptedType( (CorpusFileType) comboBox1.getSelectedItem() );
        closeAction = OK;
        dispose();
    }

    private void onCancel() {
        closeAction = CANCEL;
        dispose();
    }

    public void setCorpusCollection(CorpusCollection collection) {
        this.collection = collection;
        textField1.setText( collection.getName() );
        comboBox1.setSelectedItem( collection.getAcceptedType() );
    }

    public int getCloseAction() {
        return closeAction;
    }
}
