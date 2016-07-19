package de.hpi.bpt.chimera.jcomparser.validation;

import de.hpi.bpt.chimera.jcomparser.jaxb.AbstractDataControlNode;
import de.hpi.bpt.chimera.jcomparser.jaxb.DataInputAssociation;
import de.hpi.bpt.chimera.jcomparser.jaxb.DataNode;
import de.hpi.bpt.chimera.jcomparser.jaxb.DataOutputAssociation;
import de.hpi.bpt.chimera.jcomparser.json.DomainModel;
import de.hpi.bpt.chimera.jcomparser.json.Olc;
import de.hpi.bpt.chimera.jcomparser.saving.Fragment;

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
     * in the OLC and thus valid. Additionally, all tasks and DataNodes need valid names
     * and the fragment needs to be structural sound.
     * Throws an exception if these conditions are not fulfilled.
     * @param fragment This is a fragment.
     * @param domainModel This is the corresponding DomainModel for the fragment.
     */
    public static void validateFragment(Fragment fragment, DomainModel domainModel) {
        validateDataReferences(fragment, domainModel);
        validateOlc(domainModel.getOlcs(), fragment);
        validateNames(fragment);
        SoundnessValidator.validateStructuralSoundness(fragment);
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
     * TODO break up this logic
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
                if (incomingDataobjectStates.containsKey(dataobjectName)) {
                /* This is the case when dataobjects are created (i.e., not in input, but in output)
                     TODO instead of skipping when dataobjectName is not contained in
                     incoming dataobjects, check whether output state is valid begin state
                     for this data object */
                    List<String> inputStates = incomingDataobjectStates.get(dataobjectName);
                    if (!outgoingDataobjectStates.containsKey(dataobjectName)) {
                        continue;
                    }
                    Olc olcForDataobject = olcs.get(dataobjectName);
                    List<String> outputStates = outgoingDataobjectStates.get(dataobjectName);
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
    }

    private static void validateDataReferences(Fragment fragment, DomainModel domainModel) {
        Set<String>  dataclassNames = domainModel.getMapFromNameToDataClass().keySet();
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
        if (!incomingDataobjects.isEmpty()) {
            return Stream.concat(incomingDataobjects.stream(), outgoingDataObjects.stream())
                    .distinct().filter(dataObjectsWithOlc::contains).collect(Collectors.toList());
        }
        // this is the case with Activities that write data objects
        return new ArrayList<>();
    }

}
