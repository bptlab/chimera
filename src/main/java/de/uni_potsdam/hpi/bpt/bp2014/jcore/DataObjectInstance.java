package de.uni_potsdam.hpi.bpt.bp2014.jcore;

public class DataObjectInstance {
    public OLCInstance olcInstance;
    public int object_id;
    public int scenario_id;
    public int scenarioInstance_id;

    public DataObjectInstance(int object_id, int scenario_id, int scenarioInstance_id){
        this.object_id = object_id;
        this.scenario_id = scenario_id;
        this.scenarioInstance_id = scenarioInstance_id;
    }
}
