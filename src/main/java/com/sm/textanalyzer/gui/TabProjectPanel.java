package com.sm.textanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sm.textanalyzer.app.FileUtils;
import com.sm.textanalyzer.app.FormattedFile;
import com.sm.textanalyzer.app.FormattedFileUtils;
import com.sm.textanalyzer.app.LemmaLibraryItem;
import com.sm.textanalyzer.app.Project;
import com.sm.textanalyzer.app.ProjectFileManager;
import com.sm.textanalyzer.app.TextLibrary;

public class TabProjectPanel extends JPanel {
	
	private final AnalyzerWindow parent;
	
	public TabProjectPanel(final AnalyzerWindow parent) {
		this.parent = parent;
		updatePanel();
	}
	
	public void updatePanel() {
		Project project = TextLibrary.getInstance().getProjectFile();
		
		removeAll();
		if(project == null) {
			setLayout( new FlowLayout(FlowLayout.CENTER) );
			JLabel lblNoProject = new JLabel("Es ist kein Projekt ge�ffnet. Erstellen oder �ffnen Sie ein Projekt um fortzufahren.");
			add( lblNoProject );
		}
		else {
			setLayout( new BorderLayout() );
			
			JPanel pnlInformation = new JPanel( new FlowLayout( FlowLayout.CENTER) );
			String txt = "";
			if( project.getProjectFile()==null ) {
				txt = "Das Projekt wurde bisher noch nicht gespeichert.";
			}
			else {
				txt = "Es ist ein Projekt ge�ffnet: "+project.getProjectFile();
			}
			JLabel lblNoProject = new JLabel(txt);
			pnlInformation.add( lblNoProject );
			add( pnlInformation, BorderLayout.NORTH );
			
			JPanel pnlFiles = new JPanel( new BorderLayout() );
			final JLabel lblPath = new JLabel( "" );
			final JButton btnDel = new JButton( "Ausgew�hlten Pfad l�schen" );
			final JButton btnLemma = new JButton( "Lemma-Bibliothek einlesen." );
			
			pnlFiles.setBorder( BorderFactory.createTitledBorder("Hinzugef�gte Dateipfade") );
			JList<Path> addedPathsList = new JList<>();
			addedPathsList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
			addedPathsList.setModel(new PathListModel( project.getProjectTextFiles() ));
			addedPathsList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
                DefaultListCellRenderer def = new DefaultListCellRenderer();
                JLabel renderer = (JLabel) def.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setText( value.getFileName().toString() );
                return renderer;
            });
			addedPathsList.addListSelectionListener(listSelectionEvent -> {
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
                if (!adjust && addedPathsList.getSelectedValue()!=null) {
                    //System.out.println("Selected: "+addedPathsList.getSelectedIndices()[0]);
                    btnDel.setEnabled(true);
                    lblPath.setText( addedPathsList.getSelectedValue().toAbsolutePath().toString() );
                }
            });
			
			JScrollPane listScrollPane = new JScrollPane( addedPathsList );
			listScrollPane.setMinimumSize( new Dimension(200, 350));
			listScrollPane.setPreferredSize( new Dimension(200, 350));
			pnlFiles.add( listScrollPane, BorderLayout.WEST );
			// --> right component of list
			GridBagLayout layout = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			JPanel panel = new JPanel( layout );
			
			c.anchor = GridBagConstraints.LINE_START;
			c.ipadx = 5; c.ipady = 5;
			c.insets = new Insets(5, 5, 5, 5);
			
			JLabel lbl1 = new JLabel( "Pfad:" );
			c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.LINE_END;
			panel.add(lbl1, c);
			
			c.anchor = GridBagConstraints.LINE_START;
			
