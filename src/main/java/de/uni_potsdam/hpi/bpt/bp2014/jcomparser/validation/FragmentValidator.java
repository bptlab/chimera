package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DomainModel;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.Olc;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class FragmentValidator {

    /**
     * Hide public default constructor
     */
    private FragmentValidator() { }

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
        validateNames(fragment);
    }

    private static void validateNames(Fragment fragment) {
        String pattern =
                "^([a-zA-Z\\d\\n]|[a-zA-Z\\d\\n](?!.*[ _]{2})[a-zA-Z\\d\\n _]*?[a-zA-Z\\d\\n])$";
        Pattern compiledPattern = Pattern.compile(pattern);
        for (AbstractDataControlNode task : fragment.getTasks()) {
            if (!compiledPattern.matcher(task.getName()).matches() && !"".equals(task.getName())) {
                throw new IllegalArgumentException(String.format("%s is not a valid task name",
                        task.getName()));
            }
        }
        for (DataNode dataNode : fragment.getDataNodes()) {
            if (!compiledPattern.matcher(dataNode.getName()).matches() && !"".equals(dataNode.getName())) {
                throw new IllegalArgumentException(String.format("%s is not a valid data node name",
                        dataNode.getName()));
            }
        }
    }
    /**
     * This method validates a fragment against the given OLCs. Note that in case there is no OLC
     * for a given DataClass all transitions for this class are considered valid.
     * @param olcs A map of DataClasses (identified by name) to their respective OLCs.
     */
    private static void validateOlc(Map<String, Olc> olcs, Fragment fragment) {
        Map<String, DataNode> idToDataNode = getIdToDataNode(fragment.getDataNodes());
        for (AbstractDataControlNode task : fragment.getAllActivities()) {
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
                    if (!olcForDataobject.allowedStateTransitions.containsKey(state)) {
                        throw new InvalidDataTransitionException("Invalid OLC transition found ");
                    }
                    List<String> allowedTransitionsForState =
                            olcForDataobject.allowedStateTransitions.get(state);
                    if (!allowedTransitionsForState.containsAll(outputStates)) {
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
            AbstractDataControlNode task, Map<String, DataNode> idToDataNode) {
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
            AbstractDataControlNode task, Map<String, DataNode> idToDataNode) {
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
