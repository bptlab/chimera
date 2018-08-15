package de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class SpecialEventDefinition {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;
}
