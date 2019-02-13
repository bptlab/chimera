package de.hpi.bpt.chimera.model.datamodel;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ObjectLifecycle {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;

	@OneToMany(cascade = CascadeType.ALL)
	List<ObjectLifecycleState> objectLifecycleStates;

	@OneToOne(cascade = CascadeType.ALL)
	private
	ObjectLifecycleState initialObjectLifecycleState;

	public void setObjectLifecycleStates(List<ObjectLifecycleState> objectLifecycleStates) {
		this.objectLifecycleStates = objectLifecycleStates;
	}

	public List<ObjectLifecycleState> getObjectLifecycleStates() {
		return this.objectLifecycleStates;
	}

	public ObjectLifecycleState getInitialObjectLifecycleState() {
		return initialObjectLifecycleState;
	}

	public void setInitialObjectLifecycleState(ObjectLifecycleState initialObjectLifecycleState) {
		this.initialObjectLifecycleState = initialObjectLifecycleState;
	}
}
