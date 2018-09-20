package de.hpi.bpt.chimera.rest.beans.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

/**
 *
 */
@XmlRootElement
public class DataAttributeJaxBean {
	private String id;
	private String name;
	private String type;

	/**
	 * Create a DataAttributeConfiguration for a DataAttribute.
	 * 
	 * @param dataAttribute
	 */
	public DataAttributeJaxBean(DataAttribute dataAttribute) {
		setName(dataAttribute.getName());
		setId(dataAttribute.getId());
		setType(dataAttribute.getType());
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
}

