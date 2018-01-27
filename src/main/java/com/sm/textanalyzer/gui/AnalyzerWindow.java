package com.sm.textanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.TextAction;

import com.sm.textanalyzer.MainClass;
import com.sm.textanalyzer.app.Project;
import com.sm.textanalyzer.app.ProjectFileManager;
import com.sm.textanalyzer.app.TextLibrary;

public class AnalyzerWindow extends JFrame {

	/**
	 * 10.12.17
	 */
	private static final long serialVersionUID = 1493151096342595821L;
	private JPanel contentPane;
	
	private JMenuItem mnitProjectSave;
	private JMenuItem mnitProjectSaveAs;
	//private JMenu mnuEdit;
	
	// TABS
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private TabProjectPanel           projectPanel        = new TabProjectPanel( this );
	private FormattedFilePanel        combinedCorpusPanel = new FormattedFilePanel(this, TextLibrary.getInstance().getMergedFile(), true);
	private TabSingleCorpusFilesPanel singleFilesPanel    = new TabSingleCorpusFilesPanel(this);
	private TabLemmaLibraryPanel      lemmaPanel          = new TabLemmaLibraryPanel();
	
	final JFileChooser fileChooser = new JFileChooser();
	final FileNameExtensionFilter projectFileFilter = new FileNameExtensionFilter("Projekt-Dateien", "xml");
	final FileNameExtensionFilter lemmaFileFilter = new FileNameExtensionFilter("Lemma-Bibliothek", "xml");
	final FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Textdateien", "txt");
	final FileFilter folderFileFilter = new FileFilter() {
		
		@Override
		public String getDescription() {
			return "Ordner";
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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		initializeMenuBar();
		
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Projekt",          new ImageIcon("res/img/tab_project.png"), projectPanel,        "Details zum momentan ge�ffneten Projekt.");			// TAB: Project
		tabbedPane.addTab("Gesamtkorpus",     new ImageIcon("res/img/tab_corpus.png"),  combinedCorpusPanel, "Auswertung des gesamten geladenen Korpus");			// TAB: Complete Corpus
		tabbedPane.addTab("Einzelne Dateien", new ImageIcon("res/img/tab_project.png"), singleFilesPanel,    "Zeigt die einzelnen Korpusdateien mit Auswertung.");
		tabbedPane.addTab("Lemma-Bibliothek", new ImageIcon("res/img/tab_lemmas.png"),  lemmaPanel,          "Zeigt die geladene Lemma-Bibliothek.");
		
		fireProjectClosed();
	}
	
	protected void resetCorpusPanels() {
		combinedCorpusPanel = new FormattedFilePanel(this,TextLibrary.getInstance().getMergedFile(), true);
		tabbedPane.setComponentAt(1, combinedCorpusPanel);
		
		singleFilesPanel    = new TabSingleCorpusFilesPanel(this);
		tabbedPane.setComponentAt(2, singleFilesPanel);
	}

	protected void resetLemmaPanel() {
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
		JMenu mnuFile = new JMenu("Datei");
		JMenuItem mnitProjectNew = new JMenuItem("Neues Projekt");
		mnitProjectNew.addActionListener(arg0 -> {
            if( projectIsOpen() ) {
                // TODO Wanna save old project?
                showMessage("Ein Projekt ist noch ge�ffnet.");
            }
            else {
                Project project = new Project();
                TextLibrary.getInstance().setProjectFile( project );
                projectHasChanged();
                fireProjectOpened();
            }
        });
		JMenuItem mnitProjectOpen = new JMenuItem("Projekt �ffnen...");
		mnitProjectOpen.addActionListener(e -> {
            File file = showOpenDialog("Projekt �ffnen", "Keine Datei angew�hlt", projectFileFilter, false);

            if( file != null) {
                Project project = null;
                try {
                    project = ProjectFileManager.readProjectFile(file);
                } catch (Exception e1) {
                    showMessage("Die Projektdatei konnte nicht geladen werden.");
                    return;
                }
                TextLibrary.getInstance().setProjectFile(project);

                if(project.getLemmaFileName()!=null) {
                    try {
                        TextLibrary.getInstance().setLemmaLibrary( ProjectFileManager.readLemmaList( project.getLemmaFileName().toFile() ) );
                    } catch (Exception e1) {
                        showMessage("Lemma-Bibliothek konnte nicht geladen werden.");
                        return;
                    }
                }

                fireProjectOpened();
            }
        });
		mnitProjectSave = new JMenuItem("Projekt speichern");
		mnitProjectSave.addActionListener(e -> {
            if(TextLibrary.getInstance().getProjectFile().getProjectFile()==null) {
                File file = showSaveDialog("Projekt speichern unter...", "Der Speichervorgang wurde abgebrochen", projectFileFilter);
                if(file!=null) {
                    try {
                        saveProject( file );
                        showMessage("Projekt gespeichert.");
                        fireProjectSaved();
                    } catch (Exception e1) {
                        showMessage("Das Projekt konnte nicht gespeichert werden.");
                    }
                }
            }
            else {
                try {
                    saveProject( TextLibrary.getInstance().getProjectFile().getProjectFile() );
                    showMessage("Projekt gespeichert.");
                    fireProjectSaved();
                } catch (Exception e1) {
                    showMessage("Das Projekt konnte nicht gespeichert werden.");
                }

            }
        });
		mnitProjectSave.setEnabled(false);
		
		mnitProjectSaveAs = new JMenuItem("Projekt speichern unter...");
		mnitProjectSaveAs.addActionListener(e -> {
            File file = showSaveDialog("Projekt speichern unter...", "Der Speichervorgang wurde abgebrochen", projectFileFilter);
            if(file!=null) {
                try {
                    saveProject( file );
                    showMessage("Projekt gespeichert.");
                    fireProjectSaved();
                } catch (Exception e1) {
                    showMessage("Das Projekt konnte nicht gespeichert werden.");
                }
            }
        });
		mnitProjectSaveAs.setEnabled( false );
		
		JMenuItem mnitCloseApp = new JMenuItem("Beenden");
		mnitCloseApp.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(AnalyzerWindow.this, "Wollen Sie wirklich beenden? Ungespeicherte Daten gehen verloren.", "Programm beenden", JOptionPane.YES_NO_CANCEL_OPTION);
            if(answer==JOptionPane.YES_OPTION) {
                System.exit( 0 );
            }
        });
		
