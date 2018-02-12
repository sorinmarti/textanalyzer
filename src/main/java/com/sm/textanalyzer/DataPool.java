package com.sm.textanalyzer;

import com.sm.textanalyzer.app.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataPool {

    public static Project project;

    public static void createProject() {
        project = new Project();
    }

    public static boolean projectOpen() {
        return project!=null;
    }

    public static void saveProject(File file) throws Exception {
        project.setProjectFile(file);
        ProjectFileManager.writeProjectFile(file, project);
        //ProjectFileManager.writeLemmaList(project.getLemmaFileName().toFile(), TextLibrary.getInstance().getLemmaLibrary());
    }

    public static Corpus getCorpus() {
        if(DataPool.projectOpen()) {
            return project.getCorpus();
        }
        return null;
    }

    public static List<Language> getLanguages() {
        if(projectOpen()) {
            return project.getLanguages();
        }
        return new ArrayList<>();
    }

    public static List<Author> getAuthors() {
        if(projectOpen()) {
            return project.getAuthors();
        }
        return new ArrayList<>();
    }
}
