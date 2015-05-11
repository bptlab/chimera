package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import org.json.JSONObject;

/**
 * Created by jaspar.mang on 11.05.15.
 */
public class ExampleAService implements AnalyticsService {
    public JSONObject calculateResult(String[] args){
        return new JSONObject("{\"id\":1,\"linkDataObject\":\"http://localhost:8080/JEngine/api/interface/v2/scenario/155/instance/1302/inputset/1\"}");
    }
}
