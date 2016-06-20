package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

/**
 *
 */
public class IntermediateEvent extends AbstractEvent {

    public IntermediateEvent(int controlNodeId, int fragmentInstanceId,
                             ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.setFragmentInstanceId(fragmentInstanceId);

        scenarioInstance.getControlNodeInstances().add(this);
    }

    @Override
    public String getType() {
        return "IntermediateCatchEvent";
    }


    @Override
    public boolean skip() {
        return false;
    }
}
