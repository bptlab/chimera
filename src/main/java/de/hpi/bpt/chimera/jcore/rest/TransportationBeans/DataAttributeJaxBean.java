package de.hpi.bpt.chimera.jcore.rest.TransportationBeans;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.DataAttributeInstance;

/**
 *
 */
@XmlRootElement
public class DataAttributeJaxBean {
	private String id;
	private String name;
	private String type;
	private Object value;

	public DataAttributeJaxBean(DataAttributeInstance dataAttributeInstance) {
		setName(dataAttributeInstance.getDataAttribute().getName());
		setId(dataAttributeInstance.getId());
		setType(dataAttributeInstance.getDataAttribute().getType());
		Object attributeValue = dataAttributeInstance.getValue();
		if (attributeValue == null)
			attributeValue = "";
		setValue(attributeValue);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}

