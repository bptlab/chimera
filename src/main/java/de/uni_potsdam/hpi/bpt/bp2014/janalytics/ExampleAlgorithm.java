package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import java.sql.Timestamp;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class ExampleAlgorithm implements AnalyticsService{
    static Date date = new Date(1990, 4, 28, 12, 59);

    public Object exampleAlgorithm1(int scenarioInstance_id) {
        ArrayList<Map<Integer, Map<String, Object>>> model = AnalyticsModel.exampleAlgorithm1(scenarioInstance_id);

        //TODO: do some fancy calculations
        Object result = new Object();
        return result;
    }

    public JSONObject calculateResult(String[] args){

        JSONObject result = new JSONObject();
        return result;
    }

    public static JSONObject exampleAlgorithm2(int scenario_id) {
        ArrayList<Map<Integer, Map<String, Object>>> scenarioInstances = AnalyticsModel.calculateMeanScenarioInstanceRunTime(scenario_id);

        //get instances for scenario
        // loop for all instances:
        //      get start and end timestamp
        //      calculate diff
        //      return diff
        // sum diffs
        // divide by # instances
        // return mean diff

        JSONObject result = new JSONObject();
        return result;
    }

    public static String getTimestampDiff(Timestamp t) {
        final DateTime start = new DateTime(date.getTime());
        final DateTime end = new DateTime(t);
        Period p = new Period(start, end);
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .printZeroAlways().minimumPrintedDigits(2).appendYears()
                .appendSuffix(" year", " years").appendSeparator(", ")
                .appendMonths().appendSuffix(" month", " months")
                .appendSeparator(", ").appendDays()
                .appendSuffix(" day", " days").appendSeparator(" and ")
                .appendHours().appendLiteral(":").appendMinutes()
                .appendLiteral(":").appendSeconds().toFormatter();
        return p.toString(formatter);
    }
}
