package de.hpi.bpt.chimera.model.condition;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class TerminationCondition extends DataStateCondition {
	public TerminationCondition() {
		super();
	}
}
