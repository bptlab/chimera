package de.hpi.bpt.chimera;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.parser.CaseModelParser;

public class CaseModelTestHelper {
	/**
	 * Get the JsonString which is saved at the filepath.
	 * 
	 * @param path
	 * @return String
	 */
	public static String getJsonString(String path) {
		File file = new File(path);
		try {
			String fileContent = FileUtils.readFileToString(file);
			return fileContent;
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Parse a CaseModel by a filepath which leads to the parsing code of the
	 * CaseModel.
	 * 
	 * @param filepath
	 * @return CaseModel
	 */
	public static CaseModel parseCaseModel(String filepath) {
		String jsonString = getJsonString(filepath);
		return CaseModelParser.parseCaseModel(jsonString);
	}

	/**
	 * Create a DataStateCondition by a given name of a DataClass and a given
	 * name of a ObjectLifecycleState by getting the concrete Objects in the
	 * CaseModel.
	 * 
	 * @param cm
	 * @param dataclassName
	 * @param objectLifecycleStateName
	 * @return DataStateCondition
	 */
	public static AtomicDataStateCondition createDataStateCondition(CaseModel cm, String dataclassName, String objectLifecycleStateName) {
		DataClass dataclass = getDataClassByName(cm, dataclassName);
		if (dataclass == null) {
			return null;
		}
		ObjectLifecycleState olcState = getObjectLifecycleStateByName(dataclass, objectLifecycleStateName);
		if (olcState == null) {
			return null;
		}

		return new AtomicDataStateCondition(dataclass, olcState);
	}

	/**
	 * Get a DataClass by its name in a specific CaseModel.
	 * 
	 * @param cm
	 * @param dataclassName
	 * @return DataClass
	 */
	public static DataClass getDataClassByName(CaseModel cm, String dataclassName) {
		List<DataClass> dataclasses = cm.getDataModel().getDataClasses();
		for (DataClass dataclass : dataclasses) {
			if (dataclass.getName().equals(dataclassName)) {
				return dataclass;
			}
		}
		return null;
	}

	/**
	 * Get an ObjectLifecycleState by its name in a specific DataClass.
	 * 
	 * @param dataclass
	 * @param objectLifeycleStateName
	 * @return ObjectLifecycleState
	 */
	public static ObjectLifecycleState getObjectLifecycleStateByName(DataClass dataclass, String objectLifeycleStateName) {
		Map<String, ObjectLifecycleState> nameToObjectLifecycle = dataclass.getNameToObjectLifecycleState();
		return nameToObjectLifecycle.get(objectLifeycleStateName);
	}

	/**
	 * Get an Event of a specific Fragment by its name.
	 * 
	 * @param fragment
	 * @param name
	 * @return
	 */
	public static AbstractEvent getEventByName(Fragment fragment, String name) {
		AbstractEvent searchedEvent = null;
		for (AbstractEvent event : fragment.getBpmnFragment().getEvents()) {
			if (event.getName().equals(name)) {
				if (searchedEvent != null) {
					throw new IllegalArgumentException(String.format("more than event exists with this name: %s", name));
				}
				searchedEvent = event;
			}
		}
		return searchedEvent;
	}
}
