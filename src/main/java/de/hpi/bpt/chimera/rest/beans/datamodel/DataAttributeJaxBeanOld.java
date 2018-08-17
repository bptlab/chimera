package de.hpi.bpt.chimera.rest.beans.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

/**
 *
 */
@XmlRootElement
public class DataAttributeJaxBeanOld {
	private String id;
	private String name;
	private String type;
	private Object value;

	/**
	 * Create a DataAttributeConfiguration for a DataAttribute.
	 * 
	 * @param dataAttribute
	 */
	public DataAttributeJaxBeanOld(DataAttribute dataAttribute) {
		setName(dataAttribute.getName());
		setId(dataAttribute.getId());
		setType(dataAttribute.getType());
		setValue("");
	}

	/**
	 * Create a DataAttributeConfiguration for an Instance of a DataAttribute.
	 * 
	 * @param dataAttributeInstance
	 */
	public DataAttributeJaxBeanOld(DataAttributeInstance dataAttributeInstance) {
		setName(dataAttributeInstance.getDataAttribute().getName());
		setId(dataAttributeInstance.getDataAttribute().getId());
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

