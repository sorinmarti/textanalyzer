package com.sm.textanalyzer.app;

public class AnalysisSettings {

    private String tokenDelimiter;

    AnalysisSettings() {
        tokenDelimiter = ", ";
    }

    public String getTokenDelimiter() {
        return tokenDelimiter;
    }

    public void setTokenDelimiter(String tokenDelimiter) {
        this.tokenDelimiter = tokenDelimiter;
    }
}
