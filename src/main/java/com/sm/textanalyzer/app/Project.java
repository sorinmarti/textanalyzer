package com.sm.textanalyzer.app;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Project {

	private File projectFile;
	List<Path> projectTextFiles;
	private Path lemmaFileName;
	
	public Project( ) {
		this.projectFile = null;
		this.projectTextFiles = new ArrayList<>();
		this.lemmaFileName = null;
	}
	
	public List<Path> getProjectTextFiles() {
		return projectTextFiles;
	}

	public void setProjectTextFiles(List<Path> projectTextFiles) {
		this.projectTextFiles = projectTextFiles;
	}
	
	public void addProjectTextFile(Path projectTextFile) {
		this.projectTextFiles.add( projectTextFile );
	}
	
	public void removeProjectTextFile(Path projectTextFile) {
		this.projectTextFiles.remove( projectTextFile );
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
