package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import org.json.JSONObject;


/**
 * Interface for all analytic algorithms as controller
 */
public interface AnalyticsService {
    /**
     * @param args the arguments the algorithm gets.
     * @return a json with result of the calculation.
     */
    public JSONObject calculateResult(String[] args);
}
