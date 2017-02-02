package de.hpi.bpt.chimera.jcore.flowbehaviors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.executionbehaviors.DataAttributeWriter;

/**
 * Defines the outgoing behavior of a web service task.
 * FIXME: Merge common behavior with TaskOutputBehavior, now it is greatly redundant.
 *        Instead this should let TaskOutputBehavior do its job and then set the attributes
 *        values of output data objects based on their json-path expressions
 */
public class WebServiceTaskOutgoingBehavior extends TaskOutgoingBehavior {

  /**
   * Stores the response of the web service as a Json String.
   */
  private String responseInJson;
  private ActivityInstance wsTask;
  private static Logger log = Logger.getLogger(WebServiceTaskOutgoingBehavior.class);

  public WebServiceTaskOutgoingBehavior(int activityId, ScenarioInstance scenarioInstance, int fragmentInstanceId,
      ActivityInstance activityInstance) {
    super(activityId, scenarioInstance, fragmentInstanceId, activityInstance);
    this.wsTask = activityInstance;
  }

  /**
   * Terminates the web service task.
   */
  @Override
  public void terminate() {
    writeDataObjects();
    unregisterAttachedEvents();
    wsTask.setState(State.TERMINATED);
    enableFollowing();
  }

  /**
   * Stores the result of the webservice call into a data object.
   * FIXME: Assumption that WSTask has no input DO
   */
  private void writeDataObjects() {
    // get output set
    List<Integer> outputSetIds = new DbDataFlow().getOutputSetsForControlNode(wsTask.getControlNodeId());
    // check that output set is unique
    if (outputSetIds.size() > 1) {
      log.error("Service tasks require an unique output set, received data can not be stored.");
    } else if (outputSetIds.isEmpty()) {
      log.info("Web service task has no output set, received data can not be stored.");
    } else {
      // create data objects if necessary
      List<DataObject> createdDataObjects = new ArrayList<>();
      Map<Integer, Integer> dataClassIdToStateIdMap = new DbDataNode().getDataSetClassToStateMap(outputSetIds.get(0));
      DataManager dataManager = wsTask.getScenarioInstance().getDataManager();
      for (Integer dataClassId : dataClassIdToStateIdMap.keySet()) {
        DataObject dataObject = dataManager.initializeDataObject(dataClassId, dataClassIdToStateIdMap.get(dataClassId));
        createdDataObjects.add(dataObject);
      }
      if (responseInJson == null) { // we have a response
        log.info("No response found, received data can not be stored.");
      } else {
        // get jsonpath expressions for attributes
        List<DataAttributeInstance> attributesToCheck = new ArrayList<>();
        for (DataObject dataObject : createdDataObjects) {
          attributesToCheck.addAll(dataObject.getDataAttributeInstances());
        }
        DataAttributeWriter dataAttributeWriter = new DataAttributeWriter(wsTask.getControlNodeId(),
            wsTask.getControlNodeInstanceId(), wsTask.getScenarioInstance());
        dataAttributeWriter.writeDataAttributesFromJson(responseInJson, attributesToCheck);
      }
    }
  }

  /**
   * A way for the WebServiceTaskExecutionBehavior to set the response.
   * @param response A Json String
   */
  public void setResponse(String response) {
    responseInJson = response;
  }

}
