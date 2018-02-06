package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Author;
import com.sm.textanalyzer.app.CorpusFile;
import com.sm.textanalyzer.app.CorpusFileType;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CorpusFileDialog extends JDialog {
    static final int CANCEL = 0;
    static final int OK = 1;

    private CorpusFile file;
    private int closeAction;
    private AuthorDialog authorDialog;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox languageComboBox;
    private JComboBox fileTypeComboBox;
    private JTextField textFieldName;
    private JLabel lblFilePath;
    private JLabel lblSelectedAuthor;
    private JButton deleteAuthorButton;
    private JButton createNewAuthorButton;
    private JComboBox comboBox1;

    public CorpusFileDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        authorDialog = new AuthorDialog();
        authorDialog.pack();
        authorDialog.setLocationRelativeTo( contentPane );

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

        createNewAuthorButton.addActionListener(e -> {
            Author author = file.getAuthor();
            if(author==null) {
                author = new Author("Select a name");
            }
            authorDialog.setAuthor( author );
            authorDialog.setVisible(true);
            if(authorDialog.getCloseAction()==AuthorDialog.OK) {
                file.setAuthor( author );
                lblSelectedAuthor.setText(author.getName());
            }
        });
        deleteAuthorButton.addActionListener(e -> {
            file.setAuthor( null );
            lblSelectedAuthor.setText("");
        });
    }

    private void onOK() {
        file.setName(textFieldName.getText());
        file.setFileType( (CorpusFileType)fileTypeComboBox.getSelectedItem() );
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
        // TODO Language
        if(file.getAuthor()!=null) {
            lblSelectedAuthor.setText(file.getAuthor().getName());
        }
        this.file = file;
    }

    public int getCloseAction() {
        return closeAction;
    }
}
