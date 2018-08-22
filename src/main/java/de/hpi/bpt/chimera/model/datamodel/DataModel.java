package de.hpi.bpt.chimera.model.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DataModel {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;

	private int versionNumber;
	@OneToMany(cascade = CascadeType.ALL)
	private List<DataClass> dataModelClasses;

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public List<DataClass> getDataModelClasses() {
		return dataModelClasses;
	}

	public void setDataModelClasses(List<DataClass> dataModelClasses) {
		this.dataModelClasses = dataModelClasses;
	}

	/**
	 * Make a Map from name of DataModelClasses to the referring DataModelClass.
	 * 
	 * @return HashMap
	 */
	public Map<String, DataClass> getNameToDataModelClass() {
		Map<String, DataClass> nameToDataModelClass = new HashMap<>();

		for (DataClass dataModelClass : dataModelClasses) {
			nameToDataModelClass.put(dataModelClass.getName(), dataModelClass);
		}
		return nameToDataModelClass;
	}

	/**
	 * Get all DataClasses of the DataModel.
	 * 
	 * @return List of DataClasses
	 */
	public List<DataClass> getDataClasses() {
		// TODO: Decide whether we want to distinguish between DataClasses and
		// EventClasses.
//		List<DataClass> dataClasses = new ArrayList<>();
//		for (DataClass dataModelClass : dataModelClasses) {
//			if (dataModelClass instanceof DataClass) {
//				DataClass dataClass = (DataClass) dataModelClass;
//				dataClasses.add(dataClass);
//			}
//		}
//		return dataClasses;
		
		return dataModelClasses;
	}

	/**
	 * Make a Map from name of DataClasses to the referring DataClass.
	 * 
	 * @return HashMap
	 */
	public Map<String, DataClass> getNameToDataClass() {
		Map<String, DataClass> nameToDataClass = new HashMap<>();
		for (DataClass dataClass : getDataClasses()) {
			nameToDataClass.put(dataClass.getName(), dataClass);
		}
		return nameToDataClass;
	}
}
