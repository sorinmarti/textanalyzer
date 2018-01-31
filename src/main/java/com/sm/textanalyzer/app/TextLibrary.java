package com.sm.textanalyzer.app;

import com.sm.textanalyzer.MainClass;

import java.nio.file.Path;
import java.util.*;

public class TextLibrary {

	private final ResourceBundle resourceBundle = MainClass.getResourceBundle();
	private Project projectFile = null;
	
	private static TextLibrary instance = null;
	private List<FormattedFile> files;
	private FormattedFile mergedFile = null;
	private List<LemmaLibraryItem> lemmaItems;
	
	private int fileNumber;
	
	private TextLibrary() {
		files = new ArrayList<>();
		lemmaItems = new ArrayList<>();
		fileNumber = 0;
		
		lemmaItems.sort(Comparator.comparing(LemmaLibraryItem::getName));
	}
	
	public void resetFormattedFiles() {
		files = new ArrayList<>();
		mergedFile = null;
		
	}
	
	public Project getProjectFile() {
		return projectFile;
	}

	public void setProjectFile(Project projectFile) {
		this.projectFile = projectFile;
		for(Path p : projectFile.getProjectTextFiles()) {
			System.out.println( p.toString() );
		}
	}
	
	public void addFile(FormattedFile file) {
		files.add(file);
		fileNumber++;
	}
	
	public FormattedFile getFile(int index) {
		return files.get(index);
	}
	
	public int getNumFiles() {
		return files.size();
	}
	
	public void setLemmaLibrary(List<LemmaLibraryItem> items) {
		this.lemmaItems = items;
	}
	
	public List<LemmaLibraryItem> getLemmaLibrary() {
		return lemmaItems;
	}
	
	public LemmaLibraryItem[] getLemmaLibraryArray() {
		return lemmaItems.toArray( new LemmaLibraryItem[lemmaItems.size()] );
	}
	
	public List<Lemma> getLemmas(FormattedFile file) {
		List<Lemma> lemmas = new ArrayList<>();
		// For each type in file:
		for(Type type : file.getCleanTypes()) {
			// Search through the lemma items to see if the type can be added to one
			for(LemmaLibraryItem baseItem : lemmaItems) {
				// If a matching lemma for the type is found:
				if( baseItem.hasVariation( type.getWord()) ) {
					Lemma lemmaToModify = getExistingLemma(lemmas, baseItem.getName() );
					// See if the lemma exists in the lemma list
					if( lemmaToModify != null ) {
						lemmaToModify.addType(type);
						//System.out.println("Found existing lemma "+lemmaToModify.getName()+" ("+lemmaToModify.getNumTypes()+") and added type "+type.getWord());
					} else {
						// If no: add it
						lemmaToModify = new Lemma( baseItem.getName() );
						lemmaToModify.addType(type);
						lemmas.add(lemmaToModify);
						//System.out.println("New lemma "+lemmaToModify.getName()+" ("+lemmaToModify.getNumTypes()+") and added type "+type.getWord());
					}
				}
			} // end for each lemma library item
		} // end for each type
		
		return lemmas;
	}
	
	boolean isInTypeList(String word) {
		// Search through the lemma items to see if the type can be added to one
		for(LemmaLibraryItem baseItem : lemmaItems) {
			if(baseItem.hasVariation(word)) {
				return true;
			}
		}
		return false;
	}
	
	private static Lemma getExistingLemma(List<Lemma> lemmas, String name) {
		for(Lemma l : lemmas) {
			if(l.getName().equals(name)) {
				return l;
			}
		}
		return null;
	}

	public static TextLibrary getInstance() {
		if(instance==null) {
			instance = new TextLibrary();
		}
		return instance;
	}

	public FormattedFile getMergedFile() {
		if(mergedFile==null) {
			mergedFile = new FormattedFile( files );
		}
		return mergedFile;
	}

	public int getFileNumber() {
		return fileNumber;
	}
	
	public String[][] getStatisticsArray() {
		if( getNumFiles()>0 ) {
			FormattedFile merged = getMergedFile();
			FormattedFile mostTokens = getMostTokens();
			FormattedFile leastTokens = getLeastTokens();
			FormattedFile mostChars = getMostChars();
			FormattedFile leastChars = getLeastChars();
			
			return new String[][]{ {resourceBundle.getString("number.of.files"),                   String.valueOf(  getNumFiles() )},
								 {resourceBundle.getString("number.of.types"),                     String.valueOf( merged.getCleanTypes().size())},
								 {resourceBundle.getString("number.of.tokens"),                    String.valueOf( merged.getTokens().size() )},
					             {resourceBundle.getString("type.token.ratio"),                 String.valueOf( merged.getTypeTokenRatio() )},
					             {resourceBundle.getString("longest.text.token"),            String.valueOf( mostTokens.getFilename()+ " ("+mostTokens.getNumTokens()+")" )},
					             {resourceBundle.getString("shortest.text.token"),           String.valueOf( leastTokens.getFilename()+ " ("+leastTokens.getNumTokens()+")" )},
					             {resourceBundle.getString("average.text.length.tokens"),  String.valueOf( getAverageTokens() )},
					             {resourceBundle.getString("median.text.length.tokens"),        String.valueOf( getMedianTokens() )},
					             {resourceBundle.getString("longest.text.characters"),          String.valueOf( mostChars.getFilename()+ " ("+mostChars.getText().length()+")" )},
					             {resourceBundle.getString("shortest.text.characters"),         String.valueOf( leastChars.getFilename()+ " ("+leastChars.getText().length()+")" )},
					             {resourceBundle.getString("average.text.length.characters"), String.valueOf( getAverageChars() )},
					             {resourceBundle.getString("median.text.length.characters"),       String.valueOf( getMedianChars() )},
					             {resourceBundle.getString("number.of.lemmas"),                   String.valueOf( getLemmaLibrary().size() )},
					             {resourceBundle.getString("number.of.types.in.lemmas"),          String.valueOf( getNumberOfLemmaTypes() )},
					             {resourceBundle.getString("lemmas.with.one.type.only"),       String.valueOf( getNumberOfLemmasWithOnlyOneType() )},
					             {resourceBundle.getString("lemmas.without.type"),               String.valueOf( getNumberOfLemmasWithNoType() )}
								 };
		}
		return new String[][]{};
	}

