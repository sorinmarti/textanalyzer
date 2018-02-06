package com.sm.textanalyzer;

import com.sm.textanalyzer.app.Project;
import com.sm.textanalyzer.app.ProjectFileManager;

import java.io.File;

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
}
