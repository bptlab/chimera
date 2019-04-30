package de.hpi.bpt.chimera.rest.v2;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.Case;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.CaseStarter;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.SseNotifier;
import de.hpi.bpt.chimera.execution.exception.IllegalCaseModelIdException;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.AbstractRestService;
import de.hpi.bpt.chimera.rest.beans.event.ReceiveEventJaxBean;

/**
 * The event dispatcher class is responsible for manage registrations from
 * Events to RestQueries.
 */
@Path("eventdispatcher/")
public class EventRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(EventRestService.class);
	private final ExecutorService executorService = Executors.newFixedThreadPool(10);

	/**
	 * This method notifies that a certain event instance received Event was
	 * received. In addition the event instance will be terminated.
	 * 
	 * @param cmId
	 *            The id of a case model.
	 * @param caseId
	 *            - the id of case.
	 * @param requestId
	 *            - the id of the event instance receiving the event
	 * @param eventJson
	 *            - the json content for updating the data object the event
	 *            instance
	 * @return Response 202 because the Event was received by Chimera
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/instance/{instanceId}/events/{requestKey}")
	public Response receiveEvent(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("requestKey") String requestId, String eventJson) {
        log.info("Receiving an event...");
	    executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
                    MessageReceiveEventBehavior receiveBehavior = caseExecutioner.getRegisteredEventBehavior(requestId);
                    if (eventJson.isEmpty() || "{}".equals(eventJson)) {
                        receiveBehavior.setEventJson("");
                    } else {
                        receiveBehavior.setEventJson(eventJson);
                    }

                    AbstractEventInstance eventInstance = receiveBehavior.getEventInstance();
                    caseExecutioner.terminateDataControlNodeInstance(eventInstance);
                    SseNotifier.notifyRefresh();
                } catch (Exception e) {
                    log.error("Error while processing a received event", e);
                }
            }
        });
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Event received.\"}").build();
	}

	/**
	 * Start a new Case of a specific {@link CaseModel} by one of the case
	 * model's registered CaseStartTriggers.
	 * 
	 * @param cmId
	 *            The id of a case model.
	 * @param requestKey
	 *            - the id of the {@link CaseStartTrigger} receiving the event
	 * @param eventJson
	 *            - the json content for writing at the first data objects of
	 *            the new case
	 * @return Response 200 because the Event was received by Chimera
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/casestart/{requestKey}")
	public Response startCase(@PathParam("scenarioId") String cmId, @PathParam("requestKey") String requestKey, String eventJson) {
		log.info("An Event started a Case via REST-Interface.");
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					CaseModel cm = CaseModelManager.getCaseModel(cmId);
					Optional<CaseStartTrigger> caseStartTrigger = cm.getStartCaseTrigger().stream().filter(c -> c.getId().equals(requestKey)).findFirst();
					if (!caseStartTrigger.isPresent()) {
						String message = String.format("The case start trigger id: %s is not assigned", requestKey);
						log.error(message);
					}

					CaseExecutioner caseExecutioner = ExecutionService.createCaseExecutioner(cm, "Automatically Created Case");

					CaseStarter caseStarter = new CaseStarter(caseStartTrigger.get());
					caseStarter.startCase(eventJson, caseExecutioner);
					SseNotifier.notifyRefresh();
				} catch(IllegalCaseModelIdException e) {
					log.error("Could not start case from query", e);
				}
			}
		});
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Event received.\"}").build();
	}

	/**
	 * Get the case start triggers of a case model.
	 * 
	 * @param cmId - the id of the case model
	 * @return {@link CaseStartTrigger} ids and the respective event queries.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/casestart")
	public Response getCaseStartTriggers(@PathParam("scenarioId") String cmId) {
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);
			Object[] caseStartTriggers = cm.getStartCaseTrigger().toArray();
			JSONArray responseBody = new JSONArray();
			for (Object cst : caseStartTriggers) {
				String id = ((CaseStartTrigger) cst).getId();
				String plan = ((CaseStartTrigger) cst).getQueryExecutionPlan();
				JSONObject trigger = new JSONObject();
				trigger.put("id", id);
				trigger.put("plan", plan);
				responseBody.put(trigger);
			}
			return Response.ok(responseBody.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalCaseModelIdException e) {
			log.error("Could not get case model from query", e);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(e.getMessage()).build();
		}
	}

	
	/**
	 * Retrieve all registered events for an specific {@link Case}.
	 * 
	 * @param cmId
	 *            - Id of the CaseModel
	 * @param caseId
	 *            - Id of the Case
	 * @return Response 200 if the request succeeded with a list of the
	 *         registered events. Response 404 if the case was not found.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/instance/{instanceId}/events")
	public Response getRegisteredEvents(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
			List<ReceiveEventJaxBean> a = caseExecutioner.getRegisteredEventBehaviors().stream().map(ReceiveEventJaxBean::new).collect(Collectors.toList());
			JSONArray result = new JSONArray(a);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
