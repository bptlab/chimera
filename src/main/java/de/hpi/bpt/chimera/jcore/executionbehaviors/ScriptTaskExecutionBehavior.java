package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.controlnodes.DbScriptTask;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import org.apache.log4j.Logger;

public class ScriptTaskExecutionBehavior extends ActivityExecutionBehavior {

    private static Logger log = Logger.getLogger(ScriptTaskExecutionBehavior.class);
    private final int controlNodeId;

    private DbScriptTask dbScriptTask = new DbScriptTask();

    public ScriptTaskExecutionBehavior(ActivityInstance instance) {
        super(instance);
        controlNodeId = activityInstance.getControlNodeId();
    }

    @Override
    public void execute() {
        // TODO run java code file
        String path = dbScriptTask.getScriptFilePath(controlNodeId);
        log.info("Affenschaukel - " + System.getProperty( "catalina.base" ));

    }

}
