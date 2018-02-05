package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.DataPool;
import com.sm.textanalyzer.app.Corpus;
import com.sm.textanalyzer.app.CorpusCollection;
import com.sm.textanalyzer.app.CorpusFile;
import com.sm.textanalyzer.app.Project;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectTreeComponent {

    private JButton newCollectionButton;
    private JButton newFileButton;
    private JButton filesFromDirectoryButton;
    private JTree projectTree;
    private JButton deleteFileButton;
    private JButton editMetadataButton;
    private JTabbedPane projectTabbedPane;
    private JButton createProjectButton;
    private JButton loadProjectButton;
    private JTree analyzeFilesTree;
    private JButton analyzeButton;
    private JTextArea textAreaInformation;
    private JTextField textField1;
    private JButton searchButton;
    private JButton resetButton;
    private JButton saveSearchButton;
    private JCheckBox onlyIncludeResultsWhereCheckBox;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JTextField textField4;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JTextField textField5;
    private JButton deleteCollectionButton;
    private JButton UPButton;
    private JButton DOWNButton;
    private JButton ABCButton;
    private JButton ZYXButton;
    private JButton saveProjectButton;
    private JButton lexiconsButton;
    private JEditorPane editorPane1;
    public JSplitPane contentPane;
    private JButton editEntryButton;

    private ProjectTreeModel projectTreeModel;
    private DefaultTreeModel analyzeTreeModel;
    private boolean modified = false;

    public ProjectTreeComponent() {
        // NEW PROJECT
        createProjectButton.addActionListener(e ->  {
            if(DataPool.projectOpen() && modified) {
                int answer = JOptionPane.showConfirmDialog(projectTabbedPane, "There is an unsaved project. Discard changes and create new project?");
                if(answer!=JOptionPane.YES_OPTION) {
                    return;
                }
            }
            DataPool.createProject();
            setData(DataPool.project);
            newCollectionButton.setEnabled( true );
        });

        // SAVE PROJECT
        saveProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        newCollectionButton.addActionListener(e->{
            String answer = JOptionPane.showInputDialog("Name of collection?");
            if(answer!=null && !answer.isEmpty()) {
                CorpusCollection col = projectTreeModel.addCollection(answer);
                projectTree.setSelectionPath( new TreePath(new Object[]{DataPool.project, col}) );
            }
        });
        deleteCollectionButton.addActionListener(e-> {
            Object comp = projectTree.getSelectionPath().getLastPathComponent();
            if(comp instanceof CorpusCollection) {
                projectTreeModel.deleteCollection( (CorpusCollection)comp );
            }
        });

        projectTreeModel = new ProjectTreeModel( );
        analyzeTreeModel = new DefaultTreeModel( null );

        projectTree.setModel( projectTreeModel );
        projectTree.addTreeSelectionListener(e -> {
            if(e.getPath().getLastPathComponent() instanceof Corpus) {
                textAreaInformation.setText( "This entry represents the corpus you are working with. It contains collections of files." );
                enableFileButtons(false);
                deleteCollectionButton.setEnabled(false);
            }
            else if(e.getPath().getLastPathComponent() instanceof CorpusCollection) {
                textAreaInformation.setText( "This entry represents a collection of files. You can add files one by one or select a directory to add all files from. Only text files ending in .txt are processed." );
                enableFileButtons(true);
                deleteCollectionButton.setEnabled(true);
            }
            else if(e.getPath().getLastPathComponent() instanceof CorpusFile) {
                textAreaInformation.setText( "This entry represents a corpus file. You can delete it or edit its meta data." );
                enableFileButtons(false);
                deleteCollectionButton.setEnabled(false);
            }
        });

        analyzeFilesTree.setModel( analyzeTreeModel );
        analyzeFilesTree.addTreeSelectionListener(e -> {
            analyzeButton.setEnabled( true );
            if(e.getPath().getLastPathComponent() instanceof Corpus) {
                analyzeButton.setText("Analyze Corpus");
            }
            else if(e.getPath().getLastPathComponent() instanceof CorpusCollection) {
                analyzeButton.setText("Analyze Collection");
            }
            else if(e.getPath().getLastPathComponent() instanceof CorpusFile) {
                analyzeButton.setText("Analyze File");
            }
        });

        // EDIT ENTRY
        editEntryButton.addActionListener(e -> {
            Object selected = projectTree.getSelectionPath().getLastPathComponent();
            if(selected instanceof Corpus) {
                CorpusDialog dialog = new CorpusDialog();
                dialog.setCorpus( (Corpus)selected );
                dialog.setVisible(true);
                if(dialog.getCloseAction()==CorpusDialog.OK) {
                   projectTreeModel.rootChanged();
                }
            }
            else if(selected instanceof CorpusCollection) {
                System.out.println("Edit Corpus Collection");
            }
            else if(selected instanceof CorpusFile) {
                System.out.println("Edit Corpus File");
            }
        });
    }

    private void enableFileButtons(boolean enable) {
        newFileButton.setEnabled(enable);
        filesFromDirectoryButton.setEnabled(enable);
        deleteFileButton.setEnabled(enable);
        editMetadataButton.setEnabled(enable);
    }

    public void setData(Project data) {
        projectTreeModel.rootChanged();
        //analyzeTreeModel.setRoot( data.getCorpus() );
        modified = false;
    }

    public void getData(Project data) {
        System.out.println("get Data?");
    }

    public boolean isModified(Project data) {
        System.out.println("Is modified?");
        return modified;
    }
}
