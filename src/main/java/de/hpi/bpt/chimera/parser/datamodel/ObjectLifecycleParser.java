package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycle;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;
import de.hpi.bpt.chimera.validation.NameValidation;
import de.hpi.bpt.chimera.validation.ObjectLifecycleValidation;

public class ObjectLifecycleParser {
	private static final Logger log = Logger.getLogger((ObjectLifecycleParser.class).getName());

	private ObjectLifecycleParser() {
	}
	
	/**
	 * Parse ObjectLifecycle out of olcJson. In addition validate
	 * ObjectLifecycle.
	 * 
	 * @param olcJson
	 * @return ObjectLifecycle
	 */
	public static ObjectLifecycle parseObjectLifecylce(JSONObject olcJson) {
		ObjectLifecycle objectLifecycle = new ObjectLifecycle();
		
		try {
			Map<String, ObjectLifecycleState> mapIdToState = getMapIdToState(olcJson.getJSONArray("state"));
			NameValidation.validateNameFrequency(new ArrayList<>(mapIdToState.values()));

			List<SequenceFlow> sequenceFlows = new ArrayList<>();
			if (olcJson.has("sequenceFlow")) {
				sequenceFlows = parseSequenceFlow(olcJson.getJSONArray("sequenceFlow"));
				ObjectLifecycleValidation.validateSequenceFlow(mapIdToState, sequenceFlows);
			}
			List<ObjectLifecycleState> olcStates = parseOlcStates(mapIdToState, sequenceFlows);
			objectLifecycle.setObjectLifecycleStates(olcStates);

		} catch (JSONException | IllegalArgumentException e) {
			log.error(e);
			throw new JSONException("Invalid ObjectLifecycle States - " + e.getMessage());
		} catch (IllegalCaseModelException e) {
			throw e;
		}

		return objectLifecycle;
	}

	/**
	 * Create ObjectLifecycleStates and put them in a Map with their ids.
	 * 
	 * @param olcStateJsonArray
	 * @return Map from ObjectLifeCycleState-Id to ObjectLifeCycleState
	 */
	private static Map<String, ObjectLifecycleState> getMapIdToState(JSONArray olcStateJsonArray) {
		int arraySize = olcStateJsonArray.length();
		Map<String, ObjectLifecycleState> mapIdToState = new HashMap<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject olcStateJson = olcStateJsonArray.getJSONObject(i);
			ObjectLifecycleState olcState = new ObjectLifecycleState();

			try {
				String name = olcStateJson.getString("name");
				NameValidation.validateName(name);
				olcState.setName(name);
				String id = olcStateJson.getString("id");

				mapIdToState.put(id, olcState);
			} catch (JSONException | IllegalArgumentException e) {
				log.error(e);
				throw new IllegalCaseModelException("Invalid ObjectLifecycleState - " + e.getMessage());
			}
		}

		return mapIdToState;
	}

	/**
	 * Get ids of sourceState and targetState of SequenceFlow and put them in a
	 * List.
	 * 
	 * @param sequenceFlowJsonArray
	 * @return List of SequenceFlows
	 */
	private static List<SequenceFlow> parseSequenceFlow(JSONArray sequenceFlowJsonArray) {
		int arraySize = sequenceFlowJsonArray.length();
		List<SequenceFlow> sequenceFlows = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject sequenceFlowJson = sequenceFlowJsonArray.getJSONObject(i);
			SequenceFlow sequenceFlow = new SequenceFlow();
			sequenceFlow.setSource(sequenceFlowJson.getString("sourceRef"));
			sequenceFlow.setTarget(sequenceFlowJson.getString("targetRef"));
			sequenceFlows.add(sequenceFlow);
		}

		return sequenceFlows;
	}

	/**
	 * Converts the State-ids stored in SequenceFlows into the referring states
	 * with mapIdToState and add successor and predecessor which you get from
	 * SequenceFlow to referring state.
	 * 
	 * @param mapIdToState
	 *            parsed by getMapIdToState
	 * @param sequenceFlows
	 *            parsed by parseSequenceFlow
	 * @return List of ObjectLifecycleStates
	 */
	private static List<ObjectLifecycleState> parseOlcStates(Map<String, ObjectLifecycleState> mapIdToState, List<SequenceFlow> sequenceFlows) {
		for (SequenceFlow sequenceFlow : sequenceFlows) {
			ObjectLifecycleState sourceState = mapIdToState.get(sequenceFlow.getSource());
			ObjectLifecycleState targetState = mapIdToState.get(sequenceFlow.getTarget());

			sourceState.addSuccessor(targetState);
			targetState.addPredecessor(sourceState);
		}

		return new ArrayList<>(mapIdToState.values());
	}
}
