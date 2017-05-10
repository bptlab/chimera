package de.hpi.bpt.chimera.model;

import java.util.List;

import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;

public class CaseModel {
	private String name;
	private int versionNumber;
	private DataModel dataModel;
	private List<Fragment> fragments;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public DataModel getDataModel() {
		return dataModel;
	}
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}
	public List<Fragment> getFragments() {
		return fragments;
	}
	public void setFragments(List<Fragment> fragments) {
		this.fragments = fragments;
	}
}
