package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class LemmaLibraryItem {

	private final String lemmaName;
	private final List<String> variations;
	
	public LemmaLibraryItem(String name) {
		this.lemmaName = name;
		variations = new ArrayList<>();
	}
	
	public String getName() {
		return this.lemmaName;
	}
	
	public void addVariation(String variation) {
		if(!hasVariation(variation)) {
			this.variations.add(variation);
		}
	}
	
	boolean hasVariation(String variation) {
		return this.variations.contains(variation);
	}

	public List<String> getVariations() {
		return variations;
	}
	
	@Override
	public String toString() {
		return lemmaName;
	}
}
