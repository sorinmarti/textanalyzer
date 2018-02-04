package com.sm.textanalyzer.app;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Corpus implements TreeNode {

    private String name;
    private List<CorpusCollection> collections;

    Corpus(String name) {
        this.name = name;
        collections = new ArrayList<>();
    }

    public CorpusCollection addCollection(String collection) {
        CorpusCollection fc = new CorpusCollection(this, collection);
        collections.add( fc );
        return fc;
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
    public TreeNode getChildAt(int childIndex) {
        return collections.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return collections.size();
    }

    @Override
    public TreeNode getParent() {
        return null;
    }

    @Override
    public int getIndex(TreeNode node) {
        return collections.indexOf( node );
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return collections.size()==0;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration( collections );
    }
}
