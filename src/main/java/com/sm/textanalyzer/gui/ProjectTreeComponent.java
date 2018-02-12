package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.DataPool;
import com.sm.textanalyzer.MainClass;
import com.sm.textanalyzer.app.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectTreeComponent {
    static final String PROJECT_FILE_ENDING = "xml"; //NON-NLS
    static final String LEXICON_FILE_ENDING = "xml"; //NON-NLS
    static final String TEXT_FILE_ENDING = "txt"; //NON-NLS

    private AboutDialog aboutDialog;
    private CorpusDialog corpusDialog;
    private CollectionDialog collectionDialog;
    private CorpusFileDialog corpusFileDialog;
    private AuthorDialog authorDialog;

    private AuthorManager authorManager;
    private LexiconManager lexiconManager;
    private LanguageManagerDialog languageManager;

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

    final Comparator<CorpusCollection> CORPUS_COMP = Comparator.comparing(CorpusCollection::getName);
    final Comparator<CorpusFile> FILES_COMP = Comparator.comparing(CorpusFile::getName);

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
    private JButton tokenListSearchButton;
    private JButton tokenListResetButton;
    private JButton enableEditingButton;
    private JButton disableEditingButton;
    private JTextField textField5;
    private JButton aboutButton;
    private JButton moreHelpButton;
    private JButton copyTextFilesButton;

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

        authorManager = new AuthorManager();
        authorManager.pack();
        authorManager.setLocationRelativeTo( contentPane );

        languageManager = new LanguageManagerDialog();
        languageManager.pack();
        languageManager.setLocationRelativeTo( contentPane );

        lexiconManager = new LexiconManager();
        lexiconManager.pack();
        lexiconManager.setLocationRelativeTo( contentPane );

        // HELP PAGE INITIALIZATION
        helpEditorPanel.setEditable(false);
        HTMLEditorKit kit = new HTMLEditorKit();
        helpEditorPanel.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {background: #E6E6E6; font-size: 10px; font-family:arial; margin: 0px; padding: 5px; }");
        styleSheet.addRule("h1 {font-size:14px; color:#0B6121;}");
        styleSheet.addRule("h2 {font-size:10px; color:#0B6121;}");
        setHelpPage( 0 );

        // INITIALIZE UI
        setButtons();


        // NEW PROJECT
        createProjectButton.addActionListener(e ->  {
            if(DataPool.projectOpen() && modified) {
                int answer = JOptionPane.showConfirmDialog(projectTabbedPane, "There is an unsaved project. Discard changes and create new project?");
                if(answer!=JOptionPane.YES_OPTION) {
                    return;
                }
            }
            DataPool.createProject();
            fireProjectOpened();
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
        filesFromDirectoryButton.addActionListener(e-> {
            Object selected = projectTree.getSelectionPath().getLastPathComponent();
            if(selected instanceof CorpusCollection) {
                File file = showOpenDialog("Add Corpus Text Files from Folder", "No files selected", folderFileFilter, true);
                if(file != null) {
                    if( !FileUtils.fileExists( file ) ) {
                        showMessage("The selected folder does not exist or is not readable.");
                    }
                    else if(!file.isDirectory()) {
                        showMessage("The selected file is not a directory.");
                    }
                    else {
                        List<Path> filesToOpen = FileUtils.collectFilenames( file.getAbsolutePath() );
                        if(filesToOpen.size()>0) {
                            int addedFiles = 0;
                            int ignoredFiles = 0;
                            CorpusFile corpusFile = null;
                            String str = "<html>Nicht hinzugefügt:<br/>";
                            for(Path p : filesToOpen) {
                                if( !FileUtils.fileExists( p ) ) {
                                    str += p.getFileName().toString() + " existiert nicht.<br/>";
                                    ignoredFiles++;
                                }
                                else if( FileUtils.fileExistsInProjectList( DataPool.project, p ) ) {
                                    str += p.getFileName().toString() + " ist bereits im Projekt.<br/>";
                                    ignoredFiles++;
                                }
                                else {
                                    addedFiles++;
                                    corpusFile = projectTreeModel.addFile( ((CorpusCollection)selected), new File(p.toAbsolutePath().toString()));
                                }
                            }
                            if(addedFiles>0) {
                                projectTree.setSelectionPath( projectTreeModel.getPathToRoot(corpusFile) );
                                fireProjectChanged();
                            }
                            if(ignoredFiles>0) {
                                str += "Es wurden "+ignoredFiles+" Dateien übersprungen und "+addedFiles+" Dateien hinzugefügt.";
                                showMessage( str );
                            }
                        }
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
            setButtons();
        });
        projectTree.setCellRenderer(new TreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer();
                JLabel label = (JLabel)defaultTreeCellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                if(value instanceof Corpus) {
                    label.setIcon( getImageIcon("icons8-copyright.png"));
                }
                else if(value instanceof CorpusCollection) {
                    label.setIcon( getImageIcon("icons8-binder.png"));
                }
                else if(value instanceof CorpusFile) {
                    label.setIcon( getImageIcon("icons8-file.png"));
                }
                return label;
            }

            private Icon getImageIcon(String iconName) {
                URL theURL = MainClass.class.getClassLoader().getResource("../resources/images/"+iconName);
                ImageIcon icon = new ImageIcon(theURL);
                Image img = icon.getImage();
                Image newImg = img.getScaledInstance(16,16, Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                return icon;
            }
        });

        analyzeFilesTree.setModel( analyzeTreeModel );
        analyzeFilesTree.addTreeSelectionListener(e -> {
            setButtons();
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
                    analyzeTreeModel.rootChanged();
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot(selected) );
                    fireProjectChanged();
                }
            }
            else if(selected instanceof CorpusCollection) {
                collectionDialog.setCorpusCollection( (CorpusCollection)selected );
                collectionDialog.setVisible(true);
                if(collectionDialog.getCloseAction()==CollectionDialog.OK) {
                    analyzeTreeModel.rootChanged();
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot(selected) );
                    fireProjectChanged();
                }
            }
            else if(selected instanceof CorpusFile) {
                corpusFileDialog.setFile((CorpusFile)selected);
                corpusFileDialog.setVisible(true);
                if(corpusFileDialog.getCloseAction()==CorpusFileDialog.OK) {
                    analyzeTreeModel.rootChanged();
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot(selected) );
                    fireProjectChanged();
                }
            }
        });

        setUpOptionsButtons();
        setUpHelpButtons();
        setUpSortingButtons();

        projectTabbedPane.addChangeListener(e -> {
            int idx = projectTabbedPane.getSelectedIndex();
            setHelpPage( idx );

        });
    }

    private void setUpOptionsButtons() {
        lexiconsButton.addActionListener(e->{
            lexiconManager.setVisible(true);
        });
        authorsButton.addActionListener(e->{
            authorManager.setVisible(true);
        });
        languagesButton.addActionListener(e->{
            languageManager.init();
            languageManager.setVisible(true);
        });

    }

    /**
     * Adds {@link ActionListener} to help buttons.
     */
    private void setUpHelpButtons() {
        // MORE HELP
        moreHelpButton.addActionListener(e -> showMessage("Extended help is not implemented yet."));
        // ABOUT
        aboutButton.addActionListener(e -> aboutDialog.setVisible(true));
    }

    /**
     * Adds {@link ActionListener} to sort buttons.
     */
    private void setUpSortingButtons() {
        // UP
        UPButton.addActionListener(e -> {
            if(projectTree.getSelectionPath()!=null) {
                Object selectedComponent = projectTree.getSelectionPath().getLastPathComponent();
                if(selectedComponent instanceof CorpusCollection) {
                    int index = projectTreeModel.getIndexOfChild(DataPool.getCorpus(), selectedComponent);
                    Collections.swap(DataPool.getCorpus().getCollections(), index, (index-1));
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot( selectedComponent ));
                }
                else if(selectedComponent instanceof CorpusFile) {
                    CorpusCollection collection = projectTreeModel.getCollectionByFile((CorpusFile) selectedComponent);
                    int index = collection.getFiles().indexOf(selectedComponent);
                    Collections.swap(collection.getFiles(), index, (index-1));
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot( selectedComponent ));
                }
            }
        });
        // DOWN
        DOWNButton.addActionListener(e -> {
            if(projectTree.getSelectionPath()!=null) {
                Object selectedComponent = projectTree.getSelectionPath().getLastPathComponent();
                if(selectedComponent instanceof CorpusCollection) {
                    int index = projectTreeModel.getIndexOfChild(DataPool.getCorpus(), selectedComponent);
                    Collections.swap(DataPool.getCorpus().getCollections(), index, (index+1));
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot( selectedComponent ));
                }
                else if(selectedComponent instanceof CorpusFile) {
                    CorpusCollection collection = projectTreeModel.getCollectionByFile((CorpusFile) selectedComponent);
                    int index = collection.getFiles().indexOf(selectedComponent);
                    Collections.swap(collection.getFiles(), index, (index+1));
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot( selectedComponent ));
                }
            }

        });
        // ASC
        ABCButton.addActionListener(e -> {
            if(projectTree.getSelectionPath()!=null) {
                Object selectedComponent = projectTree.getSelectionPath().getLastPathComponent();
                if(selectedComponent instanceof Corpus) {
                    Collections.sort(DataPool.getCorpus().getCollections(), CORPUS_COMP );
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot( selectedComponent ));
                }
                else if(selectedComponent instanceof CorpusCollection) {
                    Collections.sort(((CorpusCollection)selectedComponent).getFiles(), FILES_COMP );
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot( selectedComponent ));
                }
            }
        });
        // DESC
        ZYXButton.addActionListener(e -> {
            if(projectTree.getSelectionPath()!=null) {
                Object selectedComponent = projectTree.getSelectionPath().getLastPathComponent();
                if(selectedComponent instanceof Corpus) {
                    Collections.sort(DataPool.getCorpus().getCollections(), CORPUS_COMP.reversed() );
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot( selectedComponent ));
                }
                else if(selectedComponent instanceof CorpusCollection) {
                    Collections.sort(((CorpusCollection)selectedComponent).getFiles(), FILES_COMP.reversed() );
                    projectTreeModel.rootChanged();
                    projectTree.setSelectionPath( projectTreeModel.getPathToRoot( selectedComponent ));
                }
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

    private void fireProjectChanged() {
        modified = true;
        MainClass.setWindowTitle(modified);
        setButtons();
    }

    private void fireProjectSaved() {
        modified = false;
        MainClass.setWindowTitle(modified);
        setButtons();
    }

    private void fireProjectOpened() {
        modified = false;
        projectTreeModel.rootChanged();
        analyzeTreeModel.rootChanged();
        projectTree.setSelectionPath(projectTreeModel.getPathToRoot(DataPool.project.getCorpus()));
        MainClass.setWindowTitle(modified);
        setButtons();
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

    private void setButtons() {
        // Always enabled
        createProjectButton.setEnabled( true );
        loadProjectButton.setEnabled(true);
        aboutButton.setEnabled(true);
        moreHelpButton.setEnabled(true);

        if(DataPool.projectOpen()) {
            saveProjectButton.setEnabled(true);
            copyTextFilesButton.setEnabled(true);

            lexiconsButton.setEnabled(true);
            authorsButton.setEnabled(true);
            languagesButton.setEnabled(true);

            Object projectTreeSelected = null;
            if(projectTree.getSelectionPath()!=null) {
                projectTreeSelected = projectTree.getSelectionPath().getLastPathComponent();
            }
            if(projectTreeSelected instanceof Corpus) {
                newCollectionButton.setEnabled(true);
                deleteCollectionButton.setEnabled(false);
                newFileButton.setEnabled(false);
                filesFromDirectoryButton.setEnabled(false);
                editEntryButton.setEnabled(true);
                deleteFileButton.setEnabled(false);

                if(DataPool.getCorpus().getNumCollections()>1) {
                    ABCButton.setEnabled(true);
                    ZYXButton.setEnabled(true);
                }
                else {
                    ABCButton.setEnabled(false);
                    ZYXButton.setEnabled(false);
                }

                UPButton.setEnabled(false);
                DOWNButton.setEnabled(false);
            }
            else if(projectTreeSelected instanceof CorpusCollection) {
                newCollectionButton.setEnabled(false);
                deleteCollectionButton.setEnabled(true);
                newFileButton.setEnabled(true);
                filesFromDirectoryButton.setEnabled(true);
                editEntryButton.setEnabled(true);
                deleteFileButton.setEnabled(false);

                if(((CorpusCollection)projectTreeSelected).getNumFiles()>1) {
                    ABCButton.setEnabled(true);
                    ZYXButton.setEnabled(true);
                }
                else {
                    ABCButton.setEnabled(false);
                    ZYXButton.setEnabled(false);
                }

                int idxOfCollection = projectTreeModel.getIndexOfChild(DataPool.getCorpus(), projectTreeSelected);
                if(idxOfCollection>0) {
                    UPButton.setEnabled(true);
                }
                else {
                    UPButton.setEnabled(false);
                }

                if(idxOfCollection<(DataPool.getCorpus().getNumCollections()-1)) {
                    DOWNButton.setEnabled(true);
                }
                else {
                    DOWNButton.setEnabled(false);
                }

            }
            else if(projectTreeSelected instanceof CorpusFile) {
                newCollectionButton.setEnabled(false);
                deleteCollectionButton.setEnabled(false);
                newFileButton.setEnabled(false);
                filesFromDirectoryButton.setEnabled(false);
                editEntryButton.setEnabled(true);
                ABCButton.setEnabled(false);
                ZYXButton.setEnabled(false);
                deleteFileButton.setEnabled(true);

                CorpusCollection collection = projectTreeModel.getCollectionByFile((CorpusFile) projectTreeSelected);

                if(collection.getFiles().indexOf(projectTreeSelected)<=0) {
                    UPButton.setEnabled(false);
                }
                else {
                    UPButton.setEnabled(true);
                }

                if(collection.getFiles().indexOf(projectTreeSelected)==collection.getFiles().size()-1) {
                    DOWNButton.setEnabled(false);
                }
                else {
                    DOWNButton.setEnabled(true);
                }


            }
            else {
                newCollectionButton.setEnabled(false);
                deleteCollectionButton.setEnabled(false);
                newFileButton.setEnabled(false);
                filesFromDirectoryButton.setEnabled(false);
                deleteFileButton.setEnabled(false);
                editEntryButton.setEnabled(false);
                ABCButton.setEnabled(false);
                ZYXButton.setEnabled(false);
                UPButton.setEnabled(false);
                DOWNButton.setEnabled(false);
            }

        }
        else {  // PROJECT IS CLOSED
            saveProjectButton.setEnabled(false);
            newCollectionButton.setEnabled(false);
            deleteCollectionButton.setEnabled(false);
            newFileButton.setEnabled(false);
            filesFromDirectoryButton.setEnabled(false);
            deleteFileButton.setEnabled(false);
            editEntryButton.setEnabled(false);
            copyTextFilesButton.setEnabled(false);
            lexiconsButton.setEnabled(false);
            authorsButton.setEnabled(false);
            languagesButton.setEnabled(false);

            UPButton.setEnabled(false);
            DOWNButton.setEnabled(false);
            ABCButton.setEnabled(false);
            ZYXButton.setEnabled(false);

            analyzeButton.setEnabled(false);
            exportStatisticsButton.setEnabled(false);
            exportTokenListButton.setEnabled(false);
            tokenListSearchButton.setEnabled(false);
            tokenListResetButton.setEnabled(false);
            enableEditingButton.setEnabled(false);
            disableEditingButton.setEnabled(false);

        }
    }
}
