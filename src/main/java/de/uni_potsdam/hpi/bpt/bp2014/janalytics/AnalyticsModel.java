package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Scenario;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsModel extends MetaAnalyticsModel {
    static Logger log = Logger.getLogger(AnalyticsModel.class.getName());

    /**
     * @param scenarioInstance_id
     * @return
     */
    public static ArrayList<Map<Integer, Map<String, Object>>> exampleAlgorithm1(int scenarioInstance_id) {
        ArrayList<Map<Integer, Map<String, Object>>> result = new ArrayList<Map<Integer, Map<String, Object>>>();

        Map<Integer, Map<String, Object>> ActivityLog = getLogEntriesForScenarioInstanceWithinActivity(scenarioInstance_id);
        Map<Integer, Map<String, Object>> DataAttributeLog = getLogEntriesForScenarioInstanceWithinDataAttribute(scenarioInstance_id);
        Map<Integer, Map<String, Object>> DataObjectLog = getLogEntriesForScenarioInstanceWithinDataObject(scenarioInstance_id);
        result.add(ActivityLog);
        result.add(DataAttributeLog);
        result.add(DataObjectLog);

        return result;
    }

    public static ArrayList<Map<Integer, Map<String, Object>>> calculateMeanScenarioInstanceRunTime(int scenario_id){
        ArrayList<Map<Integer, Map<String, Object>>> result = new ArrayList<Map<Integer, Map<String, Object>>>();

        Map<Integer, Map<String, Object>> scenarioInstancesForScenario = getScenarioInstancesForScenario(scenario_id);
        result.add (scenarioInstancesForScenario);

        return result;
    }

    public static ArrayList<Map<Integer, Map<String, Object>>> calculateScenarioInstanceRunTime(int scenarioInstance_id){
        ArrayList<Map<Integer, Map<String, Object>>> result = new ArrayList<Map<Integer, Map<String, Object>>>();

        Map<Integer, Map<String, Object>> timestampsForScenarioInstance = getLogTimestampsForScenarioInstance(scenarioInstance_id);
        result.add (timestampsForScenarioInstance);

        return result;
    }


}
