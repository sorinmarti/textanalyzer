package com.sm.textanalyzer.gui;

import javax.swing.*;
import java.awt.event.*;

public class AuthorManager extends JDialog {
    private JPanel contentPane;
    private JButton buttonClose;
    private JComboBox comboBoxAuthors;
    private JButton editSelectedAuthorButton;
    private JButton deleteSelectedAuthorButton;
    private JButton createANewAuthorButton;

    public AuthorManager() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonClose);

        buttonClose.addActionListener(new ActionListener() {
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

        editSelectedAuthorButton.addActionListener(e -> {
            //TODO show AuthorDialog (existing) with selected author from combo box
        });
        createANewAuthorButton.addActionListener(e -> {
            //TODO show AuthorDialog with new author
        });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
