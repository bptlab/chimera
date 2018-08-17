package de.hpi.bpt.chimera.execution.data;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class ObjectLifecycleTransition {
	private DataClass dataclass;
	private ObjectLifecycleState olcState;

	public ObjectLifecycleTransition(DataClass dataclass, ObjectLifecycleState olcState) {
		this.dataclass = dataclass;
		this.olcState = olcState;
	}

	public DataClass getDataclass() {
		return dataclass;
	}

	public void setDataclass(DataClass dataclass) {
		this.dataclass = dataclass;
	}

	public ObjectLifecycleState getOlcState() {
		return olcState;
	}

	public void setOlcState(ObjectLifecycleState olcState) {
		this.olcState = olcState;
	}

}
