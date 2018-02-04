package com.sm.textanalyzer.app;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TokenChain implements TreeNode {

    private CorpusFile parent;
    private List<Token> tokens;
    private String author;
    private String date;

    TokenChain(CorpusFile parent) {
        this.parent = parent;
        tokens = new ArrayList<>();
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public Token getToken(int i) {
        return tokens.get(i);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public int getNumTokens() {
        return tokens.size();
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return 0;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Enumeration children() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Token t : tokens) {
            builder.append(t.getWord()).append(" ");
            if(builder.length()>20) {
                builder.append("...");
                break;
            }
        }
        return builder.toString();
    }
}
