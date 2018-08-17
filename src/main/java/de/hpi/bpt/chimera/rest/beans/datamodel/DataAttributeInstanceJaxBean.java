package de.hpi.bpt.chimera.rest.beans.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;

@XmlRootElement
public class DataAttributeInstanceJaxBean extends DataAttributeJaxBean {
	private Object value;

	/**
	 * Create a DataAttributeConfiguration for an Instance of a DataAttribute.
	 * 
	 * @param dataAttributeInstance
	 */
	public DataAttributeInstanceJaxBean(DataAttributeInstance dataAttributeInstance) {
		super(dataAttributeInstance.getDataAttribute());

		Object attributeValue = dataAttributeInstance.getValue();
		if (attributeValue == null)
			attributeValue = "";
		setValue(attributeValue);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
