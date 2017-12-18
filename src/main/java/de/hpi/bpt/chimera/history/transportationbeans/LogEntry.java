package de.hpi.bpt.chimera.history.transportationbeans;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
public abstract class LogEntry {

	// TODO: think about cause
	private String label;
	private Date timeStamp;
	private String oldValue;
	private String newValue;
	private String objectReferenceId;

	public LogEntry() {
		Date date = new Date();
		this.timeStamp = new java.sql.Timestamp(date.getTime());
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getObjectReferenceId() {
		return objectReferenceId;
	}

	public void setObjectReferenceId(String objectReferenceId) {
		this.objectReferenceId = objectReferenceId;
	}
}
