package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractDataControlNode extends AbstractControlNode {
	@XmlElement(name = "bpmn:dataInputAssociation")
	protected List<DataInputAssociation> dataInputAssociations = new ArrayList<>();
	@XmlElement(name = "bpmn:dataOutputAssociation")
	protected List<DataOutputAssociation> dataOutputAssociations = new ArrayList<>();

	public List<DataOutputAssociation> getDataOutputAssociations() {
		return dataOutputAssociations;
	}

	public List<DataInputAssociation> getDataInputAssociations() {
		return dataInputAssociations;
	}

	/**
	 * 
	 * @return List of Ids of incoming DataNodeObjectReferences
	 */
	public List<String> getIncomingDataNodeObjectReferences() {
		List<String> incomingDataNodeObjectReferences = new ArrayList<>();
		for (DataInputAssociation dataInputAssociation : dataInputAssociations) {
			incomingDataNodeObjectReferences.add(dataInputAssociation.getSourceDataObjectRef());
		}
		return incomingDataNodeObjectReferences;
	}

	/**
	 * 
	 * @return List of Ids of outgoing DataNodeObjectReferences
	 */
	public List<String> getOutgoingDataNodeObjectReferences() {
		List<String> outgoingDataNodeObjectReferences = new ArrayList<>();
		for (DataOutputAssociation dataOutputAssociation : dataOutputAssociations) {
			outgoingDataNodeObjectReferences.add(dataOutputAssociation.getTargetDataObjectRef());
		}
		return outgoingDataNodeObjectReferences;
	}
}
