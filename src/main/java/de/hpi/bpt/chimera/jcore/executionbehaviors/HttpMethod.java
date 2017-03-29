package de.hpi.bpt.chimera.jcore.executionbehaviors;

/**
 * Enum for Http Methods used by the WebServiceTask.
 */
public enum HttpMethod {
	GET("GET"), POST("POST"), PUT("PUT");

	private final String name;

	private HttpMethod(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}
}