	private int getNumberOfLemmaTypes() {
		int num = 0;
		for(LemmaLibraryItem item : getLemmaLibrary()) {
			num += item.getVariations().size();
		}
		return num;
	}

	private int getNumberOfLemmasWithNoType() {
		int num = 0;
		for(LemmaLibraryItem item : getLemmaLibrary()) {
			if(item.getVariations().size()==0) {
				num++;
			}
		}
		return num;
	}
	
	private int getNumberOfLemmasWithOnlyOneType() {
		int num = 0;
		for(LemmaLibraryItem item : getLemmaLibrary()) {
			if(item.getVariations().size()==1) {
				num++;
			}
		}
		return num;
	}
	
	private FormattedFile getMostTokens() {
		FormattedFile mostTokens = null;
		for(FormattedFile f : files) {
			if(mostTokens==null || mostTokens.getNumTokens()<f.getNumTokens()) {
				mostTokens = f;
			}
		}
		return mostTokens;
	}
	
	private FormattedFile getLeastTokens() {
		FormattedFile leastTokens = null;
		for(FormattedFile f : files) {
			if(leastTokens==null || leastTokens.getNumTokens()>f.getNumTokens()) {
				leastTokens = f;
			}
		}
		return leastTokens;
	}
	
	private int getAverageTokens() {
		int sum = 0; 
		for(FormattedFile f : files) {
			sum += f.getNumTokens();
		}
		return (sum/files.size());
	}
	
	private double getMedianTokens() {
		List<Integer> tokens = new ArrayList<>(); 
		for(FormattedFile f : files) {
			tokens.add(f.getNumTokens());
		}
		Collections.sort(tokens);
		double median = 0;
		if(tokens.size()>1) {
			if (tokens.size() % 2 == 0) {
				median = (
						(double)tokens.get(tokens.size()/2) + 
						(double)tokens.get((tokens.size()/2) - 1)
						)/2;
			} else {
				median = (double) tokens.get(tokens.size()/2);
			}
		}
		return median;
	}
	
	private FormattedFile getMostChars() {
		FormattedFile mostChars = null;
		for(FormattedFile f : files) {
			if(mostChars==null || mostChars.getText().length()<f.getText().length()) {
				mostChars = f;
			}
		}
		return mostChars;
	}
	
	private FormattedFile getLeastChars() {
		FormattedFile leastChars = null;
		for(FormattedFile f : files) {
			if(leastChars==null || leastChars.getText().length()>f.getText().length()) {
				leastChars = f;
			}
		}
		return leastChars;
	}
	
	private int getAverageChars() {
		int sum = 0; 
		for(FormattedFile f : files) {
			sum += f.getText().length();
		}
		return (sum/files.size());
	}
	
	private double getMedianChars() {
		List<Integer> chars = new ArrayList<>(); 
		for(FormattedFile f : files) {
			chars.add(f.getText().length());
		}
		Collections.sort(chars);
		double median;
		if (chars.size() % 2 == 0) {
			median = (
					(double)chars.get(chars.size()/2) + 
					(double)chars.get((chars.size()/2) - 1)
					)/2;
		} else {
			median = (double) chars.get(chars.size()/2);
		}
		return median;
	}

	public void addLemmaItem(LemmaLibraryItem lemmaLibraryItem) {
		lemmaItems.add(lemmaLibraryItem);
		lemmaItems.sort(Comparator.comparing(LemmaLibraryItem::getName));
	}
	
	/*
	public String getCorpusLemmaExportString() {
		StringBuilder export = new StringBuilder();
		List<Lemma> lemmas = getLemmas( mergedFile );
		for(Lemma lemma : lemmas) {
			export.append(lemma.getName()).append(" (").append(lemma.getNumTypes()).append("): [");
			for(Type type : lemma.getTypes()) {
				export.append(type.getWord()).append("(").append(type.getNumberOfOccurrences()).append("/").append(type.getNumberOfDifferentFiles()).append("), ");
			}
			if(lemma.getTypes().size()>0) {
				export = new StringBuilder(export.substring(0, export.length() - 2));
			}
			export.append("]\n");
		}
		return export.toString();
	}
	 */
	
	/*
	public String getCorpusTypesExportString() {
		StringBuilder export = new StringBuilder();
		int addedFiles = 0;
		for(Type type : mergedFile.getCleanTypes()) {
			if(type.getOccurrences().size()>1 || type.isGrouped()) {	// Type exists more than one time OR is in lemma library
				export.append(type.getWord()).append(" (").append(type.getOccurrences().size()).append("), ");
				addedFiles++;
			}
		}
		if(addedFiles>0) {
			export = new StringBuilder(export.substring(0, export.length() - 2));
		}
		return export.toString();
	}
	 */
	
	/*
	public String getLemmaLibraryExportString() {
		StringBuilder export = new StringBuilder();
		for(LemmaLibraryItem item : getLemmaLibrary()) {
			export.append(item.getName()).append(": ");
			for(String variation :  item.getVariations()) {
				export.append(variation).append(", ");
			}
			if(item.getVariations().size()>0) {
				export = new StringBuilder(export.substring(0, export.length() - 2));
			}
		}
		
		return export.toString();
	}
	 */
}
