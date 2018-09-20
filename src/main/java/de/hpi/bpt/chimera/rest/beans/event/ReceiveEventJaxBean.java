package de.hpi.bpt.chimera.rest.beans.event;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;

@XmlRootElement
public class ReceiveEventJaxBean {
	private String name;
	private String requestKey;
	private String query;

	public ReceiveEventJaxBean(MessageReceiveEventBehavior receiveBehavior) {
		AbstractEventInstance eventInstance = receiveBehavior.getEventInstance();
		setName(eventInstance.getControlNode().getName());
		setRequestKey(eventInstance.getId());
		setQuery(receiveBehavior.getMessageDefinition().getEventQuerry());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequestKey() {
		return requestKey;
	}

	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
