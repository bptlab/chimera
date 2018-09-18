package de.hpi.bpt.chimera.execution.controlnodes.event;

import javax.persistence.Entity;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;

@Entity
public class EndEventInstance extends AbstractEventInstance {

	private static final Logger log = Logger.getLogger(EndEventInstance.class);
	/**
	 * for JPA only
	 */
	public EndEventInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public EndEventInstance(EndEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
	}

	@Override
	public void terminate() {
		if (!canTerminate()) {
			log.info(String.format("The event instance of %s can not terminate", getControlNode().getName()));
			return;
		}

		super.terminate();
		getFragmentInstance().terminate();
	}

	@Override
	public EndEvent getControlNode() {
		return (EndEvent) super.getControlNode();
	}
}
