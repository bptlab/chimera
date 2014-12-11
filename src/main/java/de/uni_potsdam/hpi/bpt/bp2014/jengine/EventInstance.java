package de.uni_potsdam.hpi.bpt.bp2014.jengine;

/**
 * Created by jaspar.mang on 24.11.14.
 */
public class EventInstance extends ControlNodeInstance {
    private ScenarioInstance scenarioInstance;
    private int fragmentInstance_id;
    public EventInstance(int fragmentInstance_id, ScenarioInstance scenarioInstance){
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
    }

    public void doEndevent(){
        scenarioInstance.restartFragment(fragmentInstance_id);
    }
}
