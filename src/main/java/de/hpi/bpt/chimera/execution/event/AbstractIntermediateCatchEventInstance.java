package de.hpi.bpt.chimera.execution.event;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.execution.DataAttributeInstanceWriter;
import de.hpi.bpt.chimera.execution.DataObject;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;

//TODO maybe rename or make abstract again
public class AbstractIntermediateCatchEventInstance extends AbstractEventInstance {

	private final static Logger logger = Logger.getLogger(AbstractIntermediateCatchEventInstance.class);

	private String notificationRule; // important for Unicorn
	private String unicornKey; // important for Unicorn

	public AbstractIntermediateCatchEventInstance(IntermediateCatchEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		// TODO Auto-generated constructor stub
	}

	/**
	 * IncomingBehaviour
	 */
	@Override
	public void enableControlFlow() {
		logger.info("Controlflow of IntermediateCatchEventInstance enabled");
		EventDispatcher.registerEvent(this);
		super.enableControlFlow();
	}

	@Override
	public void skip() {
		EventDispatcher.unregisterEvent(this);
		super.skip();
	}

	@Override
	public void terminate() {
		terminate("");
		super.terminate();
	}

	public void terminate(String json) {
		EventDispatcher.unregisterEvent(this);
		if (json.isEmpty()) {
			logger.info("No event json present to write data attributes from.");
		} else {
			this.writeDataObjects(json);
		}
		this.terminate();
		this.setState(State.TERMINATED);
		super.terminate();
	}

	public String getNotificationRule() {
		return notificationRule;
	}

	public void setNotificationRule(String notificationRule) {
		this.notificationRule = notificationRule;
	}

	public String getUnicornKey() {
		return unicornKey;
	}

	public void setUnicornKey(String unicornKey) {
		this.unicornKey = unicornKey;
	}

	private void writeDataObjects(String json) {
		// TODO
		logger.info("Here we should write JsonData to DataAttributes");
	}


}
