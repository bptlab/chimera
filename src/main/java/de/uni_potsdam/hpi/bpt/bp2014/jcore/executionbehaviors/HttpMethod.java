package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

/**
 * Enum for Http Methods used by the WebServiceTask.
 */
public enum HttpMethod {
    GET ("GET"),
    POST ("POST"),
    PUT ("PUT");

    private final String name;

    private HttpMethod(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
