package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import java.util.ArrayList;
import java.util.Map;

import javax.persistence.Entity;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.IntermediateCatchEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstanceWriter;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

@Entity
public class MessageReceiveEventBehavior extends AbstractEventBehavior {

	private static final Logger logger = Logger.getLogger(IntermediateCatchEventInstance.class);

	private String notificationRule; // important for Unicorn
	private String unicornKey; // important for Unicorn

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
	 * IncomingBehaviour
	 */
	@Override
	public void enableControlFlow() {
		logger.info("Controlflow of IntermediateCatchEventInstance enabled");
		EventDispatcher.registerEvent(getEventInstance(), this);
		getEventInstance().setState(State.REGISTERED);
	}

	@Override
	public void skip() {
		EventDispatcher.unregisterEvent(getEventInstance(), this);
	}

	@Override
	public void terminate() {
		EventDispatcher.unregisterEvent(getEventInstance(), this);
		
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

	public String getEventJson() {
		return eventJson;
	}

	public void setEventJson(String eventJson) {
		this.eventJson = eventJson;
	}
}
