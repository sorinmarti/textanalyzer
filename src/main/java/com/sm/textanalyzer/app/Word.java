package com.sm.textanalyzer.app;

public class Word {

	private String word;
	private int beginIndex = 0;
	private String sortingWord;
	
	public Word(String word) {
		this.word = word;
		this.sortingWord = word;
	}
	
	public Word(Word word) {
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
	
	public void setSortingWord(String word) {
		this.sortingWord = word;
	}
	
	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex; 
	}
	
	public int getBeginIndex() {
		return beginIndex;
	}
}
