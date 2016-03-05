package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;


import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataObject;
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


    // auto increment in the database
    private int conditionSetId;
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
    public void addCondition(int dataObjectId, int stateId) {
        this.dataObjectIdToStateId.put(dataObjectId, stateId);
    }

    /**
     * Find a DataObject by the name of its Data Class.
     * @param dataClassName name of the dataClass
     * @return DataObject that has the given class
     */
    private static DataObject findDataObjectByClass(String dataClassName, List<DataObject> dataObjects) {
        return dataObjects.stream()
                .filter(a -> a.getDataClassName().equals(dataClassName))
                .findFirst().get();
    }


    /**
     * Find the id of a DataNode State by the name.
     * @param stateName name of the state
     * @return id of the state
     */
    private static int findStateIdByName(String stateName) {
        DbState dbState = new DbState();
        return dbState.getStateId(stateName);
    }

    /**
     * @param jsonTerminationConditions Json Array of termination Strings in the format of
     *                                  DataClass[State1], DataClass[State2]
     * @return List of parsed termination conditions.
     */
    public static List<TerminationCondition> parseTerminationConditions(
            JSONArray jsonTerminationConditions, List<DataObject> dataObjects, int scenarioId,
            Map<String, Integer> stateToDatabaseId) {
        List<TerminationCondition> conditions = new ArrayList<>();
        for (int i = 0; i < jsonTerminationConditions.length(); i++) {
            String condition = jsonTerminationConditions.getString(i);
            conditions.add(parseTerminationConditionString(
                    condition, dataObjects, scenarioId, stateToDatabaseId));
        }
        return conditions;
    }

    private static TerminationCondition parseTerminationConditionString(
            String conditionString, List<DataObject> dataObjects, int scenarioId,
            Map<String, Integer> stateToDatabaseId) {
        TerminationCondition condition = new TerminationCondition(scenarioId);

        for (String tcString : conditionString.split(",")) {
            Pattern pattern = Pattern.compile("\\s?(.*)\\[(.*?)\\]");
            Matcher match = pattern.matcher(tcString);
            if (match.find()) {
                String dataClassString = match.group(1);
                String stateString = match.group(2);

                int dataObjectId = findDataObjectByClass(dataClassString, dataObjects).getDatabaseId();
                int stateId = stateToDatabaseId.get(stateString);
                condition.addCondition(dataObjectId, stateId);
            } else {
                throw new IllegalArgumentException(String.format(
                        "Malformed termination String %s", tcString));
            }
        }

        return condition;
    }

    public void save() {
        String conditionSetId = UUID.randomUUID().toString();
        Connector conn = new Connector();
        // this.conditionSetId =
        // conn.insertTerminationConditionIntoDatabase(stateId, dataObjectId, scenarioId);
    }

    public int getConditionSetId() {
        return conditionSetId;
    }

    public void setConditionSetId(int conditionSetId) {
        this.conditionSetId = conditionSetId;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }
}
