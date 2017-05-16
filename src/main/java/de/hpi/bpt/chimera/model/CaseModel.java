package de.hpi.bpt.chimera.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;

@Entity
public class CaseModel {
	// TODO is this id unique, or should we add an auto generated id which then
	// is for the database only?
	@Id
	private String id;
	private String name;
	private int versionNumber;
	@OneToOne(cascade = CascadeType.PERSIST)
	private DataModel dataModel;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Fragment> fragments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
