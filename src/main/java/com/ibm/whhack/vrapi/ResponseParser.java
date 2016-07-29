package com.ibm.whhack.vrapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseParser {

	static final String HEALTHY_EYE = "We think you have a healthy eye. Great Job!";
	static final String VERY_MILD = "The probability of you having a cataract is low. But to be sure to consult a Doctor";
	static final String MILD = "The probability of you having a cataract is medium. But to be sure to consult a Doctor";
	static final String SEVERE = "The probability of you having a cataract is high. But to be sure to consult a Doctor";
	
	@SuppressWarnings("unchecked")
	public static String decide(String response) throws JsonParseException, JsonMappingException, IOException {
		String message = "";
		int c = 0;
		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		HashMap<String, Object> resultMap = mapper.readValue(response, typeRef);
		
		List<HashMap<String, Object>> images = (List<HashMap<String, Object>>) resultMap.get("images");

		List<HashMap<String, Object>> classifiers = (List<HashMap<String, Object>>) images.get(0).get("classifiers");
		for (int i = 0; i < classifiers.size(); i++) {
			if ("cataract_eye".equals(classifiers.get(i).get("name"))) {
				List<HashMap<String, Object>> classes = (List<HashMap<String, Object>>) classifiers.get(i)
						.get("classes");

				if (!classes.isEmpty()) {
					double score = (double) classes.get(0).get("score");
					message = checkScore(score, "Cataract");
					c = 1;
				}

			} else if ("default".equals(classifiers.get(i).get("name")) && c == 0) {
				List<HashMap<String, Object>> classes = (List<HashMap<String, Object>>) classifiers.get(i)
						.get("classes");
				for (HashMap<String, Object> curClass : classes) {
					if ("eye".equals(curClass.get("class"))) {
						double score = (double) curClass.get("score");
						message = checkScore(score, "Normal");
						c = 2;
					}
				}
			}
		}
		return message;

	}

	private static String checkScore(double score, String s) {
		String message = "";
		if ("Normal".equals(s)) {
			message = HEALTHY_EYE;
		} else if ("Cataract".equals(s)) {
			if (score >= 0.5) {
				message = SEVERE;
			} else if (score >= 0.3 && score < 0.5) {
				message = MILD;

			} else if (score > 0.2 && score < 0.3) {
				message = VERY_MILD;
			}
		}
		return message;
	}
}
