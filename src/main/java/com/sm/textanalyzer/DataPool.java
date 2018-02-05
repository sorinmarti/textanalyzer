package com.sm.textanalyzer;

import com.sm.textanalyzer.app.Project;

public class DataPool {

    public static Project project;

    public static void createProject() {
        project = new Project();
    }

    public static boolean projectOpen() {
        return project!=null;
    }
}
