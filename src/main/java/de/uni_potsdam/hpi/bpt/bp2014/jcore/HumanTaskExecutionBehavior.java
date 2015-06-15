package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;

import java.util.LinkedList;
import java.util.Map;


public class HumanTaskExecutionBehavior extends TaskExecutionBehavior {

    public HumanTaskExecutionBehavior(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        super(activityInstance_id, scenarioInstance, controlNodeInstance);
    }

    @Override
    public void execute() {
        DbDataFlow dbDataFlow = new DbDataFlow();
        //allow an activity to terminate if it has no data attributes in output.
        if (dbDataFlow.getOutputSetsForControlNode(controlNodeInstance.getControlNode_id()).isEmpty()) {
            this.setCanTerminate(true);
        } else if (scenarioInstance.getDataAttributeInstances().isEmpty()) {
            this.setCanTerminate(true);
        } else {
            LinkedList<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(controlNodeInstance.getControlNode_id());
            int outputSet = outputSets.getFirst();
            DbDataNode dbDataNode = new DbDataNode();
            LinkedList<DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(outputSet);
            boolean hasAttribute = false;
            for (DataObject dataObject : dataObjects) {
                if (this.dataObjectHasAttributes(dataObject)) {
                    hasAttribute = true;
                    break;
                }
            }
            if (!hasAttribute) {
                this.setCanTerminate(true);
            }
        }
    }

    private boolean dataObjectHasAttributes(DataObject dataObject) {
        for (DataAttributeInstance dataAttributeInstance : scenarioInstance.getDataAttributeInstances().values()) {
            if (dataAttributeInstance.getDataObjectInstance().getDataObject_id() == dataObject.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setDataAttributeValues(Map<Integer, String> values) {
        for (Integer i : values.keySet()) {
            DataAttributeInstance dataAttributeInstance = scenarioInstance.getDataAttributeInstances().get(i);
            dataAttributeInstance.setValue(values.get(i));
        }
        this.setCanTerminate(true);
    }


}
