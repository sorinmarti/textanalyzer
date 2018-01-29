package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.MainClass;
import com.sm.textanalyzer.app.Project;
import com.sm.textanalyzer.app.ProjectFileManager;
import com.sm.textanalyzer.app.TextLibrary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.ResourceBundle;

public class AnalyzerWindow extends JFrame {

	private final ResourceBundle resourceBundle = MainClass.getResourceBundle();

	private JMenuItem menuItemProjectSave;
	private JMenuItem menuItemProjectSaveAs;
	//private JMenu mnuEdit;
	
	// TABS
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private final TabProjectPanel           projectPanel        = new TabProjectPanel( this );
	private FormattedFilePanel        combinedCorpusPanel = new FormattedFilePanel(this, TextLibrary.getInstance().getMergedFile(), true);
	private TabSingleCorpusFilesPanel singleFilesPanel    = new TabSingleCorpusFilesPanel(this);
	private TabLemmaLibraryPanel      lemmaPanel          = new TabLemmaLibraryPanel();

	private final JFileChooser fileChooser = new JFileChooser();
	private final FileNameExtensionFilter projectFileFilter = new FileNameExtensionFilter(resourceBundle.getString("project.files"), "xml");
	final FileNameExtensionFilter lemmaFileFilter = new FileNameExtensionFilter(resourceBundle.getString("lemma.library"), "xml");
	final FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter(resourceBundle.getString("text.files"), "txt");
	final FileFilter folderFileFilter = new FileFilter() {
		
		@Override
		public String getDescription() {
			return resourceBundle.getString("directories");
		}
		
		@Override
		public boolean accept(File arg0) {
			return arg0 != null && arg0.isDirectory();
		}
	};
	
	private boolean projectChanged = false;
	
	/**
	 * Create the frame.
	 */
	public AnalyzerWindow() {
		setTitle( MainClass.APP_TITLE );
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		initializeMenuBar();
		
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab(resourceBundle.getString("project"),         new ImageIcon("res/img/tab_project.png"), projectPanel, resourceBundle.getString("details.concerning.the.opened.project"));            // TAB: Project NON-NLS
		tabbedPane.addTab(resourceBundle.getString("complete.corpus"), new ImageIcon("res/img/tab_corpus.png"),  combinedCorpusPanel, resourceBundle.getString("analysis.of.the.opened.corpus"));			// TAB: Complete Corpus
		tabbedPane.addTab(resourceBundle.getString("single.files"),    new ImageIcon("res/img/tab_project.png"), singleFilesPanel, resourceBundle.getString("shows.the.single.corpus.files.analysis"));
		tabbedPane.addTab(resourceBundle.getString("lemma.library"),   new ImageIcon("res/img/tab_lemmas.png"),  lemmaPanel, resourceBundle.getString("shows.the.opened.lemma.library"));
		
		fireProjectClosed();
	}
	
	void resetCorpusPanels() {
		combinedCorpusPanel = new FormattedFilePanel(this,TextLibrary.getInstance().getMergedFile(), true);
		tabbedPane.setComponentAt(1, combinedCorpusPanel);
		
		singleFilesPanel    = new TabSingleCorpusFilesPanel(this);
		tabbedPane.setComponentAt(2, singleFilesPanel);
	}

	void resetLemmaPanel() {
		lemmaPanel = new TabLemmaLibraryPanel();
		tabbedPane.setComponentAt(3, lemmaPanel);
	}
	
