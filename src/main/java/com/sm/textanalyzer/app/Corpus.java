package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class Corpus {

    private String name;
    private List<CorpusCollection> collections;

    Corpus(String name) {
        this.name = name;
        collections = new ArrayList<>();
    }

    public void addCollection(String collection) {
        CorpusCollection fc = new CorpusCollection(collection);
        collections.add( fc );
    }

    public void addCollection(CorpusCollection collection) {
        collections.add( collection );
    }

    public String getName() {
        return name;
    }

    public int getNumCollections() {
        return collections.size();
    }

    public CorpusCollection getCollection(int index) {
        return collections.get( index );
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getCollectionIndex(CorpusCollection child) {
        return collections.indexOf( child );
    }

    public void removeCollection(CorpusCollection collection) {
        collections.remove(collection);
    }

    public void setName(String name) {
        this.name = name;
    }
}
