package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 * Created by jaspar.mang on 04.02.15.
 */
public class Condition {
    private int conditionSet_id;
    private int state_id;
    private int dataObject_id;
    private int scenario_id;

    public Condition(int conditionSet_id, int state_id, int dataObject_id, int scenario_id) {

        this.conditionSet_id = conditionSet_id;
        this.state_id = state_id;
        this.dataObject_id = dataObject_id;
        this.scenario_id = scenario_id;
    }

    public int getConditionSet_id() {
        return conditionSet_id;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "conditionSet_id=" + conditionSet_id +
                ", state_id=" + state_id +
                ", dataObject_id=" + dataObject_id +
                ", scenario_id=" + scenario_id +
                '}';
    }

    public void setConditionSet_id(int conditionSet_id) {
        this.conditionSet_id = conditionSet_id;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getDataObject_id() {
        return dataObject_id;
    }

    public void setDataObject_id(int dataObject_id) {
        this.dataObject_id = dataObject_id;
    }

    public int getScenario_id() {
        return scenario_id;
    }

    public void setScenario_id(int scenario_id) {
        this.scenario_id = scenario_id;
    }


}
