package de.hpi.bpt.chimera.history.transportationbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;

@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.NONE)
public class ActivityLog extends LogEntry {

	public ActivityLog(AbstractActivityInstance activityInstance, State oldState, State newState) {
		super();
		this.setLabel(activityInstance.getControlNode().getName());
		this.setObjectReferenceId(activityInstance.getId());
		if (oldState == null)
			this.setOldValue(null);
		else
			this.setOldValue(oldState.getText());
		this.setNewValue(newState.getText());
	}
}
