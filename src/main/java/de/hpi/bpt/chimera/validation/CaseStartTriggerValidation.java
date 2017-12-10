package de.hpi.bpt.chimera.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.CaseStartTriggerConsequence;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

public class CaseStartTriggerValidation {

	private CaseStartTriggerValidation() {
	}

	/**
	 * Validate that a CaseStartTriggerConsequence has a specified mapping of
	 * DataAttribute and a JsonPath.
	 * 
	 * @param arraySize
	 */
	public static void validateMappingAmount(int arraySize) {
		if (arraySize == 0) {
			throw new IllegalArgumentException("no mapping specified");
		}
	}

	/**
	 * Validate that more than one DataObjectStateCondition does not refer to
	 * the same DataAttribute.
	 * 
	 * @param 
	 */
	public static void validateCaseStartTriggers(List<CaseStartTriggerConsequence> caseStartTriggers) {
		// TODO: rethink this
		/*
		for (CaseStartTriggerConsequence trigger : triggers) {
			List<DataAttribute> dataAttributes = new ArrayList<>();

			for (Map<DataAttribute, String> dataAttributeToJsonPath : trigger.getJsonPathMapping().values()) {
				Collection<DataAttribute> mappedDataAttributes = dataAttributeToJsonPath.keySet();

				if (Collections.disjoint(dataAttributes, mappedDataAttributes)) {
					dataAttributes.addAll(mappedDataAttributes);
				} else {
					throw new IllegalArgumentException("CaseStartTrigger referres to the same Attribute more than once");
				}
			}
		}
		*/
	}

}
