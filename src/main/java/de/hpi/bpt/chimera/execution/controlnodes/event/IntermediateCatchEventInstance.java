package de.hpi.bpt.chimera.execution.controlnodes.event;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;

//TODO maybe rename or make abstract again
public class IntermediateCatchEventInstance extends AbstractEventInstance {

	private static final Logger logger = Logger.getLogger(IntermediateCatchEventInstance.class);

	public IntermediateCatchEventInstance(IntermediateCatchEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
	}
}