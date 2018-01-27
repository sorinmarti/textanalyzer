package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class LemmaLibraryItem {

	private final String lemmaName;
	private List<String> variations;
	
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
	
	public boolean hasVariation(String variation) {
		return this.variations.contains(variation);
	}

	public Lemma createLemma() {
		return new Lemma( lemmaName );
	}

	public List<String> getVariations() {
		return variations;
	}
	
	@Override
	public String toString() {
		return lemmaName;
	}
}
