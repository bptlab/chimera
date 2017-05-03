package de.hpi.bpt.chimera.janalytics;

import org.json.JSONObject;

/**
 * Interface for all analytic algorithms as controller.
 */
public interface AnalyticsService {
	/**
	 * @param args the arguments the algorithm gets.
	 * @return a json with result of the calculation.
	 */
	JSONObject calculateResult(String[] args);
}