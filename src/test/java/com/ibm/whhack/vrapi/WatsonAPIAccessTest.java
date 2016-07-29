package com.ibm.whhack.vrapi;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.whhack.vrapi.ClassifierOutput;
import com.ibm.whhack.vrapi.WatsonVRClient;

public class WatsonAPIAccessTest {
	
	WatsonVRClient client;

	@Before
	public void setUp() throws Exception {
		client = new WatsonVRClient();
	}

	@Test
	public void apiKeyTest() {
		System.out.println(client.getKey());
	}

	@Test
	public void classifierSampleReturnTest() throws JsonParseException, JsonMappingException, IOException {
		TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> map = mapper.readValue(new File("sample_classifier_output.json"), typeRef);
		ClassifierOutput out = new ClassifierOutput(map);
		System.out.println(out.getClassifierInfo());
	}
	
	@Test
	public void testStatusCheck() throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
		client.checkClassifierStatus("cataracteye_132451862", "2016-05-20");
	}
	
	@Test
	public void classifyCataractImage() throws IOException, URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		String response = client.classifyImage("/Users/vravish/Documents/SametimeFileTransfers/download.jpeg");
		System.out.println("Here is the decision:");
		System.out.println(response);
	}
}
