package de.hpi.bpt.chimera.model.fragment.bpmn.event;

import javax.persistence.Entity;

@Entity
public class EndEvent extends AbstractEvent {
	// TODO: maybe implement MessageDefinition
	// >>>see jcomparser.jaxb.MessageDefinition.java
	@Override
	public boolean hasEventQuerry() {
		return false;
	}
}
