package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Corpus;
import com.sm.textanalyzer.app.CorpusCollection;
import com.sm.textanalyzer.app.CorpusFile;
import com.sm.textanalyzer.app.Project;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class ProjectTreeComponent {

    private Project project;

    private JButton newCollectionButton;
    private JButton newFileButton;
    private JButton filesFromDirectoryButton;
    private JTree tree1;
    private JButton deleteFileButton;
    private JButton editMetadataButton;
    public JTabbedPane contentPane;
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

    private ProjectTreeModel projectTreeModel;
    private DefaultTreeModel analyzeTreeModel;

    public ProjectTreeComponent() {
        newCollectionButton.addActionListener(e->{
            String answer = JOptionPane.showInputDialog("Name of collection?");
            if(answer!=null && !answer.isEmpty()) {
                CorpusCollection col = projectTreeModel.addCollection(answer);
                tree1.setSelectionPath(new TreePath(new TreeNode[]{(TreeNode) projectTreeModel.getRoot(), col}));
            }
        });

        projectTreeModel = new ProjectTreeModel( null );
        analyzeTreeModel = new DefaultTreeModel( null );

        tree1.setModel( projectTreeModel );
        tree1.addTreeSelectionListener(e -> {
            if(e.getPath().getLastPathComponent() instanceof Corpus) {
                textAreaInformation.setText( "This entry represents the corpus you are working with. It contains collections of files." );
            }
            else if(e.getPath().getLastPathComponent() instanceof CorpusCollection) {
                textAreaInformation.setText( "This entry represents a collection of files. You can add files one by one or select a directory to add all files from. Only text files ending in .txt are processed." );
            }
            else if(e.getPath().getLastPathComponent() instanceof CorpusFile) {
                textAreaInformation.setText( "This entry represents a corpus file. You can delete it or edit its meta data." );
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
        createProjectButton.addActionListener(e ->  {
            setData( new Project() );
        });
    }

    public void setData(Project data) {
        this.project = data;
        projectTreeModel.setRoot( project.getCorpus() );
        analyzeTreeModel.setRoot( project.getCorpus() );
    }

    public void getData(Project data) {
        System.out.println("get Data?");
    }

    public boolean isModified(Project data) {
        return false;
    }
}
