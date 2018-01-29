package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.MainClass;
import com.sm.textanalyzer.app.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

class TabProjectPanel extends JPanel {

    private final ResourceBundle resourceBundle = MainClass.getResourceBundle();
	private final AnalyzerWindow parent;
	
	TabProjectPanel(final AnalyzerWindow parent) {
		this.parent = parent;
		updatePanel();
	}
	
	void updatePanel() {
		Project project = TextLibrary.getInstance().getProjectFile();
		
		removeAll();
		if(project == null) {
			setLayout( new FlowLayout(FlowLayout.CENTER) );
			JLabel lblNoProject = new JLabel(resourceBundle.getString("no.open.project.create.or.open.a.project.to.continue"));
			add( lblNoProject );
		}
		else {
			setLayout( new BorderLayout() );
			
			JPanel pnlInformation = new JPanel( new FlowLayout( FlowLayout.CENTER) );
			String txt;
			if( project.getProjectFile()==null ) {
				txt = resourceBundle.getString("the.project.has.not.been.saved.yet");
			}
			else {
				txt = "Opened project: "+project.getProjectFile();
			}
			JLabel lblNoProject = new JLabel(txt);
			pnlInformation.add( lblNoProject );
			add( pnlInformation, BorderLayout.NORTH );
			
			JPanel pnlFiles = new JPanel( new BorderLayout() );
			final JLabel lblPath = new JLabel( "" );
			final JButton btnDel = new JButton(resourceBundle.getString("delete.path.from.corpus"));
			final JButton btnLemma = new JButton(resourceBundle.getString("load.lemma.library1"));
			
			pnlFiles.setBorder( BorderFactory.createTitledBorder(resourceBundle.getString("added.corpus.paths")) );
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
			
			JLabel lbl1 = new JLabel(resourceBundle.getString("path"));
			c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.LINE_END;
			panel.add(lbl1, c);
			
			c.anchor = GridBagConstraints.LINE_START;
			
			// lblPath
			c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lblPath, c);
			
			JButton btnAdd = new JButton(resourceBundle.getString("add.new.path"));
			btnAdd.addActionListener(e -> {
                File file = parent.showOpenDialog(resourceBundle.getString("open.corpus.path"), resourceBundle.getString("no.corpus.file.selected"), parent.textFileFilter, false);
                if(file != null) {
                    if( !FileUtils.fileExists( file ) ) {
                        parent.showMessage(resourceBundle.getString("the.selected.file.does.not.exist"));
                    }
                    else if( FileUtils.fileExistsInProjectList( TextLibrary.getInstance().getProjectFile(), file ) ) {
                        parent.showMessage(resourceBundle.getString("the.file.already.exists.in.the.project"));
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
			
			JLabel lbl3 = new JLabel(resourceBundle.getString("adds.a.new.file.for.analysis.to.the.project"));
			c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lbl3, c);
			
			JButton btnAddFolder = new JButton(resourceBundle.getString("add.new.path.directory"));
			btnAddFolder.addActionListener(e -> {
                File file = parent.showOpenDialog(resourceBundle.getString("open.corpus.directory"), resourceBundle.getString("no.directory.selected"), parent.folderFileFilter, true);
                if(file != null) {
                    List<Path> filesToOpen = FileUtils.collectFilenames( file.getAbsolutePath() );
                    if(filesToOpen.size()>0) {
                        int addedFiles = 0;
                        int ignoredFiles = 0;
                        StringBuilder str = new StringBuilder("<html>Not added:<br/>");
                        for(Path p : filesToOpen) {
                            if( !FileUtils.fileExists( p ) ) {
                                str.append(p.getFileName().toString()).append(" doesn't exist.<br/>");
                                ignoredFiles++;
                            }
                            else if( FileUtils.fileExistsInProjectList( TextLibrary.getInstance().getProjectFile(), p ) ) {
                                str.append(p.getFileName().toString()).append(" is already part of the project.<br/>");
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
                            str.append("There have been ").append(ignoredFiles).append(" ignored files and ").append(addedFiles).append(" added files.");
                            parent.showMessage(str.toString());
                        }
                    }
                }
            });
			c.gridx = 0; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.BOTH;
			panel.add(btnAddFolder, c);
			
			JLabel lbl7 = new JLabel(resourceBundle.getString("adds.new.files.for.analysis"));
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
			
			JLabel lbl4 = new JLabel(resourceBundle.getString("deletes.the.selected.path.from.the.project"));
			c.gridx = 1; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lbl4, c);
			
			// btn lemma
			btnLemma.addActionListener(e -> {
                //TODO
                File file = parent.showOpenDialog(resourceBundle.getString("load.lemma.library2"), resourceBundle.getString("no.library.loaded"), parent.lemmaFileFilter, false);
                if(file != null) {
                    try {
                        List<LemmaLibraryItem> list = ProjectFileManager.readLemmaList( file );
                        project.setLemmaFileName( file.toPath() );
                        TextLibrary.getInstance().setLemmaLibrary( list );
                        parent.resetLemmaPanel();
                    } catch (Exception e1) {
                        parent.showMessage(resourceBundle.getString("the.library.could.not.be.loaded"));
                    }
                }
            });
			c.gridx = 0; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.BOTH;
			panel.add(btnLemma, c);
			
			JLabel lbl5 = new JLabel(resourceBundle.getString("loads.a.lemma.library"));
			c.gridx = 1; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .6f; c.fill = GridBagConstraints.BOTH;
			panel.add(lbl5, c);
			
			JButton btnRead = new JButton(resourceBundle.getString("analyze.added.paths"));
			btnRead.addActionListener(e -> {
                JDialog dialog = new JDialog(parent, resourceBundle.getString("loading1"), true);
                JLabel lbl = new JLabel(resourceBundle.getString("loading"));
                dialog.add(lbl);
                JProgressBar bar = new JProgressBar();
                bar.setIndeterminate(true);
                //dialog.add(bar);
                dialog.pack();
                dialog.setLocationRelativeTo( parent );

                new Thread(() -> {
                    TextLibrary.getInstance().resetFormattedFiles();

                    for(Path path : TextLibrary.getInstance().getProjectFile().getProjectTextFiles()) {
                        FormattedFile file = new FormattedFile(path);
                        file.setFileNumber(TextLibrary.getInstance().getFileNumber());
                        lbl.setText("Reading file:"+file.getFilename());
                        try {
                            file.readFile();
                        } catch(NoSuchFileException e12) {
                            // TODO error handling
                        }
                        lbl.setText("Reading file:"+file.getFilename()+"...Done!");
                        TextLibrary.getInstance().addFile( file );
                    }

                    lbl.setText(resourceBundle.getString("get.merged.file"));
                    TextLibrary.getInstance().getMergedFile();

                    lbl.setText(resourceBundle.getString("resetting.ui"));
                    parent.resetCorpusPanels();

                    dialog.setVisible(false);
                    dialog.dispose();
                }).start();


                dialog.setVisible(true);
            });
			c.gridx = 0; c.gridy = 5; c.gridwidth = 1; c.gridheight = 1;
			c.weightx = .4f; c.fill = GridBagConstraints.BOTH;
			panel.add(btnRead, c);
			
			JLabel lbl6 = new JLabel(resourceBundle.getString("reads.the.added.paths.and.analyzes.them"));
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
