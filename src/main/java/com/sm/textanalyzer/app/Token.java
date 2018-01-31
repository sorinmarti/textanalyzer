package com.sm.textanalyzer.app;

public class Token {

	private final String word;
	private int beginIndex = 0;
	private String sortingWord;
	
	Token(String word) {
		this.word = word;
		this.sortingWord = word;
	}
	
	Token(Token token) {
		this.word = token.getWord();
		this.beginIndex = token.getBeginIndex();
		this.sortingWord = token.getSortingWord();
	}
	
	public String getWord() {
		return word;
	}
	
	public String getSortingWord() {
		return sortingWord;
	}
	
	void setSortingWord(String word) {
		this.sortingWord = word;
	}
	
	void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex; 
	}
	
	public int getBeginIndex() {
		return beginIndex;
	}
}
