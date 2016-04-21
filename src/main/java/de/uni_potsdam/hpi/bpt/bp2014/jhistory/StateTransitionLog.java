package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import java.util.Date;

/**
 * This class represents the change of a value in the database
 * e.g. activity instance 1 changed from ready to running at time
 * These logs abstract from {@link LogEntry} and not stored in the database,
 */
public class StateTransitionLog {
    private String oldValue;
    private String newValue;
    private int id;
    private Date timeStamp;


}
