package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class TokenChain {

    private List<Token> tokens;
    private String author;
    private String date;

    TokenChain() {
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
}
