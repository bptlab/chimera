package de.hpi.bpt.chimera.execution.activity;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hpi.bpt.chimera.execution.DataObject;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.execution.DataAttributeInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.WebServiceTask;

public class WebServiceTaskInstance extends AbstractActivityInstance {
	private Response webServiceResponse;

	public WebServiceTaskInstance(WebServiceTask webServiceTask, FragmentInstance fragmentInstance) {
		super(webServiceTask, fragmentInstance);
		this.setAutomaticTask(true);
	}

	@Override
	public void execute() {
		WebTarget target = buildTarget();
		// log.info("Target for web service call constructed: " + target.toString());
		Response response = executeWebserviceRequest(target);
		if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			setWebServiceResponse(response);
		} else {
			// log.warn("Web service task did not begin properly");
		}
	}

	/**
	 * Construct the target of the web service call. Query params encoded in the
	 * URL are added to the target object.
	 *
	 * @return a WebTarget
	 */
	private WebTarget buildTarget() {
		String link = getControlNode().getWebServiceUrl();
		String replacedLink = insertDataObjectValues(link);

		return parseWebTarget(replacedLink);
	}

	/**
	 * This method is responsible for filling in values of selected DataObject,
	 * which were selected at the beginning of the task, into a string.
	 *
	 * @param toReplace
	 *            String which contains #dataObjectName.dataattributeName
	 *            patterns
	 * @return replaced link
	 */
	private String insertDataObjectValues(String toReplace) {
		String replacedLink = toReplace;
		for (DataObject dataObject : getSelectedDataObjects()) {
			for (DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstances()) {
				String toReplaceSpecified = String.format("#%s.%s", dataObject.getDataClass().getName(), dataAttributeInstance.getDataAttribute().getName());
				replacedLink = replacedLink.replace(toReplaceSpecified, dataAttributeInstance.getValue().toString());
			}
		}
		return replacedLink;
	}

	/**
	 * Receive a WebTarget by parsing url and query params out of a given link.
	 * 
	 * @param link
	 *            - which is used for parsing the url and query params.
	 * @return a WebTarget which referrs to the given link
	 */
	private WebTarget parseWebTarget(String link) {
		Client client = ClientBuilder.newClient();
		// Split url into link part and query param part
		String[] url = link.split("\\?");
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
	 * Execute the WebService Request with the defined WebServiceMethod (GET, POST, PUT) and modified request body of the WebServiceTask.
	 *
	 * @param webResource
	 * @return
	 */
	private Response executeWebserviceRequest(WebTarget webResource) {
		Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
		String webServiceMethod = getControlNode().getWebServiceMethod().toUpperCase();
		switch (webServiceMethod) {
		case "GET":
			return invocationBuilder.get();
		case "POST":
		case "PUT":
			String postBody = getControlNode().getWebServiceBody();
			String replacedPostBody = this.insertDataObjectValues(postBody);
			return invocationBuilder.post(Entity.json(replacedPostBody));
		default:
			throw new IllegalArgumentException(webServiceMethod + " is not implemented yet");
			// return invocationBuilder.method(webServiceMethod);
		}
	}

	@Override
	public void terminate() {
		if (!getState().equals(State.RUNNING)) {
			// log.info(String.format("%s not terminated, because the activity isn't in state RUNNING", this.getControlNode().getName()));
			return;
		}
		if (getControlNode().getPostCondition().getConditionSets().size() > 1) {
			// log.error("Service tasks require an unique output set, received data can not be stored.");
			return;
		}
		if (getControlNode().getPostCondition().getConditionSets().isEmpty()) {
		    // log.info("Web service task has no output set, received data can not be stored.");
			return;
		}
		if (webServiceResponse == null) { // we have a response
	        //log.info("No response found, received data can not be stored.");
	        return;
	    }

	}

	@Override
	public WebServiceTask getControlNode() {
		return (WebServiceTask) super.getControlNode();
	}

	public Response getWebServiceResponse() {
		return webServiceResponse;
	}

	public void setWebServiceResponse(Response webServiceResponse) {
		this.webServiceResponse = webServiceResponse;
	}
}
