package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class WordType {

	private String word;
	private List<Occurence> occurences;
	
	public WordType(String word) {
		this.word = word;
		this.occurences = new ArrayList<>();
	}
	
	public WordType(String word, Integer numberOfOccurences) {
		this( word );
	}
	
	public String getWord() {
		return word;
	}
	
	public int getNumberOfOccurences() {
		return occurences.size();
	}

	public void addOccurence(int fileNumber, int tokenNumber) {
		Occurence o = new Occurence(fileNumber, tokenNumber);
		occurences.add( o );
	}
	
	public void addOccurences(List<Occurence> list) {
		occurences.addAll(list);
	}

	public List<Occurence> getOccurences() {
		return occurences;
	}

	public int getNumberOfDifferentFiles() {
		List<Integer> foundNums = new ArrayList<>();
		for(Occurence o : occurences) {
			if(!foundNums.contains(o.getFile())){
				foundNums.add(o.getFile());
			}
		}
		return foundNums.size();
	}

	public boolean isGrouped() {
		return TextLibrary.getInstance().isInTypeList( word );
	}
}
