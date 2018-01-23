package de.hpi.bpt.chimera.history.transportationbeans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
@Entity(name = "LogEntryTransportationBean")
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class LogEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	int dbId;

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
