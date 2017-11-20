package de.hpi.bpt.chimera.jcore.rest.TransportationBeans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;

@XmlRootElement
public class DataNodeJaxBean {
	/**
	 * The label of the datanode.
	 */
	private String label;

	/**
	 * The id the datanode.
	 */
	// private String id;

	/**
	 * The dataclass of the datanode. The label not the id will be saved.
	 */
	private String dataclass;
	/**
	 * The state of the dataclass. The label not the id will be saved.
	 */
	private String state;

	/**
	 * An array of all dataAttributes belonging to this dataObject. Each
	 * attribute has an id, name, type and value.
	 */
	private DataAttributeJaxBean[] attributeConfiguration;

	public DataNodeJaxBean(AtomicDataStateCondition condition) {
		// setId(condition.getId());
		setLabel(condition.getDataClassName());
		setDataclass(condition.getDataClassName());
		setState(condition.getStateName());

		List<DataAttributeJaxBean> attributes = new ArrayList<>(condition.getDataClass().getDataAttributes().size());
		for (DataAttribute dataAttribute : condition.getDataClass().getDataAttributes()) {
			DataAttributeJaxBean attribute = new DataAttributeJaxBean(dataAttribute);
			attributes.add(attribute);
		}

		DataAttributeJaxBean[] attributesArray = attributes.toArray(new DataAttributeJaxBean[condition.getDataClass().getDataAttributes().size()]);
		setAttributeConfiguration(attributesArray);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
/*
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
*/
	public String getDataclass() {
		return dataclass;
	}

	public void setDataclass(String name) {
		this.dataclass = name;
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
