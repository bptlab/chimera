package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycle;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

//TODO: add validation of ids and sequenceFlow

public class ObjectLifecycleParser {

	private static class SequenceFlow {
		String source;
		String target;
	}

	private ObjectLifecycleParser() {
	}

	public static ObjectLifecycle parseObjectLifecylce(JSONObject olcJson) {
		ObjectLifecycle objectLifecycle = new ObjectLifecycle();

		validateObjectLifecycle(olcJson);

		Map<String, ObjectLifecycleState> mapIdToState = getMapIdToState(olcJson.getJSONArray("state"));
		
		List<SequenceFlow> sequenceFlows = new ArrayList<>();
		if (olcJson.has("sequenceFlow"))
			sequenceFlows = parseSequenceFlow(olcJson.getJSONArray("sequenceFlow"));

		List<ObjectLifecycleState> olcStates = parseOlcStates(mapIdToState, sequenceFlows);
		objectLifecycle.setObjectLifecycleStates(olcStates);
		
		return objectLifecycle;
	}

	// TODO: put this in validator
	private static void validateObjectLifecycle(JSONObject olcJson) {
		if (!olcJson.has("state")) {
			throw new IllegalArgumentException("Invalid olc - state missing");
		}
	}

	/**
	 * get all possible ObjectLifecycleState-Ids and the referring
	 * ObjectLifecycleState with name attribute
	 */
	private static Map<String, ObjectLifecycleState> getMapIdToState(JSONArray olcStateJsonArray) {
		int arraySize = olcStateJsonArray.length();
		Map<String, ObjectLifecycleState> mapIdToState = new HashMap<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject olcStateJson = olcStateJsonArray.getJSONObject(i);
			ObjectLifecycleState olcState = new ObjectLifecycleState();

			String name = olcStateJson.getString("name");
			olcState.setName(name);
			String id = olcStateJson.getString("id");

			mapIdToState.put(id, olcState);
		}
		return mapIdToState;
	}

	/**
	 * get all SequenceFlows out of JsonPath and returns source and target in
	 * form of id of state
	 */
	private static List<SequenceFlow> parseSequenceFlow(JSONArray sequenceFlowJsonArray) {
		int arraySize = sequenceFlowJsonArray.length();
		List<SequenceFlow> sequenceFlows = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject sequenceFlowJson = sequenceFlowJsonArray.getJSONObject(i);
			SequenceFlow sequenceFlow = new SequenceFlow();
			sequenceFlow.source = sequenceFlowJson.getString("sourceRef");
			sequenceFlow.target = sequenceFlowJson.getString("targetRef");
			sequenceFlows.add(sequenceFlow);
		}

		return sequenceFlows;
	}

	/*
	 * converts the Olc-State-ids stored in SequenceFlows into the referring
	 * states with mapIdToState and add successor and predecessor which you get
	 * from SequenceFlow to referring state
	 */
	private static List<ObjectLifecycleState> parseOlcStates(Map<String, ObjectLifecycleState> mapIdToState, List<SequenceFlow> sequenceFlows) {
		for (SequenceFlow sequenceFlow : sequenceFlows) {
			ObjectLifecycleState sourceState = mapIdToState.get(sequenceFlow.source);
			ObjectLifecycleState targetState = mapIdToState.get(sequenceFlow.target);

			sourceState.addSuccessor(targetState);
			targetState.addPredecessor(sourceState);
		}

		return new ArrayList<>(mapIdToState.values());
	}
}
