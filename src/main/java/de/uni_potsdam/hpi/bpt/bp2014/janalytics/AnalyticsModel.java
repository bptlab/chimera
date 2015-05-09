package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
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

        Map<Integer, Map<String, Object>> ActivityLog = this.getLogEntriesForScenarioInstanceWithinActivity(scenarioInstance_id);
        Map<Integer, Map<String, Object>> DataAttributeLog = this.getLogEntriesForScenarioInstanceWithinDataAttribute(scenarioInstance_id);
        Map<Integer, Map<String, Object>> DataObjectLog = this.getLogEntriesForScenarioInstanceWithinDataObject(scenarioInstance_id);
        result.add(ActivityLog);
        result.add(DataAttributeLog);
        result.add(DataObjectLog);

        return result;
    }


}
