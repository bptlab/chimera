package de.hpi.bpt.chimera.rest.beans.history;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;

@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class DataAttributeLog extends LogEntry {

	/**
	 * for JPA only
	 */
	public DataAttributeLog() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	public DataAttributeLog(DataAttributeInstance instance, Object oldValue, Object newValue) {
		super();
		this.setLabel(instance.getDataAttribute().getName());
		this.setObjectReferenceId(instance.getId());
		if (oldValue == null)
			this.setOldValue(null);
		else
			this.setOldValue(oldValue.toString());
		if (newValue == null)
			this.setOldValue(null);
		else
			this.setNewValue(newValue.toString());
	}
}
