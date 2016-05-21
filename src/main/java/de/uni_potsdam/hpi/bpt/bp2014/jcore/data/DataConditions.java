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
    private Map<Integer, Integer> dataclassToState;

    public Map<Integer, Integer> getDataclassToState() {
        return dataclassToState;
    }

    public boolean checkConditions(List<DataObject> dataObjects) {
        Map<Integer, Set<Integer>> dataClassIdToStateId = getAvailableDataClassStates(dataObjects);
        for (Map.Entry<Integer, Integer> cond : dataclassToState.entrySet()) {
            Set<Integer> availableStates = dataClassIdToStateId.get(cond.getKey());
            if (!(availableStates.contains(cond.getValue()))) {
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
    public Map<Integer, Set<Integer>> getAvailableDataClassStates(List<DataObject> dataObjects) {
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
