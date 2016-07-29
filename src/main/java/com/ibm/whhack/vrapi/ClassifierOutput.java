package com.ibm.whhack.vrapi;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ClassifierOutput {
	private HashMap<String, Object> resultMap;
	
	static class OneClass {
		String theClass;
		double score;

		public OneClass(String theClass, double score) {
			this.theClass = theClass;
			this.score = score;
		}

		@Override
		public String toString() {
			return "{theClass=" + theClass + ", score=" + score + "}";
		}
	}

	public ClassifierOutput(HashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<OneClass> getClassifierInfo() {
		List<HashMap<String, Object>> images = (List<HashMap<String, Object>>) resultMap.get("images");
		List<HashMap<String, Object>> classifiers = (List<HashMap<String, Object>>) images.get(0).get("classifiers");
		List<HashMap<String, Object>> classes = (List<HashMap<String, Object>>) classifiers.get(0).get("classes");
		
		
		List<OneClass> parsedClasses = classes.stream()
				.map(mappedClass -> 
						new OneClass((String) mappedClass.get("class"), (Integer) mappedClass.get("score")))
				.collect(Collectors.toList());
		return parsedClasses;
	}
}
