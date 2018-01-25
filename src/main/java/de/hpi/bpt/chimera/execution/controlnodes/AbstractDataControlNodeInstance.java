package de.hpi.bpt.chimera.execution.controlnodes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

@Entity
public abstract class AbstractDataControlNodeInstance extends ControlNodeInstance {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "JoinTableSelectedDataControlNode_DataObject", joinColumns = @JoinColumn(name = "ControlNodeInstance_Id"), inverseJoinColumns = @JoinColumn(name = "DataObject_Id"))
	private List<DataObject> selectedDataObjects;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "JoinTableOutputDataControlNode_DataObject", joinColumns = @JoinColumn(name = "ControlNodeInstance_Id"), inverseJoinColumns = @JoinColumn(name = "DataObject_Id"))
	private List<DataObject> outputDataObjects;

	/**
	 * for JPA only
	 */
	public AbstractDataControlNodeInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	public AbstractDataControlNodeInstance(AbstractControlNode controlNode, FragmentInstance fragmentInstance) {
		super(controlNode, fragmentInstance);
		this.selectedDataObjects = new ArrayList<>();
		this.outputDataObjects = new ArrayList<>();
	}

	@Override
	public AbstractDataControlNode getControlNode() {
		return (AbstractDataControlNode) super.getControlNode();
	}

	public List<DataObject> getSelectedDataObjects() {
		return selectedDataObjects;
	}

	public void setSelectedDataObjects(List<DataObject> selectedDataObjects) {
		this.selectedDataObjects = selectedDataObjects;
	}

	public List<DataObject> getOutputDataObjects() {
		return outputDataObjects;
	}

	public void setOutputDataObjects(List<DataObject> outputDataObjects) {
		this.outputDataObjects = outputDataObjects;
	}

}
