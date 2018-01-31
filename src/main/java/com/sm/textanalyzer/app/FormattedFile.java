package com.sm.textanalyzer.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("HardCodedStringLiteral")
public class FormattedFile {

	private final Path savedSource;
	private String text;
	private String cleanText;
	private final List<Token> tokens;
	private final List<Token> cleanTokens;
	private final List<Type> types;
	private final List<Type> cleanTypes;
	private int fileNumber;
	
	FormattedFile(List<FormattedFile> files) {
		this((Path)null);
				
		for(FormattedFile file : files) {
			int textLength = this.text.length();
			int cleanTextLength = this.cleanText.length();
			
			this.text += file.getText();
			this.cleanText += file.getCleanText();
			 
			for(Token token : file.getTokens()) {
				Token clonedToken = new Token( token );
				clonedToken.setBeginIndex(token.getBeginIndex()+textLength);
				tokens.add(clonedToken);
			}
			for(Token token : file.getCleanTokens()) {
				Token clonedToken = new Token( token );
				clonedToken.setBeginIndex(token.getBeginIndex()+cleanTextLength);
				cleanTokens.add(clonedToken);
			}
			
			
			for(Type type : file.getTypes()) {
				Type existing = getExistingType( type.getWord() );
				existing.addOccurrences(type.getOccurrences());
			}
			
			for(Type type : file.getCleanTypes()) {
				Type existing = getExistingCleanType( type.getWord() );
				existing.addOccurrences(type.getOccurrences());
			}
		}
	}
	
	public FormattedFile(Path path) {
		this.savedSource = path;
		this.text = "";
		this.cleanText = "";
		tokens = new ArrayList<>();
		cleanTokens = new ArrayList<>();
		types = new ArrayList<>();
		cleanTypes = new ArrayList<>();
	}
	
