package de.hpi.bpt.chimera.rest.beans.history;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
@Entity(name = "LogEntryTransportationBean")
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class LogEntryTransportationBean {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;

	// TODO: think about cause
	private String label;
	private Date timeStamp;
	private String oldValue;
	private String newValue;
	private String objectReferenceId;

	public LogEntryTransportationBean() {
		Date date = new Date();
		this.timeStamp = new java.sql.Timestamp(date.getTime());
	}

	public void appendToTrace(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element logEntry = doc.createElement("event");
		appendEntryId(logEntry);
		appendTimeStamp(logEntry);
		appendType(logEntry);
		appendValue(logEntry);
		appendLabel(logEntry);
		appendLoggedId(logEntry);
		// appendCause(logEntry);
		traceElement.appendChild(logEntry);
	}

	private void appendEntryId(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element timestampXml = doc.createElement("timestamp");
		timestampXml.setAttribute("key", "timestamp");
		timestampXml.setAttribute("value", timeStamp.toString());
		traceElement.appendChild(timestampXml);
	}

	private void appendType(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element typeNode = doc.createElement("string");
		typeNode.setAttribute("key", "type");
		typeNode.setAttribute("value", this.getClass().getName());
		traceElement.appendChild(typeNode);
	}

	private void appendLabel(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element labelNode = doc.createElement("string");
		labelNode.setAttribute("key", "label");
		labelNode.setAttribute("value", label);
		traceElement.appendChild(labelNode);
	}

//	private void appendCause(Node traceElement) {
//		Document doc = traceElement.getOwnerDocument();
//		Element causeNode = doc.createElement("string");
//		causeNode.setAttribute("key", "cause");
//		causeNode.setAttribute("value", String.valueOf(cause));
//		traceElement.appendChild(causeNode);
//	}

	private void appendLoggedId(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element loggedIdNode = doc.createElement("string");
		loggedIdNode.setAttribute("key", "id");
		loggedIdNode.setAttribute("value", objectReferenceId);
		traceElement.appendChild(loggedIdNode);
	}

	private void appendValue(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element valueNode = doc.createElement("string");
		valueNode.setAttribute("key", "value");
		valueNode.setAttribute("value", newValue);
		traceElement.appendChild(valueNode);
	}

	private void appendTimeStamp(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element id = doc.createElement("string");
		id.setAttribute("key", "entryId");
		id.setAttribute("value", newValue);
		traceElement.appendChild(id);
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
