package de.hpi.bpt.chimera.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.Listable;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.datamodel.DataModelClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

/**
 * Assists CaseModelParser by storing NameToObject maps, so they do not have to
 * be newly created every time. In addition the Class validates all the Lists,
 * Maps and Objects that it gets.
 */
public class CaseModelParserHelper {
	private DataModel dataModel;
	private Map<String, DataModelClass> nameToDataModelClass;
	private Map<DataModelClass, Map<String, DataAttribute>> dataClassAndNameToDataAttribute;
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
	public DataClass getNameToDataClass(String dataClassName) {
		validateMap(this.nameToDataModelClass, dataClassName);
		validateType(this.nameToDataModelClass.get(dataClassName), DataClass.class);
		return (DataClass) this.nameToDataModelClass.get(dataClassName);
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
	public DataAttribute getNameToDataAttribute(DataModelClass dataModelClass, String dataAttributeName) {
		if (!this.dataClassAndNameToDataAttribute.containsKey(dataModelClass)) {
			validateList(dataModel.getDataModelClasses(), dataModelClass);

			this.dataClassAndNameToDataAttribute.put(dataModelClass, dataModelClass.getNameToDataAttribute());
		}

		validateMap(this.dataClassAndNameToDataAttribute.get(dataModelClass), dataAttributeName);
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
	public ObjectLifecycleState getNameToObjectLifecycleState(DataClass dataClass, String objectLifecycleStateName) {
		if (!this.dataClassAndNameToOLCState.containsKey(dataClass)) {
			validateList(dataModel.getDataModelClasses(), dataClass);
			
			this.dataClassAndNameToOLCState.put(dataClass, dataClass.getNameToObjectLifecycleState());
		} 
		
		validateMap(this.dataClassAndNameToOLCState.get(dataClass), objectLifecycleStateName);
		return this.dataClassAndNameToOLCState.get(dataClass).get(objectLifecycleStateName);
	}

	// TODO: put this in validation
	private static void validateMap(Map<String, ? extends Listable> mapToCheck, String toCheck) {
		if (!mapToCheck.containsKey(toCheck)) {
			throw new IllegalArgumentException(String.format("%s does not exist", toCheck));
		}
	}

	private static void validateList(List<? extends Listable> listToCheck, Listable objectToCheck) {
		if (!listToCheck.contains(objectToCheck)) {
			throw new IllegalArgumentException(String.format("%s does not exist", objectToCheck.toString()));
		}
	}

	private static void validateType(Object o, @SuppressWarnings("rawtypes") Class clazz) {
		if (!(clazz.isInstance(o))) {
			throw new IllegalArgumentException(String.format("%s is not a %s", o.toString(), clazz.getName()));
		}
	}
}
