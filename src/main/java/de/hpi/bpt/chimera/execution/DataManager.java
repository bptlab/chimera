package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;

public class DataManager {
	private Case caze;
	// TerminationCondition t = caze.getCaseModel().getTerminationConditions();
	private DataModel dataModel;
	private List<DataObjectInstance> dataClassInstances;

	public DataManager(Case caze, DataModel dataModel) {
		this.setCaze(caze);
		instantiate(dataModel);
	}

	private void instantiate(DataModel dataModel) {
		dataClassInstances = new ArrayList<>();
		// TODO: decide which one is better
		/*
		 * for (DataModelClass dataModelClass : dataModel.getDataClasses()) { if
		 * (dataModelClass instanceof DataClass) { DataClass dataClass =
		 * (DataClass) dataModelClass; DataObjectInstance dataObjectInstance =
		 * new DataObjectInstance(dataClass);
		 * dataClassInstances.add(dataObjectInstance); } }
		 */
		for (DataClass dataClass : dataModel.getDataClasses()) {
			DataObjectInstance dataObjectInstance = new DataObjectInstance(dataClass);
			dataClassInstances.add(dataObjectInstance);
		}
	}

	public Case getCaze() {
		return caze;
	}

	public void setCaze(Case caze) {
		this.caze = caze;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}
}
