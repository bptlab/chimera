package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import java.util.Map;

import javax.persistence.Entity;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.IntermediateCatchEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstanceWriter;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.MessageReceiveDefinition;

@Entity
public class SignalReceiveBehavior extends MessageReceiveEventBehavior {

    /**
     * for JPA only
     */
    public SignalReceiveBehavior() {
        // JPA needs an empty constructor to instantiate objects of this class
        // at runtime.
    }


    public SignalReceiveBehavior(AbstractEventInstance eventInstance) {
        super(eventInstance);
    }
}
