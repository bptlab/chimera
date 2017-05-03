package de.hpi.bpt.chimera.model.datamodel;

import java.util.List;

public class DataModel {
	private int versionNumber;
	private List<Class> classes;


	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public List<Class> getClasses() {
		return classes;
	}
	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}

}
