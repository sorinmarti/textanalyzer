package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.DataPool;
import com.sm.textanalyzer.MainClass;
import com.sm.textanalyzer.app.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ProjectTreeComponent {
    static final String PROJECT_FILE_ENDING = "xml"; //NON-NLS
    static final String LEXICON_FILE_ENDING = "xml"; //NON-NLS
    static final String TEXT_FILE_ENDING = "txt"; //NON-NLS

    private AboutDialog aboutDialog;
    private CorpusDialog corpusDialog;
    private CollectionDialog collectionDialog;
    private CorpusFileDialog corpusFileDialog;
    private AuthorDialog authorDialog;

    private final JFileChooser fileChooser = new JFileChooser();
    private final FileNameExtensionFilter projectFileFilter = new FileNameExtensionFilter("Project files", PROJECT_FILE_ENDING);
    final FileNameExtensionFilter lemmaFileFilter = new FileNameExtensionFilter("Lexicon", LEXICON_FILE_ENDING);
    final FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Text files", TEXT_FILE_ENDING);
    final FileFilter folderFileFilter = new FileFilter() {

        @Override
        public String getDescription() {
            return "Directories";
        }

        @Override
        public boolean accept(File arg0) {
            return arg0 != null && arg0.isDirectory();
        }
    };

    private JButton newCollectionButton;
    private JButton newFileButton;
    private JButton filesFromDirectoryButton;
    private JTree projectTree;
    private JButton deleteFileButton;
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
    private JButton deleteCollectionButton;
    private JButton UPButton;
    private JButton DOWNButton;
    private JButton ABCButton;
    private JButton ZYXButton;
    private JButton saveProjectButton;
    private JButton lexiconsButton;
    private JEditorPane helpEditorPanel;
    public JSplitPane contentPane;
    private JButton editEntryButton;
    private JCheckBox authorIsCheckBox;
    private JComboBox comboBox9;
    private JCheckBox authorMatchesCheckBox;
    private JTextField textField6;
    private JTextField textField7;
    private JComboBox comboBox10;
    private JCheckBox languageIsCheckBox;
    private JComboBox comboBox1;
    private JCheckBox fileTypeIsCheckBox;
    private JComboBox comboBox2;
    private JButton authorsButton;
    private JButton languagesButton;
    private JCheckBox dateIsCheckBox;
    private JTextField textField2;
    private JTextField textField3;
    private JTable table1;
    private JList list1;
    private JButton exportStatisticsButton;
    private JButton exportTokenListButton;
    private JTextField textField4;
    private JButton searchButton1;
    private JButton resetButton1;
    private JButton enableEditingButton;
    private JButton disableEditingButton;
    private JTextField textField5;
    private JButton aboutButton;
    private JButton moreHelpButton;

    private ProjectTreeModel projectTreeModel;
    private AnalyzeTreeModel analyzeTreeModel;
    private boolean modified = false;

    public ProjectTreeComponent() {
        // DIALOG INITIALIZATION
        aboutDialog = new AboutDialog();
        aboutDialog.pack();
        aboutDialog.setLocationRelativeTo( contentPane );

        corpusDialog = new CorpusDialog();
        corpusDialog.pack();
        corpusDialog.setLocationRelativeTo( contentPane );

        collectionDialog = new CollectionDialog();
        collectionDialog.pack();
        collectionDialog.setLocationRelativeTo( contentPane );

        corpusFileDialog = new CorpusFileDialog();
        corpusFileDialog.pack();
        corpusFileDialog.setLocationRelativeTo( contentPane );

        authorDialog = new AuthorDialog();
        authorDialog.pack();
        authorDialog.setLocationRelativeTo( contentPane );

        // HELP PAGE INITIALIZATION
        helpEditorPanel.setEditable(false);
        HTMLEditorKit kit = new HTMLEditorKit();
        helpEditorPanel.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {background: #E6E6E6; font-size: 10px; font-family:arial; margin: 0px; padding: 5px; }");
        styleSheet.addRule("h1 {font-size:14px; color:#0B6121;}");
        styleSheet.addRule("h2 {font-size:10px; color:#0B6121;}");
        setHelpPage( 0 );

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
            fireProjectChanged();
        });

        // SAVE PROJECT
        saveProjectButton.addActionListener(e -> {
            File file = showSaveDialog("Aborted saving process", projectFileFilter);
            if(file!=null) {
                try {
                    DataPool.saveProject(file);
                    showMessage("Project saved");
                    fireProjectSaved();
                } catch (Exception e1) {
                    showMessage("Project could not be saved");
                }
            }
        });

        // LOAD PROJECT
        loadProjectButton.addActionListener(e -> {
            File file = showOpenDialog("Open Project", "No project file selected", projectFileFilter, false);

            if( file != null) {
                Project project;
                try {
                    project = ProjectFileManager.readProjectFile(file);
                    // TODO Check if Lexicon available
                } catch (Exception e1) {
                    showMessage("Failed loading project file");
                    return;
                }
                DataPool.project = project;

                fireProjectOpened();
            } else {
                showMessage("No Project file selected.");
            }
        });

        // NEW COLLECTION
        newCollectionButton.addActionListener(e->{
            String answer = JOptionPane.showInputDialog("Name of collection?");
            if(answer!=null && !answer.isEmpty()) {
                CorpusCollection col = projectTreeModel.addCollection(answer);
                projectTree.setSelectionPath( projectTreeModel.getPathToRoot(col) );
                fireProjectChanged();
            }
        });
        // DELETE COLLECTION
        deleteCollectionButton.addActionListener(e-> {
            int answer = JOptionPane.showConfirmDialog(contentPane, "Really remove collection?");
            if(answer==JOptionPane.YES_OPTION) {
                Object comp = projectTree.getSelectionPath().getLastPathComponent();
                if (comp instanceof CorpusCollection) {
                    projectTreeModel.deleteCollection((CorpusCollection) comp);
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot(DataPool.project.getCorpus()) );
                    fireProjectChanged();
                }
            }
        });

        // ADD FILE
        newFileButton.addActionListener(e -> {
            Object selected = projectTree.getSelectionPath().getLastPathComponent();
            if(selected instanceof CorpusCollection) {
                File file = showOpenDialog("Add Corpus Text File", "No files selected", textFileFilter, false);
                if(file != null) {
                    if( !FileUtils.fileExists( file ) ) {
                        showMessage("The selected file does not exist or is not readable.");
                    }
                    else if( FileUtils.fileExistsInProjectList( DataPool.project, file ) ) {
                        showMessage("The file is already in the corpus library");
                    }
                    else {
                        CorpusFile corpusFile = projectTreeModel.addFile( ((CorpusCollection)selected), file);
                        projectTree.setSelectionPath( projectTreeModel.getPathToRoot(corpusFile) );
                        fireProjectChanged();
                    }
                }
            }
        });

        deleteFileButton.addActionListener( e -> {
            int answer = JOptionPane.showConfirmDialog(contentPane, "Really remove file?");
            if(answer==JOptionPane.YES_OPTION) {
                Object comp = projectTree.getSelectionPath().getLastPathComponent();
                if (comp instanceof CorpusFile) {
                    projectTreeModel.deleteFile((CorpusFile) comp);
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot(DataPool.project.getCorpus()) );
                    fireProjectChanged();
                }
            }
        });
        projectTreeModel = new ProjectTreeModel( );
        analyzeTreeModel = new AnalyzeTreeModel( );

        projectTree.setModel( projectTreeModel );
        projectTree.addTreeSelectionListener(e -> {
            if(e.getPath().getLastPathComponent() instanceof Corpus) {
                textAreaInformation.setText( "This entry represents the corpus you are working with. It contains collections of files." );
                enableFileButtons(false);
                enableSortButtons(false, true);
                deleteCollectionButton.setEnabled(false);
                editEntryButton.setEnabled(true);
            }
            else if(e.getPath().getLastPathComponent() instanceof CorpusCollection) {
                textAreaInformation.setText( "This entry represents a collection of files. You can add files one by one or select a directory to add all files from. Only text files ending in .txt are processed." );
                enableFileButtons(true);
                enableSortButtons(true, true);
                deleteCollectionButton.setEnabled(true);
                editEntryButton.setEnabled(true);
            }
            else if(e.getPath().getLastPathComponent() instanceof CorpusFile) {
                textAreaInformation.setText( "This entry represents a corpus file. You can delete it or edit its meta data." );
                enableFileButtons(false);
                enableSortButtons(true, false);
                deleteCollectionButton.setEnabled(false);
                deleteFileButton.setEnabled(true);
                editEntryButton.setEnabled(true);
            }
            else {
                editEntryButton.setEnabled(false);
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
                corpusDialog.setCorpus( (Corpus)selected );
                corpusDialog.setVisible(true);
                if(corpusDialog.getCloseAction()==CorpusDialog.OK) {
                    fireProjectChanged();
                    analyzeTreeModel.rootChanged();
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot(selected) );
                }
            }
            else if(selected instanceof CorpusCollection) {
                collectionDialog.setCorpusCollection( (CorpusCollection)selected );
                collectionDialog.setVisible(true);
                if(collectionDialog.getCloseAction()==CollectionDialog.OK) {
                    fireProjectChanged();
                    analyzeTreeModel.rootChanged();
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot(selected) );
                }
            }
            else if(selected instanceof CorpusFile) {
                corpusFileDialog.setFile((CorpusFile)selected);
                corpusFileDialog.setVisible(true);
                if(corpusFileDialog.getCloseAction()==CorpusFileDialog.OK) {
                    fireProjectChanged();
                    analyzeTreeModel.rootChanged();
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot(selected) );
                }
            }
        });


        projectTabbedPane.addChangeListener(e -> {
            int idx = projectTabbedPane.getSelectedIndex();
            setHelpPage( idx );

        });
        authorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                Author author = file.getAuthor();
                if(author==null) {
                    author = new Author("Select a name");
                }
                authorDialog.setAuthor( author );
                authorDialog.setVisible(true);
                if(authorDialog.getCloseAction()==AuthorDialog.OK) {
                    //TODO
                }
                */
            }
        });
        // ABOUT
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutDialog.setVisible(true);
            }
        });
    }

    private void setHelpPage(int idx) {
        String pageName;
        switch (idx) {
            case 0:     // Manage Corpus
                pageName = "manage";
                break;
            case 1:     // Process Corpus
                pageName = "process";
                break;
            case 2:     // Search Corpus
                pageName = "search";
                break;
            case 3:     // Corpus Statistics
                pageName = "statistics";
                break;
            default:
                pageName = "default";
        }

        try {
            File file = new File("res/help/"+pageName+".html");
            helpEditorPanel.setPage(file.toURI().toURL());
        } catch (IOException e1) {
            e1.printStackTrace();
            helpEditorPanel.setText("Could not load help file.");
        }
    }

    private void enableSortButtons(boolean updown, boolean abcxyz) {
        UPButton.setEnabled( updown );
        DOWNButton.setEnabled( updown );
        ABCButton.setEnabled( abcxyz );
        ZYXButton.setEnabled( abcxyz );
    }

    private void enableFileButtons(boolean enable) {
        newFileButton.setEnabled(enable);
        filesFromDirectoryButton.setEnabled(enable);
        deleteFileButton.setEnabled(enable);
    }

    public void setData(Project data) {
        projectTreeModel.rootChanged();
        analyzeTreeModel.rootChanged();
        projectTree.setSelectionPath(projectTreeModel.getPathToRoot(DataPool.project.getCorpus()));
        modified = false;
    }

    public void getData(Project data) {
        System.out.println("get Data?");
    }

    public boolean isModified(Project data) {
        System.out.println("Is modified?");
        return modified;
    }

    private void fireProjectChanged() {
        modified = true;
        saveProjectButton.setEnabled(true);
        MainClass.setWindowTitle(modified);
    }

    private void fireProjectSaved() {
        modified = false;
        saveProjectButton.setEnabled(false);
        MainClass.setWindowTitle(modified);
    }

    private void fireProjectOpened() {
       saveProjectButton.setEnabled( true );
       modified = false;
       setData( DataPool.project );
       MainClass.setWindowTitle(modified);
    }

    /**
     * Shows a file open dialog.
     * @param title The title of the dialog
     * @param cancelMsg The cancel message in case the opening gets cancelled
     * @param filter The file filter to apply.
     * @param foldersOnly Flag if only folders can be shown and selected.
     * @return The selected File.
     */
    File showOpenDialog(String title, String cancelMsg, FileFilter filter, boolean foldersOnly) {
        fileChooser.setDialogTitle(title);
        fileChooser.setFileFilter(filter);
        if(foldersOnly) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        else {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }

        int returnVal = fileChooser.showOpenDialog(contentPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return  fileChooser.getSelectedFile();
        }
        else {
            showMessage(cancelMsg);
        }
        return null;
    }

    private File showSaveDialog(String cancelMsg, FileFilter filter) {
        fileChooser.setDialogTitle("Save Project");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int answer = fileChooser.showSaveDialog(contentPane);
        if(answer == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        else {
            showMessage(cancelMsg);
        }
        return null;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(contentPane, message);
    }
}
