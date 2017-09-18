package de.hpi.bpt.chimera.jhistory.transportationbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.DataObjectInstance;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.NONE)
public class DataObjectLog extends LogEntry {

	public DataObjectLog(DataObjectInstance instance, ObjectLifecycleState oldState, ObjectLifecycleState newState) {
		super();
		this.setLabel(instance.getDataClass().getName());
		this.setObjectReferenceId(instance.getId());
		if (oldState == null)
			this.setOldValue("no state");
		else
			this.setOldValue(oldState.getName());
		this.setNewValue(newState.getName());
	}
}
