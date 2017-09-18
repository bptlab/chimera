package de.hpi.bpt.chimera.jhistory.transportationbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.DataAttributeInstance;

@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.NONE)
public class DataAttributeLog extends LogEntry {

	public DataAttributeLog(DataAttributeInstance instance, Object oldValue, Object newValue) {
		super();
		this.setLabel(instance.getDataAttribute().getName());
		this.setObjectReferenceId(instance.getId());
		if (oldValue == null)
			this.setOldValue("empty");
		else
			this.setOldValue(oldValue.toString());
		this.setNewValue(newValue.toString());
	}
}