			// lblPath
			c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lblPath, c);
			
			JButton btnAdd = new JButton( "Neuen Pfad hinzuf�gen" );
			btnAdd.addActionListener(e -> {
                File file = parent.showOpenDialog("Korpusdatei �ffnen", "Keine Datei ausgew�hlt", parent.textFileFilter, false);
                if(file != null) {
                    if( !FileUtils.fileExists( file ) ) {
                        parent.showMessage("Die Datei existiert nicht.");
                        return;
                    }
                    else if( FileUtils.fileExistsInProjectList( TextLibrary.getInstance().getProjectFile(), file ) ) {
                        parent.showMessage("Die Datei existiert bereits im Projekt.");
                        return;
                    }
                    else {
                        TextLibrary.getInstance().getProjectFile().addProjectTextFile( file.toPath() );
                        btnLemma.setEnabled( true );	// At least one file is present: activate lemma library
                        parent.projectHasChanged();
                    }
                }
            });
			c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.BOTH;
			panel.add(btnAdd, c);
			
			JLabel lbl3 = new JLabel( "F�gt dem Projekt eine neue Datei zur Auswertung hinzu." );
			c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lbl3, c);
			
			JButton btnAddFolder = new JButton( "Neuen Pfad-Ordner hinzuf�gen" );
			btnAddFolder.addActionListener(e -> {
                File file = parent.showOpenDialog("Korpusordner �ffnen", "Kein Ordner ausgew�hlt", parent.folderFileFilter, true);
                if(file != null) {
                    List<Path> filesToOpen = FileUtils.collectFilenames( file.getAbsolutePath() );
                    if(filesToOpen.size()>0) {
                        int addedFiles = 0;
                        int ignoredFiles = 0;
                        String str = "<html>Nicht hinzugef�gt:<br/>";
                        for(Path p : filesToOpen) {
                            if( !FileUtils.fileExists( p ) ) {
                                str += p.getFileName().toString() + " existiert nicht.<br/>";
                                ignoredFiles++;
                            }
                            else if( FileUtils.fileExistsInProjectList( TextLibrary.getInstance().getProjectFile(), p ) ) {
                                str += p.getFileName().toString() + " ist bereits im Projekt.<br/>";
                                ignoredFiles++;
                            }
                            else {
                                addedFiles++;
                                TextLibrary.getInstance().getProjectFile().addProjectTextFile( p );
                            }
                        }
                        if(addedFiles>0) {
                            parent.projectHasChanged();
                        }
                        if(ignoredFiles>0) {
                            str += "Es wurden "+ignoredFiles+" Dateien �bersprungen und "+addedFiles+" Dateien hinzugef�gt.";
                            parent.showMessage( str );
                        }
                    }
                }
            });
			c.gridx = 0; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.BOTH;
			panel.add(btnAddFolder, c);
			
			JLabel lbl7 = new JLabel( "F�gt dem Projekt neue Dateien zur Auswertung hinzu." );
			c.gridx = 1; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lbl7, c);
			
			// Btn del
			btnDel.setEnabled( false );
			btnDel.addActionListener(e -> {

                //TextLibrary.getInstance().getProjectFile().removeProjectTextFile( addedPathsList.getSelectedValue() );
                ((PathListModel) addedPathsList.getModel()).removePath( addedPathsList.getSelectedValue() );
                addedPathsList.clearSelection();
                if(addedPathsList.getModel().getSize()==0) {
                    btnLemma.setEnabled( false );
                }
                lblPath.setText( "" );
                btnDel.setEnabled(false);
            });
			c.gridx = 0; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.BOTH;
			panel.add(btnDel, c);
			
			JLabel lbl4 = new JLabel( "L�scht den gew�hlten Dateipfad aus dem Projekt." );
			c.gridx = 1; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lbl4, c);
			
			// btn lemma
			btnLemma.addActionListener(e -> {
                //TODO
                File file = parent.showOpenDialog("Lemma Bibliothek laden", "Keine Bibliothek geladen.", parent.lemmaFileFilter, false);
                if(file != null) {
                    try {
                        List<LemmaLibraryItem> list = ProjectFileManager.readLemmaList( file );
                        project.setLemmaFileName( file.toPath() );
                        TextLibrary.getInstance().setLemmaLibrary( list );
                        parent.resetLemmaPanel();
                    } catch (Exception e1) {
                        parent.showMessage("Die Bibliothek konnte nicht geladen werden.");
                    }
                }
            });
			c.gridx = 0; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.BOTH;
			panel.add(btnLemma, c);
			
			JLabel lbl5 = new JLabel( "L�dt eine Lemma-Bibliothek" );
			c.gridx = 1; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lbl5, c);
			
			JButton btnRead = new JButton( "Hinzugef�gte Pfade einlesen." );
			btnRead.addActionListener(e -> {
                JDialog dialog = new JDialog(parent, "Loading", true);
                JLabel lbl = new JLabel( "          Loading          ");
                dialog.add(lbl);
                JProgressBar bar = new JProgressBar();
                bar.setIndeterminate(true);
                //dialog.add(bar);
                dialog.pack();
                dialog.setLocationRelativeTo( parent );

                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        TextLibrary.getInstance().resetFormattedFiles();

                        for(Path path : TextLibrary.getInstance().getProjectFile().getProjectTextFiles()) {
                            FormattedFile file = new FormattedFile(path);
                            file.setFileNumber(TextLibrary.getInstance().getFileNumber());
                            lbl.setText("Reading file:"+file.getFilename());
                            try {
                                file.readFile();
                            } catch(NoSuchFileException e) {
                                // TODO error handling
                            }
                            lbl.setText("Reading file:"+file.getFilename()+"...Done!");
                            TextLibrary.getInstance().addFile( file );
                        }

                        lbl.setText("Get merged file.");
                        TextLibrary.getInstance().getMergedFile();

                        lbl.setText("Resetting UI.");
                        parent.resetCorpusPanels();

                        dialog.setVisible(false);
                        dialog.dispose();
                    }
                }).start();


                dialog.setVisible(true);
                String s = TextLibrary.getInstance().getCorpusTypesExportString();
                System.out.println( s );
            });
			c.gridx = 0; c.gridy = 5; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.BOTH;
			panel.add(btnRead, c);
			
			JLabel lbl6 = new JLabel( "Liest die ausgew�hlten Textdateien ein." );
			c.gridx = 1; c.gridy = 5; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lbl6, c);
			
			JPanel enclosingPanel = new JPanel( new FlowLayout( FlowLayout.LEFT) );
			enclosingPanel.add( panel );
			pnlFiles.add(enclosingPanel, BorderLayout.CENTER);
			
			add( pnlFiles, BorderLayout.CENTER );
		}
	}
}
