package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataInputAssociation;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataOutputAssociation;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Task;
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
     * Takes in a fragment and throws
     * @param fragment
     * @param domainModel
     */
    public void validateFragment(Fragment fragment, DomainModel domainModel) {
        validateDataReferences(fragment, domainModel);
        if (!isOlcValid(domainModel.getOlcs(), fragment)) {
            throw new InvalidDataTransitionException("Invalid ocl transition found ");
        }
    }

    /**
     * This method validates an against the given OLCs. Note that in case there is no OLC
     * for a given DataClass all transitions for this class are considered valid.
     * @param olcs A map of DataClasses (identified by name) to their respective OLCs.
     * @return true if the fragment matches all given OLCs (false if there is a violation)
     */
    public Boolean isOlcValid(Map<String, Olc> olcs, Fragment fragment) {
        Map<String, DataNode> idToDataNode = getIdToDataNode(fragment.getDataNodes());
        for (Task task : fragment.getTasks()) {
            Map<String, List<String>> incomingDataobjectStates =
                    getIncomingStatesPerDataobject(task, idToDataNode);
            Map<String, List<String>> outgoingDataobjectStates =
                    getOutgoingStatesPerDataobject(task, idToDataNode);
            List<String> dataObjectsNames = getChangedDataobjectNamesWithOlc(
                    incomingDataobjectStates.keySet(),
                    outgoingDataobjectStates.keySet(), olcs.keySet());

            for (String dataobjectName : dataObjectsNames) {
                List<String> inputStates = incomingDataobjectStates.get(dataobjectName);
                List<String> outputStates = outgoingDataobjectStates.get(dataobjectName);
                Olc olcForDataobject = olcs.get(dataobjectName);
                for (String state : inputStates) {
                    if (!olcForDataobject.allowedStateTransitions.get(state).containsAll(outputStates)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void validateDataReferences(Fragment fragment, DomainModel domainModel) {
        Set<String>  dataclassNames = domainModel.getMapFromIdToDataclass().keySet();
        for (DataNode node : fragment.getDataNodes()) {
            if (!dataclassNames.contains(node.getName())) {
                throw new InvalidDataclassReferenceExeption(String.format(
                        "Data node %s references an invalid data class", node.getName()));
            }
        }
    }

    private Map<String, DataNode> getIdToDataNode(List<DataNode> dataNodes) {
        Map<String, DataNode> idToDataNode = new HashMap<>();
        for (DataNode dataNode : dataNodes) {
            idToDataNode.put(dataNode.getId(), dataNode);
        }
        return idToDataNode;
    }


    private Map<String, List<String>> getIncomingStatesPerDataobject(
            Task task, Map<String, DataNode> idToDataNode) {
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

    private Map<String, List<String>> getOutgoingStatesPerDataobject(
            Task task, Map<String, DataNode> idToDataNode) {
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


    private List<String> getChangedDataobjectNamesWithOlc(
            Set<String> incomingDataobjects,
            Set<String> outgoingDataObjects,
            Set<String> dataObjectsWithOlc) {
        // Get data objects which are in input and output set and have an olc
        return Stream.concat(incomingDataobjects.stream(), outgoingDataObjects.stream())
                .distinct().filter(dataObjectsWithOlc::contains).collect(Collectors.toList());
    }

}
