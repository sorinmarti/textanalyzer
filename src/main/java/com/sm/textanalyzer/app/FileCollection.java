package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class FileCollection {

    private String collectionName;
    private List<CorpusFile> corpusFiles;

    FileCollection(String collectionName) {
        this.collectionName = collectionName;
        corpusFiles = new ArrayList<>();
    }

    public void addFile(CorpusFile file) {
        corpusFiles.add( file );
    }

    public String getName() {
        return collectionName;
    }

    public int getNumFiles() {
        return corpusFiles.size();
    }

    public CorpusFile getFile(int index) {
        return corpusFiles.get( index );
    }
}
