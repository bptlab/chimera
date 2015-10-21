package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * This class provides an Algorithm to calculate the mean ScenarioInstanceRuntime for all terminated ScenarioInstances
 * of a Scenario
 */
public class ExampleAlgorithm implements AnalyticsService {
    static final Logger log = Logger.getLogger(ExampleAlgorithm.class.getName());

    @Override
    /**
     * This Method calculates the mean runtime for all terminated instances of a scenario
     *
     * @param args used to specify the ScenarioID for which the mean Instance-Runtime is calculated
     * @return a JSONObject containing the scenarioID and the mean Instance-Runtime
     */
    public JSONObject calculateResult(String[] args) {
        // calculate MeanScenarioInstanceRunTime
        int scenarioId = new Integer(args[0]);
        long avgDuration = 0L;
        long sumDuration = 0L;
        int numberOfScenarioInstances = 0;
        int millisecondsPerSecond = 1000;
        int secondsPerMinute = 60;
        int minutesPerHour = 60;
        int hoursPerDay = 24;
        int daysPerYear = 365;
        //get instances for scenario
        List<DbScenarioInstanceIDsAndTimestamps> scenarioInstances = MetaAnalyticsModel.getScenarioInstancesForScenario(scenarioId);
        for (DbScenarioInstanceIDsAndTimestamps scenarioInstance : scenarioInstances) {
            // retrieve timestamps for start and end of a terminated scenarioinstance
            scenarioInstance.initializeTimestamps();
            // calculate runtime for single instance
            long duration = scenarioInstance.getDuration();
            if (duration >= 0) {
                sumDuration = sumDuration + duration;
                numberOfScenarioInstances++;
            }
        }

        if (!scenarioInstances.isEmpty() && numberOfScenarioInstances != 0) {
            avgDuration = sumDuration / numberOfScenarioInstances;
        } else if (numberOfScenarioInstances == 0) {
            return new JSONObject("{\"scenarioId\":" + scenarioId + ",\"meanScenarioInstanceRuntime\":\"No terminated instances.\"}");
        }

        // converting duration from milliseconds to human readable format days:hours:minutes:seconds
        long second = (avgDuration / millisecondsPerSecond) % secondsPerMinute;
        long minute = (avgDuration / (millisecondsPerSecond * secondsPerMinute)) % minutesPerHour;
        long hour = (avgDuration / (millisecondsPerSecond * secondsPerMinute * minutesPerHour)) % hoursPerDay;
        long day = (avgDuration / (millisecondsPerSecond * secondsPerMinute * minutesPerHour * hoursPerDay)) % daysPerYear;

        String time = String.format("%02d:%02d:%02d:%02d", day, hour, minute, second);
        String json = "{\"scenarioId\":" + scenarioId + ",\"meanScenarioInstanceRuntime\":\"" + time + "\"}";
        return new JSONObject(json);
    }

    /**
     * This class is used to provide objects which hold information about the start and end dates of a scenarioinstance
     */
    public static class DbScenarioInstanceIDsAndTimestamps {

        private int scenarioInstanceID;
        private Date startDate;
        private Date endDate;


        public DbScenarioInstanceIDsAndTimestamps(int scenarioInstanceID) {
            this.scenarioInstanceID = scenarioInstanceID;
        }


        public void setScenarioInstanceID(int scenarioinstanceID) {
            this.scenarioInstanceID = scenarioinstanceID;
        }

        /**
         * This method is used to calculate the time difference between the start and end date in milliseconds
         *
         * @return duration in milliseconds
         */
        public long getDuration() {
            if (endDate == null || startDate == null) {
                return (-1);
            }
            return (endDate.getTime() - startDate.getTime());
        }

        /**
         * This method accesses the database to retrieve the start and end timestamps for the given scenarioinstanceID attribute
         */
        public void initializeTimestamps() {

            String sql = "SELECT MAX(timestamp) AS end_timestamp, MIN(timestamp) AS start_timestamp FROM `historydataobjectinstance` as h, scenarioinstance as s WHERE h.scenarioinstance_id = " + scenarioInstanceID + " AND h.scenarioinstance_id = s.id AND s.terminated = 1";
            java.sql.Connection conn = Connection.getInstance().connect();
            ResultSet results = null;
            try {
                results = conn.prepareStatement(sql).executeQuery();
                if (results.next()) {
                    startDate = results.getTimestamp("start_timestamp");
                    endDate = results.getTimestamp("end_timestamp");
                }

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
