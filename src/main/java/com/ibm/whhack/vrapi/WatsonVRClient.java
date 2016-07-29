package com.ibm.whhack.vrapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WatsonVRClient {
	APICredentials creds;
	ClassifierProperties props;
	ObjectMapper mapper;
	HttpClient client;
	
	public WatsonVRClient() throws Exception {
		mapper = new ObjectMapper();
		
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
		
		creds = mapper.readValue(new File("watson_credentials.json"), APICredentials.class);
		props = mapper.readValue(new File("classifier_properties.json"), ClassifierProperties.class);
		
		client = HttpClientBuilder.create().build();
	}
	
	public String getBaseAPI() {
		return creds.getCredentials().getUrl();
	}
	
	public String getKey() {
		return creds.getCredentials().getApi_key();
	}
	
	/**
	 * Sends a request to the endpoint /v3/classifiers to create a classifier.
	 * Refer to <link>https://watson-api-explorer.mybluemix.net/apis/visual-recognition-v3</link> for more information.  
	 * @param negativeExamples (FORM) ZIP data containing images that don't match
	 * @param classifierName (FORM) name of the new classifier created
	 * @param version (QUERY) release date of the API you want (e.g. 2016-05-14)
	 * @param positiveExampleClassnamesAndZips (FORM) each class you would like to create should be represented by an array like:
	 * [className, zipDataContainingPositiveExampleImages]
	 * @throws URISyntaxException from URI builder
	 * @throws ExecutionException from Jetty REST call
	 * @throws TimeoutException from Jetty REST call
	 * @throws InterruptedException from Jetty REST call
	 * @throws IOException from Jackson parser
	 * @throws JsonMappingException from Jackson parser
	 * @throws JsonParseException from Jackson parser
	 */
	public HashMap<String, Object> createClassifier(String negativeExamples, String classifierName, String version, String[]... positiveExampleClassnamesAndZips) throws URISyntaxException, InterruptedException, TimeoutException, ExecutionException, JsonParseException, JsonMappingException, IOException {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(getBaseAPI().replace("https://","")).setPath("/v3/classifiers")
			.setParameter("api_key", getKey())
			.setParameter("version", version);
		URI uri = builder.build();
		
		HttpPost post = new HttpPost(uri);
		
		MultipartEntityBuilder me = MultipartEntityBuilder.create();
		if (negativeExamples != null)
			me.addBinaryBody("negative_examples", negativeExamples.getBytes());
		me.addTextBody("name", classifierName);
		for (String[] singleClass : positiveExampleClassnamesAndZips) {
			me.addBinaryBody(singleClass[0] + "_positive_examples", singleClass[1].getBytes());
		}
		post.setEntity(me.build());
		
		HttpResponse response = client.execute(post);
		
		if (response.getStatusLine().getStatusCode() != 200)
			throw new RuntimeException("The service was unsuccessful in processing the request! HTTP: " + response.getStatusLine());
		
		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
//		Fields fields = new Fields();
//		if (negativeExamples != null)
//			fields.put("negative_examples", negativeExamples);
//		fields.put("name", classifierName);
//		for (String[] singleClass : positiveExampleClassnamesAndZips) {
//			fields.put(singleClass[0] + "_positive_examples", singleClass[1]);
//		}
//		
//		ContentResponse response = client.FORM(uri, fields);
//		if (response.getStatus() != 200)
//			throw new RuntimeException("The service was unsuccessful in processing the request! HTTP: " + response.getStatus());
		
		TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};

	    return mapper.readValue(result.toString(), typeRef);
	}
	
	/**
	 * Sends a request to the endpoint /v3/classifiers/{classifier_id} to check status of a classifier.
	 * Refer to <link>https://watson-api-explorer.mybluemix.net/apis/visual-recognition-v3</link> for more information.  
	 * @param classifierID name of the new classifier created
	 * @param version release date of the API you want (e.g. 2016-05-14)
	 * @throws IOException from Jackson parser
	 * @throws JsonMappingException from Jackson parser
	 * @throws JsonParseException from Jackson parser
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws URISyntaxException 
	 */
	public HashMap<String, Object> checkClassifierStatus(String classifierID, String version) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
//		RequestSpecification request = RestAssured.given();
//		request.basePath(getBaseAPI());
//		request.baseUri(getBaseAPI());
//		
//		request.queryParam("api_key", getKey());
//		request.queryParam("version", version);
//		request.pathParam("classifier_id", classifierID);
//		
//		Response response = request.get("/v3/classifiers/{classifier_id}");
//		
//		if (response.getStatusCode() != 200)
//			throw new RuntimeException("The service was unsuccessful in processing the request! HTTP: " + response.getStatusCode());
		
		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(getBaseAPI().replace("https://","")).setPath("/v3/classifiers/" + classifierID)
			.setParameter("api_key", getKey())
			.setParameter("version", version);
		URI uri = builder.build();
		
		HttpGet get = new HttpGet(uri);
		
//		ContentResponse response = client.GET(uri);
		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() != 200)
			throw new RuntimeException("The service was unsuccessful in processing the request! HTTP: " + response.getStatusLine());
		
	    TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};

//	    return mapper.readValue(response.getEntity().getContentAsString(), typeRef);
	    BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return mapper.readValue(result.toString(), typeRef);
	}
	
	/**
	 * Classifies the image using the REST call. This method retrieves the necessary parameters not from
	 * parameters but from the file watson_properties.json. 
	 * @param imageData String data representing the image bytes itself
	 * @return a ClassifierOutput object which contains a list of scores for each class in each classifier
	 * @throws IOException from Jackson parser
	 * @throws JsonMappingException from Jackson parser
	 * @throws JsonParseException from Jackson parser
	 * @throws URISyntaxException 
	 */
	public String classifyImage(String imageData) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
//		RequestSpecification request = RestAssured.given();
//		request.baseUri(getBaseAPI());
//		request.basePath(getBaseAPI());
//		
//		request.queryParam("api_key", getKey());
//		request.queryParam("classifier_ids", props.getClassifierID());
//		request.queryParam("owners", props.getOwners());
//		request.queryParam("threshold", props.getThreshold());
//		request.queryParam("version", props.getVersion());
//		
//		request.formParam("images_file", imageData);
		
		try {
			URIBuilder builder = new URIBuilder();
			builder.setScheme("https").setHost(getBaseAPI().replace("https://","")).setPath("/v3/classify")
				.setParameter("api_key", getKey())
				.setParameter("classifier_ids", props.getClassifierID())
				.setParameter("owners", props.getOwners())
				.setParameter("threshold", "" + props.getThreshold())
				.setParameter("version", props.getVersion());
			URI uri = builder.build();
			
//		Response response = request.post("/v3/classify");
			
			HttpPost post = new HttpPost(uri);
			
			MultipartEntityBuilder me = MultipartEntityBuilder.create();
			me.addPart("images_file", new FileBody(new File(imageData)));
			post.setEntity(me.build());
			
			HttpResponse response = client.execute(post);
			
			if (response.getStatusLine().getStatusCode() != 200)
				throw new RuntimeException("The service was unsuccessful in processing the request! HTTP: " + response.getStatusLine());
			
			BufferedReader rd = new BufferedReader(
			        new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			return ResponseParser.decide(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occurred while the system was accessing Watson's API. Please try again later."; 
		}
	}
}
