package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class CorpusFile {

    private List<TokenChain> tokenChains;

    CorpusFile() {
        tokenChains = new ArrayList<>();
    }

    public void addTokenChain(TokenChain chain) {
        tokenChains.add( chain );
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
}
