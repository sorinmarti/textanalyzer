package com.sm.textanalyzer.app;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FormattedFileUtils {
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortReverseByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue( Collections.reverseOrder() ))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}

	public static <K extends Comparable<? super K>, V> Map<K,V> sortByKey(Map<K,V> map) {
	    return new TreeMap<>(map);
	}

}
