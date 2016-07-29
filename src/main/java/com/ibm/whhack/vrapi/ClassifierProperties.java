package com.ibm.whhack.vrapi;

public class ClassifierProperties {
	private String classifierID, version, owners;
	private double threshold;
	
	public String getClassifierID() {
		return classifierID;
	}
	public void setClassifierID(String classifierID) {
		this.classifierID = classifierID;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getOwners() {
		return owners;
	}
	public void setOwners(String owners) {
		this.owners = owners;
	}
	
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public String toString() {
		return "ClassifierProperties [classifierID=" + classifierID + ", version=" + version + ", owners=" + owners
				+ ", threshold=" + threshold + "]";
	}
}
