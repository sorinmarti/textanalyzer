package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class WordType {

	private final String word;
	private final List<Occurrence> occurrences;
	
	WordType(String word) {
		this.word = word;
		this.occurrences = new ArrayList<>();
	}
	
	public String getWord() {
		return word;
	}
	
	public int getNumberOfOccurrences() {
		return occurrences.size();
	}

	void addOccurrence(int fileNumber, int tokenNumber) {
		Occurrence o = new Occurrence(fileNumber, tokenNumber);
		occurrences.add( o );
	}
	
	void addOccurrences(List<Occurrence> list) {
		occurrences.addAll(list);
	}

	public List<Occurrence> getOccurrences() {
		return occurrences;
	}

	public int getNumberOfDifferentFiles() {
		List<Integer> foundNumberOfFiles = new ArrayList<>();
		for(Occurrence o : occurrences) {
			if(!foundNumberOfFiles.contains(o.getFile())){
				foundNumberOfFiles.add(o.getFile());
			}
		}
		return foundNumberOfFiles.size();
	}

	public boolean isGrouped() {
		return TextLibrary.getInstance().isInTypeList( word );
	}
}
