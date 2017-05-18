package de.hpi.bpt.chimera.parser.condition;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import de.hpi.bpt.chimera.model.Listable;
import de.hpi.bpt.chimera.model.Nameable;
import de.hpi.bpt.chimera.model.condition.DataObjectStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.TerminationConditionComponent;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.datamodel.DataModelClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class TerminationConditionParser {
	private TerminationConditionParser() {
	}

	/**
	 * Parse TerminationCondition out of jsonArray with help of given dataModel.
	 * 
	 * @param jsonArray
	 * @param dataModel
	 * @return TerminationCondition
	 */
	public static TerminationCondition parseTerminationCondition(JSONArray jsonArray, DataModel dataModel) {
		TerminationCondition terminationCondition = new TerminationCondition();

		int arraySize = jsonArray.length();
		for (int i = 0; i < arraySize; i++) {
			String conditionComponentString = jsonArray.getString(i);
			terminationCondition.addConditionComponent(parseConditionComponent(conditionComponentString, dataModel));
		}

		return terminationCondition;
	}

	private static TerminationConditionComponent parseConditionComponent(String conditionString, DataModel dataModel) {
		TerminationConditionComponent conditionComponent = new TerminationConditionComponent();

		for (String component : conditionString.split(",")) {
			DataObjectStateCondition condition = new DataObjectStateCondition();

			Pattern pattern = Pattern.compile("\\s?(.*)\\[(.*?)\\]");
			Matcher match = pattern.matcher(component);
			if (!match.find()) {
				throw new IllegalArgumentException(String.format("%s is invalid termination String", component));
			}
			String dataClassString = match.group(1);
			String stateString = match.group(2);

			Map<String, DataModelClass> nameToDataModelClass = dataModel.getNameToDataModelClass();
			validateList(nameToDataModelClass, dataClassString);
			validateType(nameToDataModelClass.get(dataClassString), DataClass.class);
			DataClass dataClass = (DataClass)nameToDataModelClass.get(dataClassString);
			condition.setDataClass(dataClass);

			Map<String, ObjectLifecycleState> nameToObjectLifecycleState = dataClass.getNameToObjectLifecycleState();
			validateList(nameToObjectLifecycleState, stateString);
			ObjectLifecycleState objectLifecycleState = nameToObjectLifecycleState.get(stateString);
			condition.setState(objectLifecycleState);

			conditionComponent.addCondition(condition);
		}

		return conditionComponent;
	}

	// TODO: put this in validator
	private static void validateList(Map<String, ? extends Listable> mapToCheck, String toCheck) {
		if (!mapToCheck.containsKey(toCheck)) {
			throw new IllegalArgumentException(String.format("%s does not exist", toCheck));
		}
	}

	private static void validateType(Object o, Class clazz) {
		if (!(clazz.isInstance(o))) {
			throw new IllegalArgumentException(String.format("%s is not a %s", o.toString(), clazz.getName()));
		}
	}
}
