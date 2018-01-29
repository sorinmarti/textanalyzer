package com.sm.textanalyzer.app;

import java.util.ArrayList;
import java.util.List;

public class Lemma {

	private final String lemmaName;
	private final List<WordType> typesOfLemma;
	
	Lemma(String name) {
		this.lemmaName = name;
		typesOfLemma = new ArrayList<>();
	}
	
	public String getName() {
		return this.lemmaName;
	}
	
	void addType(WordType type) {
		this.typesOfLemma.add(type);
	}

	/*
	public int getNumTypes() {
		return typesOfLemma.size();
	}
	*/
	
	@Override
	public boolean equals(Object obj) {
        return obj != null && obj instanceof Lemma && lemmaName.equals(obj);
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
