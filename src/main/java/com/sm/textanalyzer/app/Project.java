package com.sm.textanalyzer.app;

import java.io.File;

public class Project {

	private File projectFile;
	private Corpus corpus;
	
	public Project( ) {
		this.projectFile = null;
		corpus = new Corpus( "Corpus" );
	}

    public Corpus getCorpus() {
		return corpus;
	}

	public File getProjectFile() {
		return projectFile;
	}

	public void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}
}
