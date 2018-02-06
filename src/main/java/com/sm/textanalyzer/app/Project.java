package com.sm.textanalyzer.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Project {

	private File projectFile;
	private Corpus corpus;
	private List<Author> authors;
	
	public Project( ) {
		this.projectFile = null;
		corpus = new Corpus( "Corpus" );
		authors = new ArrayList<>();
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

	public void addAuthor(Author author) {
	    authors.add( author );
    }

    public List<Author> getAuthors() {
	    return authors;
    }
}
