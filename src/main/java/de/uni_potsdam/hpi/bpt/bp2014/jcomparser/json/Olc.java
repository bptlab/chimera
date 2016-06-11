package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class Olc {
    private static Logger log = Logger.getLogger(Olc.class);

    private Map<String, List<String>> stateToOutgoing = new HashMap<>();

    /**
     * Map which represents allowed state transitions.
     */
    public Map<String, List<String>> allowedStateTransitions = new HashMap<>();

    private List<String> stateNames = new ArrayList<>();

    public Olc(final String element) {
        try {
            JSONObject olcJson = new JSONObject(element);
            parseStates(olcJson);
            if (olcJson.has("sequenceFlow")) {
                parseSequenceFlows(olcJson);
                allowedStateTransitions = replaceIdsWithNames(olcJson);
            }
        } catch (JSONException e) {
            log.error(e);
            throw new JSONException("Illegal OLC JSON");
        }
    }

    private void parseStates(JSONObject olcJson) {
        if (!olcJson.has("state")) {
            throw new IllegalArgumentException("Invalid olc json found");
        }
        JSONArray states = olcJson.getJSONArray("state");
        for (int i = 0; i < states.length(); i++) {
            JSONObject state = states.getJSONObject(i);
            stateToOutgoing.put(state.getString("id"), new ArrayList<>());
            stateNames.add(state.getString("name"));
        }
    }

    private void parseSequenceFlows(JSONObject olcJson) {

        JSONArray sequenceFlow = olcJson.getJSONArray("sequenceFlow");
        for (int i = 0; i < sequenceFlow.length(); i++) {
            JSONObject flow = sequenceFlow.getJSONObject(i);
            stateToOutgoing.get(flow.getString("sourceRef")).add(flow.getString("targetRef"));
        }
    }

    private Map<String, String> getIdToNameMapping(JSONObject olcJson) {
        Map<String, String> idToName = new HashMap<>();
        JSONArray states = olcJson.getJSONArray("state");
        for (int i = 0; i < states.length(); i++) {
            JSONObject state = states.getJSONObject(i);
            idToName.put(state.getString("id"), state.getString("name"));
        }
        return idToName;
    }

    private Map<String, List<String>> replaceIdsWithNames(JSONObject olcJson) {
        Map<String, List<String>> nameOutgoing = new HashMap<>();
        Map<String, String> idToName = getIdToNameMapping(olcJson);
        for (Map.Entry<String, List<String>> entry : stateToOutgoing.entrySet()) {
            String stateName = idToName.get(entry.getKey());
            List<String> outgoingStateNames = entry.getValue().stream().map(
                    idToName::get).collect(Collectors.toList());
            nameOutgoing.put(stateName, outgoingStateNames);
        }
        return nameOutgoing;
    }

    public List<String> getStateNames() {
        return stateNames;
    }
}
