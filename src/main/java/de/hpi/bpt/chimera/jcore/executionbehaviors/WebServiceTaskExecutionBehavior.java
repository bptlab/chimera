package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.controlnodes.DbWebServiceTask;
import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This is the execution behavior for webservice tasks.
 */
public class WebServiceTaskExecutionBehavior extends TaskExecutionBehavior {
	private static Logger log = Logger.getLogger(
			WebServiceTaskExecutionBehavior.class);

	/**
	 * DB ConnectionWrapper class.
	 */
	private DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();

	/**
	 * Initializes the webservice task.
	 *
	 * @param activityInstanceId The id of the webservice task.
	 * @param scenarioInstance    The instance of the ScenarioInstance.
	 * @param controlNodeInstance The AbstractControlNodeInstance (ActivityInstance).
	 */
	public WebServiceTaskExecutionBehavior(
            int activityInstanceId, ScenarioInstance scenarioInstance,
            AbstractControlNodeInstance controlNodeInstance) {
		super(activityInstanceId, scenarioInstance, controlNodeInstance);
	}

	@Override
    public void execute() {
        WebTarget target = buildTarget();
		Response response = executeWebserviceRequest(target);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            writeDataObjects(response);
        } else {
            log.warn("Web service task did not execute properly");
        }
        this.setCanTerminate(true);
	}

    private WebTarget buildTarget() {
        String link = dbWebServiceTask
                .getUrl(getControlNodeInstance().getControlNodeId());
        String replacedLink = insertDataObjectValues(link,
                new ArrayList<>(this.getScenarioInstance().getDataAttributeInstances().values()));

        Client client = ClientBuilder.newClient();
        // Split url into link part and query param part
        String[] url = replacedLink.split("\\?");
        WebTarget webResource = client.target(url[0]);
        if (url.length > 1) {
            String[] params = url[1].split("&");
            for (String param : params) {
                String[] values = param.split("=");
                webResource = webResource.queryParam(values[0], values[1]);
            }
        }
        return webResource;
    }

    /**
     * This method is responsible for filling in values of dataobjects into a string
     * @param toReplace String which contains #dataObjectName.dataattributeName patterns
     * @param dataAttributes List of data attributes which should be used to fill in the
     * @return String with filled in parameters
     */
    private String insertDataObjectValues(
            String toReplace, List<DataAttributeInstance> dataAttributes) {
        String replacedLink = toReplace;
        for (DataAttributeInstance dataAttributeInstance : dataAttributes) {
            replacedLink = replacedLink.replace("#" + (dataAttributeInstance.getDataObject())
                            .getName() + "."
                            + dataAttributeInstance.getName(),
                    dataAttributeInstance.getValue().toString());
        }
        return replacedLink;
    }

    private Response executeWebserviceRequest(WebTarget webResource) {
        Invocation.Builder invocationBuilder = webResource.request(
                MediaType.APPLICATION_JSON);

        List<DataAttributeInstance> dataAttributeInstances = new ArrayList<>(getScenarioInstance()
                .getDataAttributeInstances().values());

        String method = dbWebServiceTask.getMethod(getControlNodeInstance().getControlNodeId());
        if ("POST".equals(method) || "PUT".equals(method)) {
            String post = dbWebServiceTask.getPOSTBody(getControlNodeInstance().getControlNodeId());
            String bodyWithAttributeValues = insertDataObjectValues(post, dataAttributeInstances);
            return invocationBuilder.post(Entity.json(bodyWithAttributeValues));
        } else {
            return invocationBuilder.get();
        }
    }

    private void writeDataObjects(Response response) {
        String json = response.readEntity(String.class);
        AbstractControlNodeInstance node = this.getControlNodeInstance();
        DataAttributeWriter dataAttributeWriter = new DataAttributeWriter(
                node.getControlNodeId(),
                node.getControlNodeInstanceId(),
                this.getScenarioInstance());
        List<DataAttributeInstance> dataAttributeInstances = new ArrayList<>();
        getPossibleDataObjects().values().stream().filter(x -> !x.isEmpty())
                .forEach(x -> dataAttributeInstances.addAll(
                        x.get(new Random().nextInt(x.size())).getDataAttributeInstances()));
        dataAttributeWriter.writeDataAttributesFromJson(json, dataAttributeInstances);
    }

    private Map<Integer, List<DataObject>> getPossibleDataObjects() {
        Map<Integer, List<DataObject>> possibleDataObjects = new HashMap<>();
        DbDataFlow dbDataFlow = new DbDataFlow();
        List<Integer> followingDataClasses = dbDataFlow.getFollowingDataClassIds(
                this.getControlNodeInstance().getControlNodeId());
        for (Integer dataClass : followingDataClasses) {
            possibleDataObjects.put(dataClass, new ArrayList<>());
        }
        List<DataObject> dataObjects = getScenarioInstance().getDataManager().getDataObjects();
        dataObjects.stream().filter(x -> followingDataClasses.contains(x.getDataClassId()))
                .forEach(x -> possibleDataObjects.get(x.getDataClassId()).add(x));
        return possibleDataObjects;
    }
}