	/**
	 * Initializes the menu bar
	 */
	private void initializeMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		//////////////////////////////////////////////
		// Menu file
		//////////////////////////////////////////////
		JMenu mnuFile = new JMenu(resourceBundle.getString("file"));
		JMenuItem menuItemProjectNew = new JMenuItem(resourceBundle.getString("new.project"));
		menuItemProjectNew.addActionListener(arg0 -> {
            if( projectIsOpen() ) {
                // TODO Wanna save old project?
                showMessage(resourceBundle.getString("a.project.is.still.open"));
            }
            else {
                Project project = new Project();
                TextLibrary.getInstance().setProjectFile( project );
                projectHasChanged();
                fireProjectOpened();
            }
        });
		JMenuItem menuItemProjectOpen = new JMenuItem(resourceBundle.getString("open.project"));
		menuItemProjectOpen.addActionListener(e -> {
            File file = showOpenDialog(resourceBundle.getString("open.project1"), resourceBundle.getString("no.project.file.selected"), projectFileFilter, false);

            if( file != null) {
                Project project;
                try {
                    project = ProjectFileManager.readProjectFile(file);
                } catch (Exception e1) {
                    showMessage(resourceBundle.getString("the.project.file.could.not.ne.read"));
                    return;
                }
                TextLibrary.getInstance().setProjectFile(project);

                if(project.getLemmaFileName()!=null) {
                    try {
                        TextLibrary.getInstance().setLemmaLibrary( ProjectFileManager.readLemmaList( project.getLemmaFileName().toFile() ) );
                    } catch (Exception e1) {
                        showMessage(resourceBundle.getString("the.lemma.library.could.not.be.read"));
                        return;
                    }
                }

                fireProjectOpened();
            }
        });
		menuItemProjectSave = new JMenuItem(resourceBundle.getString("save.project"));
		menuItemProjectSave.addActionListener(e -> {
            if(TextLibrary.getInstance().getProjectFile().getProjectFile()==null) {
                File file = showSaveDialog(resourceBundle.getString("aborted.saving.process"), projectFileFilter);
                if(file!=null) {
                    try {
                        saveProject( file );
                        showMessage(resourceBundle.getString("project.saved"));
                        fireProjectSaved();
                    } catch (Exception e1) {
                        showMessage(resourceBundle.getString("the.project.could.not.be.saved"));
                    }
                }
            }
            else {
                try {
                    saveProject( TextLibrary.getInstance().getProjectFile().getProjectFile() );
                    showMessage(resourceBundle.getString("project.saved"));
                    fireProjectSaved();
                } catch (Exception e1) {
                    showMessage(resourceBundle.getString("the.project.could.not.be.saved"));
                }

            }
        });
		menuItemProjectSave.setEnabled(false);
		
		menuItemProjectSaveAs = new JMenuItem(resourceBundle.getString("save.project.as"));
		menuItemProjectSaveAs.addActionListener(e -> {
            File file = showSaveDialog(resourceBundle.getString("aborted.saving.process"), projectFileFilter);
            if(file!=null) {
                try {
                    saveProject( file );
                    showMessage(resourceBundle.getString("project.saved"));
                    fireProjectSaved();
                } catch (Exception e1) {
                    showMessage(resourceBundle.getString("the.project.could.not.be.saved"));
                }
            }
        });
		menuItemProjectSaveAs.setEnabled( false );
		
