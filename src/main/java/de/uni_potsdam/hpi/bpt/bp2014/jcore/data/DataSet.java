package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

/**
 * TODO reuse part of this for termination condition
 * This class represents an input and output set.
 */
public class DataSet {
    private Map<Integer, Integer> dataclassToState;

    public DataSet(int dataSetId) {
        throw new NotImplementedException();
    }

    public Map<Integer, Integer> getDataclassToState() {
        return dataclassToState;
    }
}
