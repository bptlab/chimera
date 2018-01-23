package de.hpi.bpt.chimera.history;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.*;
import java.util.Date;

/**
 *
 */
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class LogEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	int dbId;

	private int id;
	private Date timeStamp;
	private LogType type;
	private String newValue;
	private String label;

	private int scenarioInstanceId;


	/**
	 * This id is the controlnode id in case of activities and events
	 * or the id of a data attribute or data object.
	 */
	private int loggedId;

	/**
	 * Cause indicates that this log entry is the result of an activity or event
	 * The value will be null for activity and event and only be set in data objects and data
	 * attributes
	 */
	private int cause;

	/**
	 * for JPA only
	 */
	public LogEntry() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	public LogEntry(int id, Date timeStamp, LogType type, String newValue, String label, int scenarioInstanceId, int loggedId, int cause) {
		this.id = id;
		this.timeStamp = timeStamp;
		this.type = type;
		this.newValue = newValue;
		this.label = label;
		this.scenarioInstanceId = scenarioInstanceId;
		this.loggedId = loggedId;
		this.cause = cause;
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
		appendCause(logEntry);
		traceElement.appendChild(logEntry);
	}

	;

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
		typeNode.setAttribute("value", type.name());
		traceElement.appendChild(typeNode);
	}

	private void appendLabel(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element labelNode = doc.createElement("string");
		labelNode.setAttribute("key", "label");
		labelNode.setAttribute("value", label);
		traceElement.appendChild(labelNode);
	}

	private void appendCause(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element causeNode = doc.createElement("string");
		causeNode.setAttribute("key", "cause");
		causeNode.setAttribute("value", String.valueOf(cause));
		traceElement.appendChild(causeNode);
	}

	private void appendLoggedId(Node traceElement) {
		Document doc = traceElement.getOwnerDocument();
		Element loggedIdNode = doc.createElement("string");
		loggedIdNode.setAttribute("key", "id");
		loggedIdNode.setAttribute("value", String.valueOf(loggedId));
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

	public String getString() {
		return this.newValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public LogType getType() {
		return type;
	}

	public void setType(LogType type) {
		this.type = type;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public int getScenarioInstanceId() {
		return scenarioInstanceId;
	}

	public void setScenarioInstanceId(int scenarioInstanceId) {
		this.scenarioInstanceId = scenarioInstanceId;
	}

	public int getLoggedId() {
		return loggedId;
	}

	public void setLoggedId(int loggedId) {
		this.loggedId = loggedId;
	}

	public int getCause() {
		return cause;
	}

	public void setCause(int cause) {
		this.cause = cause;
	}

	@XmlType(name = "LogType")
	@XmlEnum
	public enum LogType {
		@XmlEnumValue("DATA_ATTRIBUTE")
		DATA_ATTRIBUTE, @XmlEnumValue("DATA_OBJECT")
		DATA_OBJECT, @XmlEnumValue("ACTIVITY")
		ACTIVITY, @XmlEnumValue("EVENT")
		EVENT
	}
}
