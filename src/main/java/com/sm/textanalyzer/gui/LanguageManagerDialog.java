package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.DataPool;
import com.sm.textanalyzer.app.Language;

import javax.swing.*;
import java.awt.event.*;

public class LanguageManagerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JComboBox comboBoxLanguage;
    private JTextField textFieldLanguageName;
    private JButton addButton;
    private JButton deleteSelectedLanguageButton;
    private DefaultComboBoxModel<Language> model = new DefaultComboBoxModel<Language>();

    public LanguageManagerDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Manage Languages");
        getRootPane().setDefaultButton(buttonCancel);

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

        init();
        comboBoxLanguage.setRenderer((ListCellRenderer<Language>) (list, value, index, isSelected, cellHasFocus) -> {
            DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();
            JLabel label = (JLabel) defaultListCellRenderer.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            label.setText(value.getName());
            return label;
        });

        addButton.addActionListener(e-> {
            if(!textFieldLanguageName.getText().isEmpty()) {
                Language lang = new Language(textFieldLanguageName.getText());
                DataPool.getLanguages().add(lang);
                model.addElement(lang);
                textFieldLanguageName.setText("");
            }
        });

        deleteSelectedLanguageButton.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(LanguageManagerDialog.this, "Really delete language?");
            if(answer==JOptionPane.YES_OPTION) {
                DataPool.getLanguages().remove(comboBoxLanguage.getSelectedItem());
                model.removeElement(comboBoxLanguage.getSelectedItem());
            }
        });
    }

    public void init() {
        model = new DefaultComboBoxModel<>();
        for(Language lang : DataPool.getLanguages()) {
            model.addElement(lang);
        }
        comboBoxLanguage.setModel(model);

    }

    private void onCancel() {
        dispose();
    }
}
