package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class TokenChain {

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
