package com.sm.textanalyzer.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TextLibrary {

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
		
		Collections.sort( lemmaItems , (o1, o2) -> o1.getName().compareTo( o2.getName() ));
	}
	
	public void resetFormattedFiles() {
		files = new ArrayList<FormattedFile>();
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
		for(WordType type : file.getCleanTypes()) {
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
	
	public boolean isInTypeList(String word) {
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
			
			String[][] stats = { {"Anzahl Dateien",                   String.valueOf(  getNumFiles() )},
								 {"Anzahl Types",                     String.valueOf( merged.getCleanTypes().size())},
								 {"Anzahl Tokens",                    String.valueOf( merged.getTokens().size() )},
					             {"Type/Token Ratio",                 String.valueOf( merged.getTypeTokenRatio() )},
					             {"L�ngster Text (Token)",            String.valueOf( mostTokens.getFilename()+ " ("+mostTokens.getNumTokens()+")" )},
					             {"K�rzester Text (Token)",           String.valueOf( leastTokens.getFilename()+ " ("+leastTokens.getNumTokens()+")" )},
					             {"Durchschnitt Textl�nge (Tokens)",  String.valueOf( getAverageTokens() )},
					             {"Median Textl�nge (Tokens)",        String.valueOf( getMedianTokens() )},
					             {"L�ngster Text (Zeichen)",          String.valueOf( mostChars.getFilename()+ " ("+mostChars.getText().length()+")" )},
					             {"K�rzester Text (Zeichen)",         String.valueOf( leastChars.getFilename()+ " ("+leastChars.getText().length()+")" )},
					             {"Durchschnitt Textl�nge (Zeichen)", String.valueOf( getAverageChars() )},
					             {"Median Textl�nge (Zeichen)",       String.valueOf( getMedianChars() )},
					             {"Anzahl Lemmata",                   String.valueOf( getLemmaLibrary().size() )},
					             {"Anzahl Types in Lemmata",          String.valueOf( getNumberOfLemmaTypes() )},
					             {"Lemmata mit nur einem Type",       String.valueOf( getNumberOfLemmasWithOnlyOneType() )},
					             {"Lemmata ohne Types",               String.valueOf( getNumberOfLemmasWithNoType() )}
								 };
			return stats;
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
		double median = 0;
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
		Collections.sort( lemmaItems , (o1, o2) -> o1.getName().compareTo( o2.getName() ));
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCorpusLemmaExportString() {
		String export = "";
		List<Lemma> lemmas = getLemmas( mergedFile );
		for(Lemma lemma : lemmas) {
			export += lemma.getName() + " ("+lemma.getNumTypes()+"): [";
			for(WordType type : lemma.getTypes()) {
				export += type.getWord() + "("+type.getNumberOfOccurences()+"/"+type.getNumberOfDifferentFiles()+"), ";
			}
			if(lemma.getTypes().size()>0) {
				export = export.substring(0, export.length()-2);
			}
			export += "]\n";
		}
		return export;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCorpusTypesExportString() {
		String export = "";
		int addedFiles = 0;
		for(WordType type : mergedFile.getCleanTypes()) {
			if(type.getOccurences().size()>1 || type.isGrouped()) {	// Type exists more than one time OR is in lemma library
				export += type.getWord() + " ("+type.getOccurences().size()+"), ";
				addedFiles++;
			}
		}
		if(addedFiles>0) {
			export = export.substring(0, export.length()-2);
		}
		return export;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLemmaLibraryExportString() {
		String export = "";
		for(LemmaLibraryItem item : getLemmaLibrary()) {
			export += item.getName()+": ";
			for(String variation :  item.getVariations()) {
				export += variation + ", ";
			}
			if(item.getVariations().size()>0) {
				export = export.substring(0, export.length()-2);
			}
		}
		
		return export;
	}
}
