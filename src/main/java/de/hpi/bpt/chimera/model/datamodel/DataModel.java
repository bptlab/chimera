package de.hpi.bpt.chimera.model.datamodel;

import java.util.List;

public class DataModel {
	private int versionNumber;
	private List<DataModelClass> dataModelClasses;

	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public List<DataModelClass> getDataModelClasses() {
		return dataModelClasses;
	}

	public void setDataModelClasses(List<DataModelClass> dataModelClasses) {
		this.dataModelClasses = dataModelClasses;
	}

}