	public void readFile() throws NoSuchFileException {
		if(savedSource==null) {
			throw new NoSuchFileException("null");
		}
		try (Stream<String> stream = Files.lines(savedSource, StandardCharsets.ISO_8859_1)) {
			stream.forEach((string) -> {
				// For each line
				String[] words = string.split(" ");
				for(String word : words) {
					// For each word (=character sequence separated by space) 
					processWord(word);
				}
				// After each line
				// 1.) Add line break
				text = text.substring(0, text.length()-1);
				text += "\n";
				// 2.) Add line break
				cleanText = cleanText.substring(0, cleanText.length()-1);
				cleanText += "\n";
			});
			// After reading
			// 1.) remove additional space at the end
			if(text.length()>0) {
				text = text.substring(0, text.length()-1);
			}
			// 2.) remove additional space at the end
			if(cleanText.length()>0) {
				cleanText = cleanText.substring(0, cleanText.length()-1);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void processWord(String word) {
		if(word==null || word.isEmpty()) {
			return;
		}
		int beginIndex = text.length();
		int cleanBeginIndex = cleanText.length();
		
		// 1.) Create text
		text += word + " ";
		
		word = prepareWord(word);
		
		// 2.) Create word without special characters
		String strippedWord = removeSpecialCharacters( word );
		if(!strippedWord.isEmpty()) {
			// 2.) If the word was not only special characters:
			// --> Add token
			Token tokenObj = new Token(word);
			tokenObj.setBeginIndex(beginIndex);
			tokenObj.setSortingWord(strippedWord);
			tokens.add(tokenObj);
			
			// --> Add clean token
			Token cleanWordObj = new Token( strippedWord );
			cleanWordObj.setBeginIndex(cleanBeginIndex);
			cleanTokens.add( cleanWordObj );
		}
		
		// 4.) Create 'clean' text
		cleanText += strippedWord + " ";
		
		// 5.) Add token to type list
		Type savedType = getExistingType( word );
		savedType.addOccurrence(fileNumber, tokens.size()-1);
		
		// 6.) Add clean token to clean type list
		if(!strippedWord.isEmpty()) {
			Type savedCleanType = getExistingCleanType( strippedWord );
			savedCleanType.addOccurrence(fileNumber, cleanTokens.size()-1);
		}
	}

	private Type getExistingType(String name) {
		for(Type type : types) {
			if(type.getWord().equals(name)) {
				return type;
			}
		}
		Type newType = new Type(name);
		types.add(newType);
		return newType;
	}
	
	private Type getExistingCleanType(String name) {
		for(Type type : cleanTypes) {
			if(type.getWord().equals(name)) {
				return type;
			}
		}
		Type newType = new Type(name);
		cleanTypes.add(newType);
		return newType;
	}

	private String prepareWord(String word) {
		return word.replaceAll("[,.\\?\\!\\:\\;]", "");
	}
	
	private String removeSpecialCharacters(String word) {
		String formattedWord = word.replaceAll("[0-9]", "");	// Numbers
		formattedWord = formattedWord.replaceAll("'", "");	// Numbers
		formattedWord = formattedWord.replaceAll("-", "");	// 
		formattedWord = formattedWord.replaceAll("[\\(\\)]", "");
		formattedWord = formattedWord.replaceAll("[\\\"\\\"]", "");
		formattedWord = formattedWord.replaceAll("[/]", "");
		
		formattedWord = formattedWord.replaceAll("[\\%\\.\\,\"\\(\\)\\-\\#]", "");
		
		
		formattedWord = formattedWord.replaceAll("[òó]", "o");
		formattedWord = formattedWord.replaceAll("[ùú]", "u");
		formattedWord = formattedWord.replaceAll("[éè]", "e");
		formattedWord = formattedWord.replaceAll("[àá]", "a");
		formattedWord = formattedWord.replaceAll("[íì]", "i");
		
		//formattedWord = formattedWord.replaceAll("\\P{Print}", "");
		formattedWord = formattedWord.toLowerCase();
		return formattedWord;
	}

	public Path getPath() {
		return savedSource;
	}
	
	public String getFilename() {
		String filename = "";
		if(savedSource!=null) {
			filename = savedSource.getName( savedSource.getNameCount()-2 ).toString()+"/"+savedSource.getFileName().toString();
		}
		return filename;
	}
	public String getText() {
		return text;
	}
	
	public String getCleanText() {
		return cleanText;
	}
	
	public List<Token> getTokens() {
		return tokens;
	}
	
	public List<Token> getCleanTokens() {
		return cleanTokens;
	}
	
	public List<Type> getTypes() {
		return types;
	}
	
	public List<Type> getCleanTypes() {
		return cleanTypes;
	}
	
	public String getContext(int word, int wordsBefore, int wordsAfter) {
		return getAnyContext(tokens, word, wordsBefore, wordsAfter);
	}

	public String getCleanContext(int word, int wordsBefore, int wordsAfter) {
		return getAnyContext(cleanTokens, word, wordsBefore, wordsAfter);
	}
	
	private String getAnyContext(List<Token> tokenList, int word, int wordsBefore, int wordsAfter) {
		String result = "["+ tokenList.get(word).getWord()+"]";
		
		for(int i=1;i<=wordsAfter;i++) {
			if(tokenList.size()>((word+i))) {
				result += " " + tokenList.get( word + i ).getWord();
			}
			else {
				result += " [EOF]";
				break;
			}
		}
		
		for(int i=(word-1);i>=(word-wordsBefore);i--) {
			if(i>=0) {
				result = tokenList.get( i ).getWord() + " " + result;
			}
			else {
				result = "[BOF] "+result;
				break;
			}
		}
		
		return result;
	}

	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}

	float getTypeTokenRatio() {
		return ((float)getCleanTypes().size()/(float)getTokens().size());
	}

	int getNumTokens() {
		return tokens.size();
	}

	/*
	public int getNumCleanTokens() {
		return cleanTokens.size();
	}
	
	public int getNumTypes() {
		return types.size();
	}
	
	public int getNumCleanTypes() {
		return cleanTypes.size();
	}
	*/
}
