package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Corpus;

import javax.swing.*;
import java.awt.event.*;

public class CorpusDialog extends JDialog {
    static final int CANCEL = 0;
    static final int OK = 1;

    private Corpus corpus;
    private int closeAction;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;

    public CorpusDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        corpus.setName( textField1.getText() );
        closeAction = OK;
        dispose();
    }

    private void onCancel() {
        closeAction = CANCEL;
        dispose();
    }

    public static void main(String[] args) {
        CorpusDialog dialog = new CorpusDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void setCorpus(Corpus corpus) {
        this.corpus = corpus;
        textField1.setText( corpus.getName() );
    }

    public int getCloseAction() {
        return closeAction;
    }
}
