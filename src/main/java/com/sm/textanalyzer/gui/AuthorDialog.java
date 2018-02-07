package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Author;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AuthorDialog extends JDialog {
    static final int CANCEL = 0;
    static final int OK = 1;

    private Author author;
    private int closeAction;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JTextField textFieldAgeFrom;
    private JTextField textFieldAgeTo;
    private JComboBox comboBoxGender;
    private JTextField textFieldID;
    private JButton generateIDButton;

    public AuthorDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        for(Integer gender : Author.getGenderArray()) {
            comboBoxGender.addItem(gender);
        }
        comboBoxGender.setRenderer(new ListCellRenderer<Integer>() {
            @Override
            public Component getListCellRendererComponent(JList list, Integer value, int index, boolean isSelected, boolean cellHasFocus) {
                DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
                JLabel item = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                switch (value) {
                    case 0:
                        item.setText("undefined");
                        break;
                    case 1:
                        item.setText("male");
                        break;
                    case 2:
                        item.setText("female");
                        break;
                    case 3:
                        item.setText("other");
                        break;
                    default:
                        item.setText("not implemented.");
                }
                return  item;
            }
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
        generateIDButton.addActionListener(e -> {
            textFieldID.setText( String.valueOf(textFieldName.getText().hashCode()));
        });
    }

    private void onOK() {
        // TODO check if new id is unique.
        author.setId( textFieldID.getText() );
        author.setName( textFieldName.getText() );
        author.setAgeMin( Integer.valueOf(textFieldAgeFrom.getText()) );
        author.setAgeMax( Integer.valueOf(textFieldAgeTo.getText()) );
        author.setGender( comboBoxGender.getSelectedIndex() );
        closeAction = OK;
        dispose();
    }

    private void onCancel() {
        closeAction = CANCEL;
        dispose();
    }

    public void setAuthor(Author author) {
        this.author = author;
        textFieldID.setText( author.getId() );
        textFieldName.setText( author.getName() );
        textFieldAgeFrom.setText( String.valueOf(author.getAgeMin()) );
        textFieldAgeTo.setText( String.valueOf(author.getAgeMax()) );
        comboBoxGender.setSelectedIndex( author.getGender() );
    }

    public int getCloseAction() {
        return closeAction;
    }
}
