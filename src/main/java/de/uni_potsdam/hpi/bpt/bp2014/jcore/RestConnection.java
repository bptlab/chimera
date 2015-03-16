package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryService;
import de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.LinkedList;

import static de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil.JsonWrapperHashMap;
import static de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil.JsonWrapperLinkedList;


/**
 * ********************************************************************************
 *
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 *
 * ******************************************************************
 *
 * Copyright © All Rights Reserved 2014 - 2015
 *
 * Please be aware of the License. You may found it in the root directory.
 *
 * **********************************************************************************
 */

/**
 *
 */
@SuppressWarnings("IfCanBeSwitch")
@Path("interface/v1/en/") //defining also version and language
public class RestConnection {
    private final ExecutionService executionService = new ExecutionService();
    private final HistoryService historyService = new HistoryService();

    /* ######################################################
     *
     * HTTP GET REQUESTS
     *
     * #######################################################
     */

    /**
     * GET  information about an activityID
     *
     * @param scenarioID the ID of the related scenario
     * @return returns JSON containing details for scenarios
     */

    @GET
    @Path("scenario/{scenarioID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showDetailsForScenarios(@PathParam("scenarioID") int scenarioID) {
        //if 0 as scenarioID is provided, list all available scenarioIDs
        if (scenarioID == 0) {
            LinkedList<Integer> scenarioIDs = executionService.getAllScenarioIDs();
            /*
            if (scenarioIDs.size() == 0) {
                return Response.serverError().entity("Error: not correct Scenario ID").build();//no scenarios present
            }*/
            String jsonRepresentation = JsonWrapperLinkedList(scenarioIDs);

            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
            //otherwise display the label for the scenarioID
        } else {
            String label = executionService.getScenarioName(scenarioID);

            if (label.equals(""))
                return Response.serverError().entity("Error: not correct scenarioID").build();//no activity with this id present

            return Response.ok("{\"label\":\"" + label + "\"}", MediaType.APPLICATION_JSON).build();
        }
    }


