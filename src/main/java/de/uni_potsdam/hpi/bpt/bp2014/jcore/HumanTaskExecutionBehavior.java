package de.uni_potsdam.hpi.bpt.bp2014.jcore;

public class HumanTaskExecutionBehavior extends TaskExecutionBehavior{
    public HumanTaskExecutionBehavior(int activityInstance_id, ScenarioInstance scenarioInstance){
        this.activityInstance_id = activityInstance_id;
        this.scenarioInstance = scenarioInstance;
    }

}
