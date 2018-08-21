package de.hpi.bpt.chimera.rest.beans.event;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;

@XmlRootElement
public class MultipleReceiveEventJaxBean {
	private List<ReceiveEventJaxBean> events;

	public MultipleReceiveEventJaxBean(List<MessageReceiveEventBehavior> eventBehaviors) {
		events = eventBehaviors.stream()
					.map(ReceiveEventJaxBean::new)
					.collect(Collectors.toList());
	}
	
	public List<ReceiveEventJaxBean> getEvents() {
		return events;
	}

	public void setEvents(List<ReceiveEventJaxBean> events) {
		this.events = events;
	}
}
