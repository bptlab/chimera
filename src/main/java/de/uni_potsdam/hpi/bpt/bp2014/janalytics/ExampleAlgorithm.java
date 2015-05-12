package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ExampleAlgorithm implements AnalyticsService{
    static Logger log = Logger.getLogger(MetaAnalyticsModel.class.getName());

    public Object exampleAlgorithm1(int scenarioInstance_id) {
        ArrayList<Map<Integer, Map<String, Object>>> model = AnalyticsModel.exampleAlgorithm1(scenarioInstance_id);

        //TODO: do some fancy calculations
        Object result = new Object();
        return result;
    }

    public JSONObject calculateResult(String[] args){
        int scenarioId = new Integer (args[0]);
        return exampleAlgorithm2(scenarioId);
    }

    public static void main(String[] args){

        exampleAlgorithm2(105);
    }

    public static JSONObject exampleAlgorithm2(int scenario_id) {

        // MeanScenarioInstanceRunTime

        long avgDuration = 0L;
        long sumDuration = 0L;
        int numberOfScenarioInstances = 0;
        //get instances for scenario
        List<DbScenarioInstanceIDsAndTimestamps> scenarioInstances = MetaAnalyticsModel.getScenarioInstancesForScenario(scenario_id);

        for (DbScenarioInstanceIDsAndTimestamps scenarioInstance : scenarioInstances) {



            scenarioInstance.initiallizeTimestamps();
            long duration =  scenarioInstance.getDuration();
            if(duration < 0){

                continue;
            }
                    sumDuration = sumDuration + duration;
                    numberOfScenarioInstances++;
        }

        if (!scenarioInstances.isEmpty()){
            avgDuration = sumDuration / numberOfScenarioInstances;
        }
        System.out.println(avgDuration);
        long second = (avgDuration / 1000) % 60;
        long minute = (avgDuration / (1000 * 60)) % 60;
        long hour = (avgDuration / (1000 * 60 * 60)) % 24;
        long day = (avgDuration / (1000 * 60 * 60 * 24)) % 365;

        String time = String.format("%02d:%02d:%02d:%02d", day, hour, minute, second);
        System.out.println(time);
        String json = "{\"scenarioId\":" + scenario_id + ",\"meanScenarioInstanceRuntime\":\"" + time + "\"}";
        JSONObject result = new JSONObject(json);
        return result;
    }

    public static class DbScenarioInstanceIDsAndTimestamps{

        private int scenarioInstanceID;
        private Date startDate;
        private Date endDate;

        public DbScenarioInstanceIDsAndTimestamps(int scenarioInstanceID) {
            this.scenarioInstanceID = scenarioInstanceID;
        }

        public void setScenarioInstanceID(int scenarioinstanceID) {
            this.scenarioInstanceID = scenarioinstanceID;
        }

        public long getDuration(){
            if(endDate == null || startDate == null){
                return (-1);
            }
           return (endDate.getTime() - startDate.getTime());
        }

        public void initiallizeTimestamps() {

        String sql = "SELECT MAX(timestamp) AS end_timestamp, MIN(timestamp) AS start_timestamp FROM `historydataobjectinstance` as h, scenarioinstance as s WHERE h.scenarioinstance_id = "+scenarioInstanceID+" AND h.scenarioinstance_id = s.id AND s.terminated = 1";
            java.sql.Connection conn = Connection.getInstance().connect();
            ResultSet results = null;
            try {
                results = conn.prepareStatement(sql).executeQuery();
                results.next();
                startDate = results.getTimestamp("start_timestamp");
                endDate = results.getTimestamp("end_timestamp");

            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("SQL Error!: ", e);
                }
                try {
                    results.close();
                } catch (SQLException e) {
                    log.error("SQL Error!: ", e);
                }
            }

        }

    }


}
