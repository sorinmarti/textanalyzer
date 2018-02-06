package com.sm.textanalyzer.app;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CorpusFile {

    private enum FileState {
        ADDED(),
        PROCESSED(),
        NEEDS_UPDATE()
    }

    private String name;
    private CorpusFileType fileType;
    private FileState fileState;
    private Author author;
    private Date fileDate;
    private Path originalSavePath;
    private List<TokenChain> tokenChains;

    public CorpusFile(String name) {
        this(name, null);
    }

    public CorpusFile(String name, Path savePath) {
        this.name = name;

        this.fileType = CorpusFileType.ALL;
        this.fileState = FileState.ADDED;

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

    public String getName() {
        return name;
    }

    public void setAuthor(Author author) {
        propertyChanged();
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public void setName(String name) {
        propertyChanged();
        this.name = name;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileDateString() {
        if(fileDate==null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(fileDate);
    }

    private void propertyChanged() {
        if(fileState==FileState.PROCESSED) {
            fileState = FileState.NEEDS_UPDATE;
        }
    }

    public CorpusFileType getFileType() {
        return fileType;
    }

    public void setFileType(CorpusFileType fileType) {
        propertyChanged();
        this.fileType = fileType;
    }
}
