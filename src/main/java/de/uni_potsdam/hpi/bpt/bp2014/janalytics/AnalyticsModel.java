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
/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

/**
 *
 */
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


    /**
     *
     * @param scenarioInstance_id
     * @return
     */
    public static ArrayList<Map<Integer, Map<String, Object>>> calculateScenarioInstanceRunTime(int scenarioInstance_id){
        ArrayList<Map<Integer, Map<String, Object>>> result = new ArrayList<Map<Integer, Map<String, Object>>>();

        Map<Integer, Map<String, Object>> timestampsForScenarioInstance = getLogTimestampsForScenarioInstance(scenarioInstance_id);
        result.add (timestampsForScenarioInstance);

        return result;
    }


}
