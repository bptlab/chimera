package de.hpi.bpt.chimera.model.fragment.bpmn;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.hpi.bpt.chimera.jcomparser.jaxb.MessageDefinition;

@Entity
public class EndEvent extends AbstractEvent {
	// TODO: maybe implement MessageDefinition
	// >>>see jcomparser.jaxb.MessageDefinition.java
	@Override
	public boolean hasEventQuerry() {
		return false;
	}
}
