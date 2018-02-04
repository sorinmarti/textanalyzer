package com.sm.textanalyzer.app;

import javax.swing.tree.TreeNode;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class CorpusFile implements TreeNode {

    private CorpusCollection parent;
    private String name;
    private Path originalSavePath;
    private List<TokenChain> tokenChains;

    public CorpusFile(CorpusCollection parent, String name) {
        this(parent, name, null);
    }

    public CorpusFile(CorpusCollection parent, String name, Path savePath) {
        this.parent = parent;
        this.name = name;
        this.originalSavePath = savePath;
        tokenChains = new ArrayList<>();
    }

    public TokenChain addTokenChain() {
        TokenChain chain = new TokenChain(this );
        tokenChains.add( chain );
        return chain;
    }

    public int getNumTokenChains() {
        return tokenChains.size();
    }

    public int getNumTokens() {
        int numTokens = 0;
        for(TokenChain chain : tokenChains) {
            numTokens += chain.getNumTokens();
        }
        return numTokens;
    }

    public TokenChain getTokenChain(int index) {
        return tokenChains.get( index );
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return tokenChains.get( childIndex );
    }

    @Override
    public int getChildCount() {
        return tokenChains.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return tokenChains.indexOf( node );
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return tokenChains.size()==0;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration( tokenChains );
    }

    @Override
    public String toString() {
        return this.name;
    }
}
