package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import org.json.JSONObject;

/**
 * Created by jaspar.mang on 11.05.15.
 */
public interface AnalyticsService {

    public JSONObject calculateResult(String[] args);
}
