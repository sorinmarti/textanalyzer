package com.sm.textanalyzer.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CorpusCollection {

    private String collectionName;
    private CorpusFileType acceptedType;

    private List<CorpusFile> corpusFiles;

    public CorpusCollection(String collectionName) {
        this.collectionName = collectionName;
        corpusFiles = new ArrayList<>();
        acceptedType = CorpusFileType.ALL;
    }

    public CorpusFile addFile(String name) {
        CorpusFile file = new CorpusFile( name);
        corpusFiles.add( file );
        return file;
    }

    public CorpusFile addFile(String name, Path path) {
        CorpusFile file = new CorpusFile( name, path);
        corpusFiles.add( file );
        return file;
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

    @Override
    public String toString() {
        return this.collectionName;
    }

    public int getFileIndex(CorpusFile child) {
        return corpusFiles.indexOf( child );
    }

    public List<CorpusFile> getFiles() {
        return corpusFiles;
    }

    public void removeFile(CorpusFile file) {
        corpusFiles.remove(file);
    }

    public void setName(String name) {
        this.collectionName = name;
    }

    public CorpusFileType getAcceptedType() {
        return this.acceptedType;
    }

    public void setAcceptedType(CorpusFileType acceptedType) {
        this.acceptedType = acceptedType;
    }
}
