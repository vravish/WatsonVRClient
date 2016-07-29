package com.ibm.whhack.vrapi;

public class APICredentials {
	static class Credentials {
		private String url, api_key;
	
		public String getUrl() {
			return url;
		}
	
		public void setUrl(String url) {
			this.url = url;
		}
	
		public String getApi_key() {
			return api_key;
		}
	
		public void setApi_key(String api_key) {
			this.api_key = api_key;
		}

		@Override
		public String toString() {
			return "Credentials [url=" + url + ", api_key=" + api_key + "]";
		}
	}
	
	private Credentials credentials;

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public String toString() {
		return "APICredentials [credentials=" + credentials + "]";
	}
}
