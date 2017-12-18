package de.hpi.bpt.chimera.history.transportationbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.DataObject;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.NONE)
public class DataObjectLog extends LogEntry {

	public DataObjectLog(DataObject dataObject, ObjectLifecycleState oldState, ObjectLifecycleState newState) {
		super();
		// this.setLabel(instance.getDataNode().getName());
		this.setLabel(dataObject.getDataClass().getName());
		this.setObjectReferenceId(dataObject.getId());
		if (oldState == null)
			this.setOldValue(null);
		else
			this.setOldValue(oldState.getName());
		this.setNewValue(newState.getName());
	}
}
