package com.sm.textanalyzer.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CorpusFile {

    private String name;
    private Path originalSavePath;
    private List<TokenChain> tokenChains;

    public CorpusFile(String name) {
        this(name, null);
    }

    public CorpusFile(String name, Path savePath) {
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
    public String toString() {
        return this.name;
    }

    public Path getPath() {
        return originalSavePath;
    }
}
