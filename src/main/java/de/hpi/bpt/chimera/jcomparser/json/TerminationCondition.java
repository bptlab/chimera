package de.hpi.bpt.chimera.jcomparser.json;


import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.json.JSONArray;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A Class representing a TerminationCondition,
 * consisting of multiple DataObjects in certain states.
 */
public class TerminationCondition {
    Map<Integer, Integer> dataObjectIdToStateId = new HashMap<>();
    private int scenarioId;

    /**
     * Initializes the Condition.
     *
     * @param scenarioId     This is the database ID of a Scenario.
     */
    public TerminationCondition(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     *
     * @param stateId        This is the ID of the state of the DataNode.
     * @param dataObjectId   This is the database ID of a DataObject.
     */
    private void addCondition(int dataObjectId, int stateId) {
        this.dataObjectIdToStateId.put(dataObjectId, stateId);
    }

    /**
     * @param jsonTerminationConditions Json Array of termination Strings in the format of
     *                                  DataClass[State1], DataClass[State2]
     * @param dataClasses
     * @return List of parsed termination conditions.
     */
    static List<TerminationCondition> parseTerminationConditions(
            JSONArray jsonTerminationConditions, List<DataClass> dataClasses, int scenarioId,
            Map<String, Integer> stateToDatabaseId) {
        List<TerminationCondition> conditions = new ArrayList<>();
        for (int i = 0; i < jsonTerminationConditions.length(); i++) {
            String condition = jsonTerminationConditions.getString(i);
            conditions.add(parseTerminationConditionString(
                    condition, dataClasses, scenarioId, stateToDatabaseId));
        }
        return conditions;
    }

    /**
     * Parses the Input from the editor to a termination condition.
     * Since a termination condition references dataclasses and states, which
     * need to be met to fulfill it, those have to be saved before.
     *
     * @param conditions String in the format "A[state], B[state2], ..."
     * @param dataClasses List of saved data class objects.
     * @param scenarioId Database id of the scenario. This means the scenario has
     *                   to be saved to call this method.
     * @param stateToDatabaseId Map from state name to it's database id.
     * @return Termination condition object
     * @throws IllegalArgumentException If invalid data class is referenced IllegalArgumentException is thrown.
     */
    private static TerminationCondition parseTerminationConditionString(
            String conditions, List<DataClass> dataClasses, int scenarioId,
            Map<String, Integer> stateToDatabaseId) {
        TerminationCondition condition = new TerminationCondition(scenarioId);
        Map<String, Integer> dataClassNameToId = dataClasses.stream()
                .collect(Collectors.toMap(DataClass::getName, DataClass::getDatabaseId));

        for (String tcString : conditions.split(",")) {
            Matcher match = findTerminationCondition(tcString);
            String dataClassString = match.group(1);
            String stateString = match.group(2);
            if (!dataClassNameToId.containsKey(dataClassString)) {
                String errorMsg = "Termination condition references invalid data class %s";
                throw new IllegalArgumentException(String.format(errorMsg, dataClassString));
            }
            int dataClassId = dataClassNameToId.get(dataClassString);
            int stateId = stateToDatabaseId.get(stateString);
            condition.addCondition(dataClassId, stateId);
        }

        return condition;
    }

    /**
     * Creates match object for termination condition String. First matching group
     * will be the name of the data class. The second one is the name of the state.
     * @param terminationCondition String in the format A[state]
     * @return Matcher object with the respective data class and state as matching groups
     * @throws IllegalArgumentException If the String is not in the specified format
     */
    private static Matcher findTerminationCondition(String terminationCondition) {
        Pattern pattern = Pattern.compile("\\s?(.*)\\[(.*?)\\]");
        Matcher match = pattern.matcher(terminationCondition);
        if (match.find()) {
            return match;
        } else {
            throw new IllegalArgumentException(String.format(
                    "Malformed termination String %s", terminationCondition));
        }
    }

    public void save() {
        String conditionSetId = UUID.randomUUID().toString();
        Connector conn = new Connector();
        for (Map.Entry<Integer, Integer> condition: this.dataObjectIdToStateId.entrySet()) {
            conn.insertTerminationCondition(condition.getKey(), condition.getValue(),
                    scenarioId, conditionSetId);
        }
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }
}
