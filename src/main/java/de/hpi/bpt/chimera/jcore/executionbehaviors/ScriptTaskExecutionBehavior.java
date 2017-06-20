package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import org.apache.log4j.Logger;

public class ScriptTaskExecutionBehavior extends ActivityExecutionBehavior {

    private static Logger log = Logger.getLogger(ScriptTaskExecutionBehavior.class);
    private final int controlNodeId;

    public ScriptTaskExecutionBehavior(ActivityInstance instance) {
        super(instance);
        controlNodeId = activityInstance.getControlNodeId();
    }

    @Override
    public void execute() {
        // TODO run java code file
        //new DbDataFlow().getInputSetsForControlNode()
    }

}
