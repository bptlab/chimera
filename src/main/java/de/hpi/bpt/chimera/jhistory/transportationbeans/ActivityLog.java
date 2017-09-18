package de.hpi.bpt.chimera.jhistory.transportationbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;

@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.NONE)
public class ActivityLog extends LogEntry {

	public ActivityLog(AbstractActivityInstance activityInstance, State oldState, State newState) {
		super();
		this.setLabel(activityInstance.getActivity().getName());
		this.setObjectReferenceId(activityInstance.getId());
		if (oldState == null)
			this.setOldValue("no state");
		else
			this.setOldValue(oldState.getText());
		this.setNewValue(newState.getText());
	}
}
