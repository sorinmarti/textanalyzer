package com.sm.textanalyzer.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

	public static List<Path> collectFilenames(String baseDir) {
		List<Path> filePaths = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get(baseDir))) {
		    paths
		        .filter( isTextFile() )
		        .forEach( filePaths::add );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return filePaths;
	}
	
	public static Predicate<Path> isTextFile() {
        return p -> p.getFileName().toString().endsWith("txt") && !p.getName( p.getNameCount()-1 ).toString().contains("test");
    }

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}

	public static boolean fileExists(File file) {
		return file != null && fileExists(file.toPath());
	}

	public static boolean fileExists(Path path) {
		return path != null && Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
	}
	
	public static boolean fileExistsInProjectList(Project project, File file) {
		return file != null && fileExistsInProjectList(project, file.toPath());
	}
	
	public static boolean fileExistsInProjectList(Project project, Path path) {
		if(project==null || path==null) {
			throw new IllegalArgumentException("Project and path can't be null");
		}
		for(Path projectPath : project.getProjectTextFiles()) {
			if(projectPath.equals(path)) {
				return true;
			}
		}
		return false;
	}
	
}