		mnuFile.add(mnitProjectNew);
		mnuFile.add(mnitProjectOpen);
		mnuFile.addSeparator();
		mnuFile.add(mnitProjectSave);
		mnuFile.add(mnitProjectSaveAs);
		mnuFile.addSeparator();
		mnuFile.add(mnitCloseApp);
		
		//////////////////////////////////////////////
		// Menu Help
		//////////////////////////////////////////////
		JMenu mnuHelp = new JMenu("Hilfe");
		JMenuItem mnitAbout = new JMenuItem("�ber "+MainClass.APP_TITLE);
		mnitAbout.addActionListener(e -> showMessage("<html>"+MainClass.APP_TITLE+"<br/>"+
                    "Version "+MainClass.APP_VERSION+"</html>"));
		JMenuItem mnitHelp = new JMenuItem("Hilfe");
		mnitHelp.addActionListener(e -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                URI oURL = new URI(MainClass.HELP_URL);
                desktop.browse(oURL);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
		
		mnuHelp.add(mnitAbout);
		mnuHelp.addSeparator();
		mnuHelp.add(mnitHelp);
		
		//////////////////////////////////////////////
		// Menu Bar
		menuBar.add(mnuFile);                                 
		//menuBar.add(mnuEdit);
		menuBar.add(mnuHelp);
		
		setJMenuBar(menuBar);
	}

	private boolean projectIsOpen() {
		return TextLibrary.getInstance().getProjectFile() != null;
	}
	
	protected void projectHasChanged() {
		projectChanged = true;
		
		projectPanel.updatePanel();
		setWindowTitle();
	}

	private void fireProjectSaved() {
		projectChanged = false;
		setWindowTitle();
	}
	
	private void fireProjectOpened() {
		mnitProjectSave.setEnabled( true );		// Enable save
		mnitProjectSaveAs.setEnabled( true );	// Enable save As
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
		mnitProjectSave.setEnabled( false );	// Enable save
		mnitProjectSaveAs.setEnabled( false );	// Enable save As
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
		String string = "";
		if(TextLibrary.getInstance().getProjectFile().getProjectFile()==null) {
			// The opened project was not yet saved
			string = "[ungespeichertes Projekt]";
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
	 * TODO
	 * @param message
	 */
	protected void showMessage(String message) {
		JOptionPane.showMessageDialog(AnalyzerWindow.this, message);
	}
	
	/**
	 * TODO
	 * @param title
	 * @param cancelMsg
	 * @param filter
	 * @param foldersOnly
	 * @return
	 */
	protected File showOpenDialog(String title, String cancelMsg, FileFilter filter, boolean foldersOnly) {
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
            File file = fileChooser.getSelectedFile();
            return file;
        }
		else {
			showMessage(cancelMsg);
		}
		return null;
	}
	
	/**
	 * TODO
	 * @param title
	 * @param cancelMsg
	 * @param filter
	 * @return
	 */
	private File showSaveDialog(String title, String cancelMsg, FileFilter filter) {
		fileChooser.setDialogTitle(title);
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
