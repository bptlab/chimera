package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

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
}