		JMenuItem menuItemCloseApp = new JMenuItem(resourceBundle.getString("exit"));
		menuItemCloseApp.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(AnalyzerWindow.this, resourceBundle.getString("do.you.really.want.to.exit.unsaved.data.is.lost"), resourceBundle.getString("exit.program"), JOptionPane.YES_NO_CANCEL_OPTION);
            if(answer==JOptionPane.YES_OPTION) {
                System.exit( 0 );
            }
        });
		
		mnuFile.add(menuItemProjectNew);
		mnuFile.add(menuItemProjectOpen);
		mnuFile.addSeparator();
		mnuFile.add(menuItemProjectSave);
		mnuFile.add(menuItemProjectSaveAs);
		mnuFile.addSeparator();
		mnuFile.add(menuItemCloseApp);
		
		//////////////////////////////////////////////
		// Menu Help
		//////////////////////////////////////////////
		JMenu menuHelp = new JMenu(resourceBundle.getString("help"));
		JMenuItem menuItemAbout = new JMenuItem("About "+MainClass.APP_TITLE);
		menuItemAbout.addActionListener(e -> showMessage("<html>"+MainClass.APP_TITLE+"<br/>"+
                    "Version "+MainClass.APP_VERSION+"</html>"));
		JMenuItem menuItemHelp = new JMenuItem("Help");
		menuItemHelp.addActionListener(e -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                URI oURL = new URI(MainClass.HELP_URL);
                desktop.browse(oURL);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
		
		menuHelp.add(menuItemAbout);
		menuHelp.addSeparator();
		menuHelp.add(menuItemHelp);
		
		//////////////////////////////////////////////
		// Menu Bar
		menuBar.add(mnuFile);                                 
		//menuBar.add(mnuEdit);
		menuBar.add(menuHelp);
		
		setJMenuBar(menuBar);
	}

	private boolean projectIsOpen() {
		return TextLibrary.getInstance().getProjectFile() != null;
	}
	
	void projectHasChanged() {
		projectChanged = true;
		
		projectPanel.updatePanel();
		setWindowTitle();
	}

	private void fireProjectSaved() {
		projectChanged = false;
		setWindowTitle();
	}
	
	private void fireProjectOpened() {
		menuItemProjectSave.setEnabled( true );		// Enable save
		menuItemProjectSaveAs.setEnabled( true );	// Enable save As
		//mnuEdit.setEnabled( true );				// Enable Options menu
		
		projectPanel.updatePanel();
		
		tabbedPane.setEnabledAt(1, true);
		tabbedPane.setEnabledAt(2, true);
		tabbedPane.setEnabledAt(3, true);
		
		resetCorpusPanels();
		resetLemmaPanel();
		
		setWindowTitle();
	}
	
	private void fireProjectClosed() {
		menuItemProjectSave.setEnabled( false );	// Enable save
		menuItemProjectSaveAs.setEnabled( false );	// Enable save As
		//mnuEdit.setEnabled( false );			// Disable Options menu
		
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(3, false);
		
		setWindowTitle();
	}
	
	private void saveProject(File file) throws Exception {
		Project project = TextLibrary.getInstance().getProjectFile();
		project.setProjectFile(file);
		
		ProjectFileManager.writeProjectFile(file, project);
		ProjectFileManager.writeLemmaList(project.getLemmaFileName().toFile(), TextLibrary.getInstance().getLemmaLibrary());
	}
	
	/**
	 * TODO
	 */
	private void setWindowTitle() {
		// No project is opened
		if(TextLibrary.getInstance().getProjectFile()==null) {
			setTitle(MainClass.APP_TITLE);
			return;
		}
		
		// A project is open
		String string;
		if(TextLibrary.getInstance().getProjectFile().getProjectFile()==null) {
			// The opened project was not yet saved
			string = resourceBundle.getString("unsaved.project");
		}
		else {
			// The opened project has been saved
			string = TextLibrary.getInstance().getProjectFile().getProjectFile().getName();
		}
		
		if(projectChanged) {
			string += "*";
		}
		
		setTitle(MainClass.APP_TITLE + " " + string);
	}

	/**
	 * Shows a {@link JOptionPane} message
	 * @param message The message to show
	 */
	void showMessage(String message) {
		JOptionPane.showMessageDialog(AnalyzerWindow.this, message);
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
		
		int returnVal = fileChooser.showOpenDialog(AnalyzerWindow.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            return  fileChooser.getSelectedFile();
        }
		else {
			showMessage(cancelMsg);
		}
		return null;
	}
	
	/**
	 * Shows a file save dialog.
	 * @param cancelMsg The cancel message in case the saving gets cancelled
	 * @param filter The file filter to apply.
	 * @return The selected File.
	 */
	private File showSaveDialog(String cancelMsg, FileFilter filter) {
		fileChooser.setDialogTitle(resourceBundle.getString("save.project.as1"));
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int answer = fileChooser.showSaveDialog(AnalyzerWindow.this);
		if(answer == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		else {
			showMessage(cancelMsg);
		}
		return null;
	}
}
