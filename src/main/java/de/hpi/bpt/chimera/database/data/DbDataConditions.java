package de.hpi.bpt.chimera.database.data;

import de.hpi.bpt.chimera.jcore.data.DataManager;

import java.util.*;

/**
 * Database access for user readable representation of input and output sets.
 * Used by the {@link DataManager}, and outgoing behaviors.
 */
public class DbDataConditions {

    /**
     * Retrieve the input set data for a given activity.
     * @param activityId Id of the activity.
     * @return A mapping from data object name to possible input states.
     */
    public Map<String, Set<String>> loadInputSets(int activityId) {
        List<Integer> inputSetIds = new DbDataFlow().getInputSetsForControlNode(activityId);
        return loadDataDependency(inputSetIds);
    }

    /**
     * Retrieve the output set data for a given activity.
     * @param activityId Id of the activity.
     * @return A mapping from data object name to possible output states.
     */
    public Map<String, Set<String>> loadOutputSets(int activityId) {
        List<Integer> outputSetIds = new DbDataFlow()
                .getOutputSetsForControlNode(activityId);
        return loadDataDependency(outputSetIds);
    }

    private Map<String, Set<String>> loadDataDependency(List<Integer> setIds) {
        Map<Integer, Set<Integer>> inputIdToStateIds = new HashMap<>();
        DbDataNode dbDataNode = new DbDataNode();
        for (Integer inputSetId : setIds) {
            Map<Integer, Integer> inputSet = dbDataNode.getDataSetClassToStateMap(inputSetId);
            for (Map.Entry<Integer, Integer> entry : inputSet.entrySet()) {
                if (!inputIdToStateIds.containsKey(entry.getKey())) {
                    inputIdToStateIds.put(entry.getKey(), new HashSet<>());
                }
                inputIdToStateIds.get(entry.getKey()).add(entry.getValue());
            }
        }
        return resolveNames(inputIdToStateIds);
    }
    
    private Map<String, Set<String>> resolveNames(Map<Integer, Set<Integer>> inputIdToStates) {
        Map<String, Set<String>> result = new HashMap<>();
        DbDataClass dataClass = new DbDataClass();
        DbState dbState = new DbState();
        for (Map.Entry<Integer, Set<Integer>> entry : inputIdToStates.entrySet()) {
            int dataClassId = entry.getKey();
            String dataClassName = dataClass.getName(dataClassId);
            result.put(dataClassName, new HashSet<>());
            for (Integer stateId : entry.getValue()) {
                String stateName = dbState.getStateName(stateId);
                result.get(dataClassName).add(stateName);
            }
        }
        return result;
    }
}
