package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataObjectReference")
@XmlAccessorType(XmlAccessType.NONE)
public class BpmnDataNode {
	@XmlAttribute(name = "id")
	private String dataObjectReferenceId;

	@XmlAttribute(name = "dataObjectRef")
	private String dataNodeId;

	/**
	 * Name of dataObject.
	 */
	@XmlAttribute(name = "name")
	private String name;
	/**
	 * Name of the dataclass the dataobject/node refers to.
	 */
	@XmlAttribute(name = "griffin:dataclass")
	private String dataClassName;
	/**
	 * Current [state] of the datanode
	 */
	@XmlAttribute(name = "griffin:state")
	private String olcStateName;
	/**
	 * JsonObject that might contain a jsonpath expression for each data attribute.
	 */
	@XmlAttribute(name = "griffin:jsonpath")
	private String jsonPath = "";

	// GETTER
	public String getDataNodeObjectReferenceId() {
		return dataObjectReferenceId;
	}

	public String getDataNodeId() {
		return dataNodeId;
	}

	public String getName() {
		return name;
	}

	public String getDataClassName() {
		return dataClassName;
	}

	public String getStateName() {
		return olcStateName;
	}

	public String getJsonPath() {
		return jsonPath;
	}
}
