package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DomainModel;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.Olc;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class FragmentValidator {

    /**
     * For a given fragment and its corresponding domain model, checks if
     * all data objects belong to a data class, and if all state transitions are modeled
     * in the OLC and thus valid.
     * Throws an exception if these conditions are not fulfilled.
     * @param fragment
     * @param domainModel
     */
    public static void validateFragment(Fragment fragment, DomainModel domainModel) {
        validateDataReferences(fragment, domainModel);
        validateOlc(domainModel.getOlcs(), fragment);
    }

    /**
     * This method validates a fragment against the given OLCs. Note that in case there is no OLC
     * for a given DataClass all transitions for this class are considered valid.
     * @param olcs A map of DataClasses (identified by name) to their respective OLCs.
     * @return true if the fragment matches all given OLCs (false if there is a violation)
     */
    private static void validateOlc(Map<String, Olc> olcs, Fragment fragment) {
        Map<String, DataNode> idToDataNode = getIdToDataNode(fragment.getDataNodes());
        for (AbstractTask task : fragment.getAllActivities()) {
            Map<String, List<String>> incomingDataobjectStates =
                    getIncomingStatesPerDataobject(task, idToDataNode);
            Map<String, List<String>> outgoingDataobjectStates =
                    getOutgoingStatesPerDataobject(task, idToDataNode);
            List<String> dataObjectsNames = getDataObjectsToBeChecked(
                    incomingDataobjectStates.keySet(),
                    outgoingDataobjectStates.keySet(), olcs.keySet());

            for (String dataobjectName : dataObjectsNames) {
                List<String> inputStates = incomingDataobjectStates.get(dataobjectName);
                List<String> outputStates = outgoingDataobjectStates.get(dataobjectName);
                Olc olcForDataobject = olcs.get(dataobjectName);
                for (String state : inputStates) {
                    if (!olcForDataobject.allowedStateTransitions.get(state).containsAll(outputStates)) {
                        throw new InvalidDataTransitionException("Invalid OLC transition found ");
                    }
                }
            }
        }
    }

    private static void validateDataReferences(Fragment fragment, DomainModel domainModel) {
        Set<String>  dataclassNames = domainModel.getMapFromNameToDataclass().keySet();
        for (DataNode node : fragment.getDataNodes()) {
            if (!dataclassNames.contains(node.getName())) {
                throw new InvalidDataclassReferenceExeption(String.format(
                        "Data node %s references an invalid data class", node.getName()));
            }
        }
    }

    private static Map<String, DataNode> getIdToDataNode(List<DataNode> dataNodes) {
        Map<String, DataNode> idToDataNode = new HashMap<>();
        for (DataNode dataNode : dataNodes) {
            idToDataNode.put(dataNode.getId(), dataNode);
        }
        return idToDataNode;
    }


    private static Map<String, List<String>> getIncomingStatesPerDataobject(
            AbstractTask task, Map<String, DataNode> idToDataNode) {
        Map<String, List<String>> incomingStatesPerDataobject = new HashMap<>();
        for (DataInputAssociation assoc : task.getDataInputAssociations()) {
            DataNode dataNode =  idToDataNode.get(assoc.getSourceRef());
            if (!incomingStatesPerDataobject.containsKey(dataNode.getName())) {
                incomingStatesPerDataobject.put(dataNode.getName(), new ArrayList<>());
            }
            incomingStatesPerDataobject.get(dataNode.getName()).add(dataNode.getState());
        }
        return incomingStatesPerDataobject;
    }

    private static Map<String, List<String>> getOutgoingStatesPerDataobject(
            AbstractTask task, Map<String, DataNode> idToDataNode) {
        Map<String, List<String>> outgoingStatesPerDataobject = new HashMap<>();
        for (DataOutputAssociation assoc : task.getDataOutputAssociations()) {
            DataNode dataNode =  idToDataNode.get(assoc.getTargetRef());
            if (!outgoingStatesPerDataobject.containsKey(dataNode.getName())) {
                outgoingStatesPerDataobject.put(dataNode.getName(), new ArrayList<>());
            }
            outgoingStatesPerDataobject.get(dataNode.getName()).add(dataNode.getState());
        }
        return outgoingStatesPerDataobject;
    }


    /**
     *
     * @param incomingDataobjects
     * @param outgoingDataObjects
     * @param dataObjectsWithOlc
     * @return a list of data object names for data objects, that are in an input and
     * an output set and are part of an OLC
     */
    private static List<String> getDataObjectsToBeChecked(
            Set<String> incomingDataobjects,
            Set<String> outgoingDataObjects,
            Set<String> dataObjectsWithOlc) {
        return Stream.concat(incomingDataobjects.stream(), outgoingDataObjects.stream())
                .distinct().filter(dataObjectsWithOlc::contains).collect(Collectors.toList());
    }

}
