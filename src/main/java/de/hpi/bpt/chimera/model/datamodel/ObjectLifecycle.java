package de.hpi.bpt.chimera.model.datamodel;

import java.util.List;

//TODO: decide wether sequence flow needs to be implemented

public class ObjectLifecycle {
	List<ObjectLifecycleState> objectLifecycleStates;

	public void setObjectLifecycleStates(List<ObjectLifecycleState> objectLifecycleStates) {
		this.objectLifecycleStates = objectLifecycleStates;
	}

	public List<ObjectLifecycleState> getObjectLifecycleStates() {
		return this.objectLifecycleStates;
	}
}
