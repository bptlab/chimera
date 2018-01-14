package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

public abstract class AbstractDataControlNodeInstance extends ControlNodeInstance {
	private List<DataObject> selectedDataObjects;
	private List<DataObject> outputDataObjects;

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
