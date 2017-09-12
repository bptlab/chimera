package de.hpi.bpt.chimera.jcore.rest.TransportationBeans;

import de.hpi.bpt.chimera.execution.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.DataObjectInstance;
import de.hpi.bpt.chimera.jcore.ExecutionService;
import de.hpi.bpt.chimera.jcore.data.DataObject;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A JAX bean which is used for dataobject data.
 * It contains the data of one dataobject.
 * It can be used to create a JSON Object
 */
@XmlRootElement
public class DataObjectJaxBean {
	/**
	 * The label of the data object.
	 */
	private String label;

	/**
	 * The id the dataobjectinstance.
	 */
	private String id;
	/**
	 * The state inside the database of the dataobject
	 * which is stored in the table.
	 * The label not the id will be saved.
	 */
	private String state;

	/**
	 * An array of all dataAttributes belonging to this dataObject.
	 * Each attribute has an id, name, type and value.
	 */
	private DataAttributeJaxBean[] attributeConfiguration;

	public DataObjectJaxBean(DataObjectInstance instance) {
		setId(instance.getId());
		setLabel(instance.getDataClass().getName());
		setState(instance.getObjectLifecycleState().getName());

		List<DataAttributeJaxBean> attributes = new ArrayList<>(instance.getDataAttributeInstances().size());
		for (DataAttributeInstance dataAttributeInstance : instance.getDataAttributeInstances()) {
			DataAttributeJaxBean attributeInstance = new DataAttributeJaxBean(dataAttributeInstance);
			attributes.add(attributeInstance);
		}

		DataAttributeJaxBean[] attributesArray = attributes.toArray(new DataAttributeJaxBean[instance.getDataAttributeInstances().size()]);
		setAttributeConfiguration(attributesArray);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}

