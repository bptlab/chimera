package de.hpi.bpt.chimera.execution.controlnodes.activity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmptyActivity;
import org.apache.log4j.Logger;

public class EmptyActivityInstance extends AbstractActivityInstance {
    private static final Logger log = Logger.getLogger(EmptyActivityInstance.class);

    /**
     * for JPA only
     */
    public EmptyActivityInstance() {
        // JPA needs an empty constructor to instantiate objects of this class
        // at runtime.
    }

    public EmptyActivityInstance(EmptyActivity emptyActivity, FragmentInstance fragmentInstance) {
        super(emptyActivity, fragmentInstance);
    }

    @Override
    public EmptyActivity getControlNode() {
        return (EmptyActivity) super.getControlNode();
    }

    @Override
    public void execute() {
        log.debug("Executing empty activity.");
    }

}
