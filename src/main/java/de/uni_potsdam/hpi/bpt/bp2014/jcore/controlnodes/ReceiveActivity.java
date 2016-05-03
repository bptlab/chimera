package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

/**
 *
 */
// The ReceiveActivity extends AbstractEvent since it needs to handle event queries.
public class ReceiveActivity extends AbstractEvent {

    public ReceiveActivity(int controlNodeId, int fragmentInstanceId,
                           ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.setFragmentInstanceId(fragmentInstanceId);

        scenarioInstance.getControlNodeInstances().add(this);
    }

    @Override
    public String getType() {
        return "ReceiveActivity";
    }


    @Override
    public boolean skip() {
        return false;
    }
}
