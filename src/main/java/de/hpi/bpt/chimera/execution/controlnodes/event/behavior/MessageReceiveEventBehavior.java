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
public class MessageReceiveEventBehavior extends EventBehavior {

	private static final Logger logger = Logger.getLogger(IntermediateCatchEventInstance.class);

	private String notificationRule; // important for Unicorn

	private String eventJson = "";


	/**
	 * for JPA only
	 */
	public MessageReceiveEventBehavior() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public MessageReceiveEventBehavior(AbstractEventInstance eventInstance) {
		super(eventInstance);
	}

	/**
	 * ExecutionBehaviour
	 */
	@Override
	public void begin() {
		logger.info("Controlflow of IntermediateCatchEventInstance enabled");
		EventDispatcher.registerReceiveEvent(this);
		getEventInstance().setState(State.REGISTERED);
	}

	@Override
	public void skip() {
		EventDispatcher.deregisterReceiveEvent(this);
	}

	@Override
	public void terminate() {
		EventDispatcher.deregisterReceiveEvent(this);
		
		if (eventJson.isEmpty()) {
			logger.info("No event json present to write data attributes from.");
			return;
		}

		if (!getEventInstance().getControlNode().hasPostCondition()) {
			logger.info("Message receive event has no output set, received data can not be stored.");
			return;
		}

		for (DataObject dataObject : getEventInstance().getOutputDataObjects()) {
			AtomicDataStateCondition condition = dataObject.getCondition();
			Map<DataAttribute, String> dataAttributeToJsonPath = getEventInstance().getControlNode().getJsonPathMapping().get(condition);
			DataAttributeInstanceWriter.writeDataAttributeInstances(dataObject, dataAttributeToJsonPath, eventJson);
		}

		getEventInstance().getFragmentInstance().activate();
	}

	/**
	 * An receive event can only terminate if the {@link FragmentInstance} is
	 * executable.
	 * 
	 * @see FragmentInstance#isExecutable() isExecutable
	 */
	@Override
	public boolean canTerminate() {
		return getEventInstance().getFragmentInstance().isExecutable();
	}

	/**
	 * The request key is equal to Id of the EventInstance.
	 * 
	 * @return the Id the EventInstance is registered with at Unicorn.
	 */
	public String getRequestKey() {
		return getEventInstance().getId();
	}

	public MessageReceiveDefinition getMessageDefinition() {
		return (MessageReceiveDefinition) getEventInstance().getControlNode().getSpecialEventDefinition();
	}

	public String getNotificationRule() {
		return notificationRule;
	}

	public void setNotificationRule(String notificationRule) {
		this.notificationRule = notificationRule;
	}

	public String getEventJson() {
		return eventJson;
	}

	public void setEventJson(String eventJson) {
		this.eventJson = eventJson;
	}
}