    /**
     * GET all scenarioInstanceIDs  of a scenario
     *
     * @param scenarioID the ID of the related scenario
     * @param instanceID the ID of the related scenario instance
     * @return details for a scenario instance or a error code
     */

    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarioInstances(@PathParam("scenarioID") int scenarioID, @PathParam("instanceID") int instanceID) {
        //if instanceID is null, display all available instances for the mentioned scenarioID
        if (instanceID == 0) {
            if (!executionService.existScenario(scenarioID)) {
                return Response.serverError().entity("Error: not a correct scenario").build();
            }
            LinkedList<Integer> scenarioIDs = executionService.listAllScenarioInstancesForScenario(scenarioID);
            /*
            if (scenarioIDs.size() == 0) {
                return Response.serverError().entity("Error: not correct instance ID").build(); //no instances present
            }*/
            String jsonRepresentation = JsonWrapperLinkedList(scenarioIDs);

            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();

            //otherwise display details for this instanceID
            //TODO: implement returning of instance details (label, timestamp..)
        } else {
            String label = executionService.getScenarioNameForScenarioInstance(instanceID);
            //int scenarioID = executionService.getScenarioIDForScenarioInstance(scenarioInstanceID); //get the scenarioID for this instance
            if (label.equals("")) {
                return Response.serverError().entity("Error: not correct instanceID").build();//no activity with this id present
            }
            return Response.ok("{\"label\":\"" + label + "\"}", MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     * GET  details for an activityID
     *
     * @param scenarioID         the ID of the related scenario
     * @param instanceID         the ID of the related scenario instance
     * @param activityinstanceID the ID of the related activity instance
     * @param status             the new status of the activity which is supposed to be updated
     * @param limit              a limit which is not used yet but defined through API specs
     * @return details for an activity
     * <p/>
     * TODO: Limit has to be implemented
     */

    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activityinstance/{activityinstanceID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showDetailsForActivity(@PathParam("scenarioID") int scenarioID, @PathParam("instanceID") int instanceID, @PathParam("activityinstanceID") int activityinstanceID, @QueryParam("status") String status, @QueryParam("limit") String limit) {
        //display all open activities if ID is 0
        if (activityinstanceID == 0) {
            // if status is not set, set default value
            if (status == null) {
                status = "enabled"; //set status enabled for default value
            }
            // if status is enabled -> return all enabled activities
            if (status.equals("enabled")) { //open activities;

                if (!executionService.openExistingScenarioInstance(scenarioID, instanceID)) {
                    return Response.serverError().entity("Error: not a correct scenario instance: !executionService.openExistingScenarioInstance").build();
                }
                LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(instanceID);
                HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(instanceID);
                // iff no open activities present return {empty}
               /* if (enabledActivitiesIDs.size() == 0) {
                    return Response.serverError().entity("Error: not correct scenarioInstance ID: enabledActivitiesIDs.size() == 0").build();
                }*/
                String jsonRepresentation = JsonWrapperHashMap(enabledActivitiesIDs, labels);

                return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
                // if status is terminated -> return all terminated activities
            } else if (status.equals("terminated")) {

                if (!executionService.existScenarioInstance(scenarioID, instanceID)) {
                    return Response.serverError().entity("Error: not a correct scenario instance").build();
                }
                LinkedList<Integer> terminatedActivities = historyService.getTerminatedActivitiesForScenarioInstance(instanceID);
                HashMap<Integer, String> labels = historyService.getTerminatedActivityLabelsForScenarioInstance(instanceID);
                //if no closed activities present -> return {empty}
                // TODO: Don't throw an error if there are no terminated instances
                /*if (terminatedActivities.size() == 0) {
                    return Response.serverError().entity("Error: not correct activity ID").build();
                }*/
                String jsonRepresentation = JsonWrapperHashMap(terminatedActivities, labels);

                return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
                // if status is running -> return all running activities
            } else if (status.equals("running")) { //running activities;

                if (!executionService.openExistingScenarioInstance(scenarioID, instanceID)) {
                    return Response.serverError().entity("Error: not a correct scenario instance").build();
                }
                LinkedList<Integer> enabledActivitiesIDs = executionService.getRunningActivitiesIDsForScenarioInstance(instanceID);
                HashMap<Integer, String> labels = executionService.getRunningActivityLabelsForScenarioInstance(instanceID);
                // if no running activities present -> return {empty}
                /*if (enabledActivitiesIDs.size() == 0) {
                    return Response.serverError().entity("Error: not correct Activity ID").build();
                }*/
                String jsonRepresentation = JsonWrapperHashMap(enabledActivitiesIDs, labels);

                return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();

            }
            // if status is not satisfying declared (!= {enabled,terminated,running} thorugh error
            return Response.serverError().entity("Error: status not clear").build();

            // display details for this activityID
            //TODO: implement returning of timestamp and additional details
            //if activity ID is != 0 then display details for this activity
        } else {
            String label = executionService.getLabelForControlNodeID(activityinstanceID);
            //if no activity with this id present
            if (label.equals("")) {
                return Response.serverError().entity("Error: not correct Activity ID or empty Label").build();
            }
            return Response.ok("{\"label\":\"" + label + "\"}", MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     * GET Dataobjects and States
     *
     * @param scenarioID         the ID of the related scenario
     * @param scenarioInstanceID the ID of the related scenario instance
     * @param status             the new status of the activity which is supposed to be updated
     * @return json representation of all available dataobjects
     */

    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/dataobject/{dataobjectID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showDataObjects(@PathParam("scenarioID") int scenarioID, @PathParam("instanceID") int scenarioInstanceID, @PathParam("dataobjectID") int dataobjectID, @QueryParam("status") String status, @QueryParam("limit") String limit) {

        if (dataobjectID == 0) {
            if (!executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID)) {
                return Response.serverError().entity("Error: there is no existing open scenario instance").build();
            }
            LinkedList<Integer> dataObjects = executionService.getAllDataObjectIDs(scenarioInstanceID);
            HashMap<Integer, String> states = executionService.getAllDataObjectStates(scenarioInstanceID);
            HashMap<Integer, String> labels = executionService.getAllDataObjectNames(scenarioInstanceID);
            //if no dataobject is available -> return {empty}
            /*if (dataObjects.size() == 0) {
                return Response.serverError().entity("Error: not correct dataobject ID").build();
            }*/
            String jsonRepresentation = JsonUtil.JsonWrapperMultipleHashMap(dataObjects, labels, states);

            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.serverError().entity("Error: not correct dataobject ID").build();
        }
    }


    @GET
    @Path("scenario/{scenarioID}/emailtask/{emailtaskID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmailConfiguration(@PathParam("scenarioID") int scenarioID, @PathParam("emailtaskID") int emailtaskID) {
        DbEmailConfiguration dbEmailConfiguration = new DbEmailConfiguration();
        if (emailtaskID == 0) {
            String jsonRepresentation = JsonUtil.JsonWrapperLinkedList(dbEmailConfiguration.getAllEmailTasksForScenario(scenarioID));
            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
        } else {
            String receiver = dbEmailConfiguration.getReceiverEmailAddress(emailtaskID);
            if (receiver.equals("")) {
                return Response.serverError().entity("Error: there is no email configuration").build();
            }
            String message = dbEmailConfiguration.getMessage(emailtaskID);
            String subject = dbEmailConfiguration.getSubject(emailtaskID);
            String jsonRepresentation = "{\"receiver\":\"" + receiver + "\", \"subject\":\"" + subject + "\",\"message\":\"" + message + "\"}";

            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
        }
    }


    /* #############################################################################
     *
     * HTTP POST REQUEST
     *
     * #############################################################################
     */

    /**
     * POST to start a new instance
     *
     * @param scenarioID defines the ID of the scenario
     * @return error status or id of new instance
     */

    @POST
    @Path("scenario/{scenarioID}/")
    public int startNewScenarioInstance(@PathParam("scenarioID") int scenarioID) {
        // if scenario exists
        if (executionService.existScenario(scenarioID)) {
            //return the ID of new instanceID
            return executionService.startNewScenarioInstance(scenarioID);
            // else scenario does not exist
        } else {
            return -1;
        }
    }

    /**
     * POST to change status of an activity
     *
     * @param scenarioID         the ID of the related scenario
     * @param scenarioInstanceID the ID of the related scenario instance
     * @param activityInstanceID the ID of the related activity instance
     * @param status             the new status of the activity which is supposed to be updated
     * @return true or false
     */
    @POST
    // TODO: Fix path names
    @Path("scenario/{scenarioID}/instance/{instanceID}/activityinstance/{activityinstanceID}/")
    public Boolean doActivity(@PathParam("scenarioID") String scenarioID,
                              @PathParam("instanceID") int scenarioInstanceID,
                              @PathParam("activityinstanceID") int activityInstanceID,
                              @QueryParam("status") String status) {
        Boolean result;
        executionService.openExistingScenarioInstance(new Integer(scenarioID), scenarioInstanceID);
        // check on status, if begin -> start the activity
        if (status.equals("begin")) {
            result = executionService.beginActivity(scenarioInstanceID, activityInstanceID);
            if (result) {
                return true;
            } else {
                System.err.print("ERROR within the executionService.beginActivity during REST Call doActivity");
                return false;
            }
            // otherwise when terminate -> terminate the activity
        } else if (status.equals("terminate")) {
            result = executionService.terminateActivity(scenarioInstanceID, activityInstanceID);
            if (result) {
                return true;
            } else {
                System.err.print("ERROR within the executionService.terminateActivity during REST Call doActivity");
                return false;
            }
        }
        System.err.print("ERROR: no status defined " + status);
        return false;
    }

    /**
     * @param emailtaskID id of related emailtask
     * @param input       HTTP body as json which is retrieved by the REST interface
     * @return boolean
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("config/emailtask/{emailtaskID}/")
    public boolean updateEmailConfiguration(
            @PathParam("emailtaskID") int emailtaskID,
            final EmailConfigJaxBean input) {

        DbEmailConfiguration dbEmailConfiguration = new DbEmailConfiguration();
        dbEmailConfiguration.setEmailConfiguration(emailtaskID,
                input.receiver, input.subject, input.content);
        return true;
    }


    /* #############################################################################
     *
     * Helper
     *
     * #############################################################################
     */

    /**
     * This is a data class for the email configuration.
     * It is used by Jersey to deserialize JSON.
     * Also it can be used for tests to provide the correct contents.
     * This class in particular is used by the POST for the email configuration.
     * See the {@link #updateEmailConfiguration(int,
     * de.uni_potsdam.hpi.bpt.bp2014.jcore.RestConnection.EmailConfigJaxBean)
     * updateEmailConfiguration} method for more information.
     */
    @XmlRootElement
    public static class EmailConfigJaxBean {
        /**
         * The receiver of the email.
         * coded as an valid email address (as String)
         */
        public String receiver;
        /**
         * The subject of the email.
         * Could be any String but null.
         */
        public String subject;
        /**
         * The content of the email.
         * Could be any String but null.
         */
        public String content;
    }
}
