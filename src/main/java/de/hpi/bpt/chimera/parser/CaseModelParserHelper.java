package de.hpi.bpt.chimera.parser;

import java.util.HashMap;
import java.util.Map;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.validation.ParsingHelperValidation;

/**
 * Assists CaseModelParser by storing NameToObject maps, so they do not have to
 * be newly created every time. In addition the Class validates all the Lists,
 * Maps and Objects that it gets.
 */
public class CaseModelParserHelper {
	private DataModel dataModel;
	private Map<String, DataClass> nameToDataModelClass;
	private Map<DataClass, Map<String, DataAttribute>> dataClassAndNameToDataAttribute;
	private Map<DataClass, Map<String, ObjectLifecycleState>> dataClassAndNameToOLCState;

	public CaseModelParserHelper(DataModel dataModel) {
		this.dataModel = dataModel;
		this.nameToDataModelClass = dataModel.getNameToDataModelClass();
		this.dataClassAndNameToDataAttribute = new HashMap<>();
		this.dataClassAndNameToOLCState = new HashMap<>();
	}

	/**
	 * Returns DataClass corresponding to DataClassName. Validates existence of
	 * DataClass suggested by dataClassName and validates that DataModelClass
	 * received by nameToDataModelClass is of DataClass type.
	 * 
	 * @param dataClassName
	 * @return DataClass
	 */
	public DataClass getDataClassByName(String dataClassName) {
		try {
			ParsingHelperValidation.validateMap(this.nameToDataModelClass, dataClassName);
			return this.nameToDataModelClass.get(dataClassName);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Returns DataAttribute corresponding to dataModelClass and
	 * dataAttributeName. If it does not already exists in
	 * dataClassAndNameToDataAttribute the existence of DataAttribute suggested
	 * by dataModelClass and dataAttributeName gets validated. If it exists it
	 * will be put in dataClassAndNameToDataAttribute, so that it does not have
	 * to be created again.
	 * 
	 * @param dataModelClass
	 * @param dataAttributeName
	 * @return DataAttribute
	 */
	public DataAttribute getNameToDataAttribute(DataClass dataModelClass, String dataAttributeName) {
		if (!this.dataClassAndNameToDataAttribute.containsKey(dataModelClass)) {
			ParsingHelperValidation.validateList(dataModel.getDataModelClasses(), dataModelClass);

			this.dataClassAndNameToDataAttribute.put(dataModelClass, dataModelClass.getNameToDataAttribute());
		}

		ParsingHelperValidation.validateMap(this.dataClassAndNameToDataAttribute.get(dataModelClass), dataAttributeName);
		return this.dataClassAndNameToDataAttribute.get(dataModelClass).get(dataAttributeName);
	}

	/**
	 * Returns ObjectLifecycleState corresponding to dataClass and
	 * objectLifecycleStateName. If it does not already exists in
	 * dataClassAndNameToOLCState the existence of ObjectLifecycleState
	 * suggested by dataModelClass and objectLifecycleStateName gets validated.
	 * If it exists it will be put in dataClassAndNameToOLCState, so that it
	 * does not have to be created again.
	 * 
	 * @param dataClass
	 * @param objectLifecycleStateName
	 * @return ObjectLifecycleState
	 */
	public ObjectLifecycleState getObjectLifecycleStateByName(DataClass dataClass, String objectLifecycleStateName) {
		if (!this.dataClassAndNameToOLCState.containsKey(dataClass)) {
			ParsingHelperValidation.validateList(dataModel.getDataModelClasses(), dataClass);
			
			this.dataClassAndNameToOLCState.put(dataClass, dataClass.getNameToObjectLifecycleState());
		} 
		
		ParsingHelperValidation.validateMap(this.dataClassAndNameToOLCState.get(dataClass), objectLifecycleStateName);
		return this.dataClassAndNameToOLCState.get(dataClass).get(objectLifecycleStateName);
	}
}
