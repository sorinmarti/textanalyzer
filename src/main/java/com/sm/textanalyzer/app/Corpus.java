package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class Corpus {

    private String name;
    private List<FileCollection> collections;

    Corpus(String name) {
        this.name = name;
        collections = new ArrayList<>();
    }

    public void addCollection(FileCollection collection) {
        collections.add( collection );
    }

    public String getName() {
        return name;
    }

    public int getNumCollections() {
        return collections.size();
    }

    public FileCollection getCollection(int index) {
        return collections.get( index );
    }
}
