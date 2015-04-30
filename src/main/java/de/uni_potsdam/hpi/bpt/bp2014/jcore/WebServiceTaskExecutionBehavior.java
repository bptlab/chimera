package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Created by jaspar.mang on 29.04.15.
 */
public class WebServiceTaskExecutionBehavior extends TaskExecutionBehavior {


    public WebServiceTaskExecutionBehavior(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        super(activityInstance_id, scenarioInstance, controlNodeInstance);
    }

    @Override
    public void execute(){
        this.setCanTerminate(true);
    }
}
