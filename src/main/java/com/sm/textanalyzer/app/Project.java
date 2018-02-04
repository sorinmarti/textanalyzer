package com.sm.textanalyzer.app;

import java.io.File;
import java.nio.file.Path;

public class Project {

	private File projectFile;
	private Corpus corpus;
	private Path lemmaFileName;
	
	public Project( ) {
		this.projectFile = null;
		corpus = new Corpus( "Corpus" );
		this.lemmaFileName = null;
	}

    public Corpus getCorpus() {
		return corpus;
	}

	public Path getLemmaFileName() {
		return lemmaFileName;
	}

	public void setLemmaFileName(Path lemmaFileName) {
		this.lemmaFileName = lemmaFileName;
	}

	public File getProjectFile() {
		return projectFile;
	}

	public void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}
}
