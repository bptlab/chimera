package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

@Entity
public abstract class Activity extends AbstractDataControlNode {
	// TODO This class only exists to specify control nodes as Tasks.
	// Maybe leave it out?
}
