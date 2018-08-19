package de.hpi.bpt.chimera.rest.beans.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

@XmlRootElement
public class AvailableOutputJaxBean {
	private List<AvailableDataObjectJaxBean> dataobjects;
	private List<AvailableDataClassJaxBean> dataclasses;

	public AvailableOutputJaxBean(AbstractActivityInstance activityInstance) {
		dataobjects = new ArrayList<>();
		Map<DataClass, List<ObjectLifecycleState>> possibleTransitions = activityInstance.getControlNode().getPostCondition().getDataClassToObjectLifecycleStates();
		List<DataObject> workingItems = activityInstance.getSelectedDataObjects();
		
		for (DataObject workingItem : workingItems) {
			DataClass dataclass = workingItem.getDataClass();

			if (!possibleTransitions.containsKey(dataclass)) {
				// If the dataclass occurs in the input but not in the output
				// condition
				continue;
			}
			// If the dataclass occurs in the input and in the output condition
			ObjectLifecycleState olcState = workingItem.getObjectLifecycleState();
			List<ObjectLifecycleState> availableOlcStates = possibleTransitions.get(dataclass).stream()
																.filter(s -> s.isSuccessorOf(olcState))
																.collect(Collectors.toList());
			AvailableDataObjectJaxBean availableDataObjectJaxBean = new AvailableDataObjectJaxBean(workingItem, availableOlcStates);
			dataobjects.add(availableDataObjectJaxBean);
			possibleTransitions.remove(dataclass);
		}
		
		dataclasses = new ArrayList<>();
		// If the dataclass occurs in the output but not in the input condition
		for (Map.Entry<DataClass, List<ObjectLifecycleState>> remainingTransition : possibleTransitions.entrySet()) {
			DataClass dataclass = remainingTransition.getKey();
			List<ObjectLifecycleState> availableOlcStates = remainingTransition.getValue();
			AvailableDataClassJaxBean availableDataclassJaxBean = new AvailableDataClassJaxBean(dataclass, availableOlcStates);
			dataclasses.add(availableDataclassJaxBean);
		}
		
		setDataobjects(dataobjects);
		setDataclasses(dataclasses);
	}

	public List<AvailableDataObjectJaxBean> getDataobjects() {
		return dataobjects;
	}

	public void setDataobjects(List<AvailableDataObjectJaxBean> dataobjects) {
		this.dataobjects = dataobjects;
	}

	public List<AvailableDataClassJaxBean> getDataclasses() {
		return dataclasses;
	}

	public void setDataclasses(List<AvailableDataClassJaxBean> dataclasses) {
		this.dataclasses = dataclasses;
	}
}
