package de.hpi.bpt.chimera.validation;

import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.parser.datamodel.SequenceFlow;

//TODO: think about validating the states in JsonPath, means if there
// income and outcome is a valid sequenceflow, beacuse it does not affect
// parsing

public class ObjectLifecycleValidation {

	private ObjectLifecycleValidation() {
	}

	/**
	 * Validate that every sequence flow refers to existing states.
	 * 
	 * @param mapIdToState
	 * @param sequenceFlows
	 */
	public static void validateSequenceFlow(Map<String, ObjectLifecycleState> mapIdToState, List<SequenceFlow> sequenceFlows) {
		for (SequenceFlow sequenceFlow : sequenceFlows) {
			if (!mapIdToState.containsKey(sequenceFlow.getSource()) || !mapIdToState.containsKey(sequenceFlow.getTarget())) {
				throw new IllegalArgumentException("Sequence flow contains unknown state id");
			}
		}
	}
}
