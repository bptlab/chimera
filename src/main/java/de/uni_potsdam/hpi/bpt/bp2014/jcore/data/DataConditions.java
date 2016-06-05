package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import java.util.*;

/**
 * DataConditions represents a set of conditions consisting of a data class id and a state id.
 * The meaning of these conditions is that for each data class id there has to exist one
 * data object of the specified state.
 *
 *
 * For each data class specified by this
 */
public class DataConditions {
    protected Map<Integer, Integer> dataClassToState;

    public Map<Integer, Integer> getDataClassToState() {
        return dataClassToState;
    }

    public boolean checkConditions(List<DataObject> dataObjects) {
        Map<Integer, Set<Integer>> possibleStatesForDataClasses = getPossibleDataClassStates(dataObjects);
        for (Map.Entry<Integer, Integer> condition : dataClassToState.entrySet()) {
            int dataClassId = condition.getKey();
            int stateId = condition.getValue();
            if (!possibleStatesForDataClasses.containsKey(dataClassId)) {
                return false;
            }
            Set<Integer> possibleStatesOfClass = possibleStatesForDataClasses.get(condition.getKey());
            if (!(possibleStatesOfClass.contains(stateId))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Scans dataobjects for data class ids and collects the states.
     *
     * @return Map from dataclass id to set of states of data objects from this class.
     *         If there is no data object for a data class there is no entry for this class.
     */
    public Map<Integer, Set<Integer>> getPossibleDataClassStates(List<DataObject> dataObjects) {
        Map<Integer, Set<Integer>> dataclassToStates = new HashMap<>();
        for (DataObject dataObject : dataObjects) {
            int dataClassId = dataObject.getDataClassId();
            if (!dataclassToStates.containsKey(dataClassId)) {
                dataclassToStates.put(dataClassId, new HashSet<>());
            }
            dataclassToStates.get(dataClassId).add(dataObject.getStateId());
        }
        return dataclassToStates;
    }


}
