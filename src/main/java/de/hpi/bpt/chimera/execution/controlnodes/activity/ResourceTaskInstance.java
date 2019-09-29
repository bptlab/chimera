package de.hpi.bpt.chimera.execution.controlnodes.activity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.ResourceTask;
import org.apache.log4j.Logger;

public class ResourceTaskInstance extends AbstractActivityInstance {
    private static final Logger log = Logger.getLogger(ResourceTaskInstance.class);

    @Override
    public void execute() {
        // it gets excecuted here
    }

    public ResourceTaskInstance(ResourceTask activity, FragmentInstance fragmentInstance) {
        super(activity, fragmentInstance);
    }

}
