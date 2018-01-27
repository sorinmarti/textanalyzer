package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class Lemma {

	private final String lemmaName;
	private List<WordType> typesOfLemma;
	
	public Lemma(String name) {
		this.lemmaName = name;
		typesOfLemma = new ArrayList<>();
	}
	
	public String getName() {
		return this.lemmaName;
	}
	
	public void addType(WordType type) {
		this.typesOfLemma.add(type);
	}
	
	public int getNumTypes() {
		return typesOfLemma.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		return lemmaName.equals(obj);
	}

	@Override
	public int hashCode() {
		return lemmaName.hashCode();
	}

	@Override
	public String toString() {
		return lemmaName;
	}

	public List<WordType> getTypes() {
		return typesOfLemma;
	}
}
