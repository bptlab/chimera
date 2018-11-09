package de.hpi.bpt.chimera.rest.beans.activity;

import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OutputJaxBean {
    private List<DataJaxBean> output;

    public OutputJaxBean(AbstractActivityInstance activityInstance) {
        Map<DataClass, List<ObjectLifecycleState>> possibleTransitions = activityInstance.getControlNode().getPostCondition().getDataClassToObjectLifecycleStates();
        List<DataObject> workingItems = activityInstance.getSelectedDataObjects();
        setOutput(possibleTransitions.entrySet().stream()
                    .map(t -> new DataJaxBean(t.getKey(), t.getValue(), workingItems))
                    .collect(Collectors.toList()));
    }

    public List<DataJaxBean> getOutput() {
        return output;
    }

    public void setOutput(List<DataJaxBean> output) {
        this.output = output;
    }
}
