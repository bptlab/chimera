package de.hpi.bpt.chimera.rest.beans.datamodel;

import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A JAX bean which is used for dataobject data.
 * It contains the data of one dataobject.
 * It can be used to create a JSON Object
 */
@XmlRootElement
public class DataObjectJaxBean {
	/**
	 * The id the dataobjectinstance.
	 */
	private String id;

	/**
	 * The dataclass of the datanode. The label not the id will be saved.
	 */
	private String dataclass;

	/**
	 * The state inside the database of the dataobject
	 * which is stored in the table.
	 * The label not the id will be saved.
	 */
	private String state;

	private boolean locked;
	/**
	 * An array of all dataAttributes belonging to this dataObject.
	 * Each attribute has an id, name, type and value.
	 */
	private DataAttributeJaxBean[] attributeConfiguration;

	public DataObjectJaxBean(DataObject dataObject) {
		setId(dataObject.getId());
		setDataclass(dataObject.getDataClass().getName());
		setState(dataObject.getObjectLifecycleState().getName());
		setLocked(dataObject.isLocked());

		List<DataAttributeJaxBean> attributes = new ArrayList<>(dataObject.getDataAttributeInstanceIdToInstance().size());
		for (DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstanceIdToInstance().values()) {
			DataAttributeJaxBean attributeInstance = new DataAttributeJaxBean(dataAttributeInstance);
			attributes.add(attributeInstance);
		}

		DataAttributeJaxBean[] attributesArray = attributes.toArray(new DataAttributeJaxBean[dataObject.getDataAttributeInstanceIdToInstance().size()]);
		setAttributeConfiguration(attributesArray);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataclass() {
		return dataclass;
	}

	public void setDataclass(String dataclass) {
		this.dataclass = dataclass;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public DataAttributeJaxBean[] getAttributeConfiguration() {
		return attributeConfiguration;
	}

	public void setAttributeConfiguration(DataAttributeJaxBean[] attributeConfiguration) {
		this.attributeConfiguration = attributeConfiguration;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean isLocked) {
		this.locked = isLocked;
	}
}

