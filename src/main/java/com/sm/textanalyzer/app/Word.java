package com.sm.textanalyzer.app;

public class Word {

	private final String word;
	private int beginIndex = 0;
	private String sortingWord;
	
	Word(String word) {
		this.word = word;
		this.sortingWord = word;
	}
	
	Word(Word word) {
		this.word = word.getWord();
		this.beginIndex = word.getBeginIndex();
		this.sortingWord = word.getSortingWord();
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
