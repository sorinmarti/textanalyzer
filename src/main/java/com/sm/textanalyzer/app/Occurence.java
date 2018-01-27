package com.sm.textanalyzer.app;

public class Occurence {

	private int file;
	private int token;
	
	public Occurence(int fileNumber, int tokenNumber) {
		this.file = fileNumber;
		this.token = tokenNumber;
	}
	
	public int getFile() {
		return file;
	}
	
	public void setFile(int file) {
		this.file = file;
	}
	
	public int getToken() {
		return token;
	}
	
	public void setToken(int token) {
		this.token = token;
	}
}
