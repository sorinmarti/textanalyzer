package com.sm.textanalyzer.app;

import javax.swing.tree.TreeNode;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class CorpusCollection implements TreeNode {

    private Corpus parent;
    private String collectionName;
    private List<CorpusFile> corpusFiles;

    CorpusCollection(Corpus parent, String collectionName) {
        this.parent = parent;
        this.collectionName = collectionName;
        corpusFiles = new ArrayList<>();
    }

    public CorpusFile addFile(String name) {
        CorpusFile file = new CorpusFile(this, name);
        corpusFiles.add( file );
        return file;
    }

    public CorpusFile addFile(String name, Path path) {
        CorpusFile file = new CorpusFile(this, name, path);
        corpusFiles.add( file );
        return file;
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
    public TreeNode getChildAt(int childIndex) {
        return corpusFiles.get( childIndex );
    }

    @Override
    public int getChildCount() {
        return corpusFiles.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return corpusFiles.indexOf( node );
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return corpusFiles.size()==0;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration( corpusFiles );
    }
}
