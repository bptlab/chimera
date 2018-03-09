package de.hpi.bpt.chimera.execution.controlnodes.activity;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstanceWriter;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.WebServiceTask;

@Entity
public class WebServiceTaskInstance extends AbstractActivityInstance {
	private static final Logger log = Logger.getLogger(WebServiceTaskInstance.class);

	private String webServiceJson;

	/**
	 * for JPA only
	 */
	public WebServiceTaskInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public WebServiceTaskInstance(WebServiceTask webServiceTask, FragmentInstance fragmentInstance) {
		super(webServiceTask, fragmentInstance);
	}

	@Override
	public void execute() {
		WebTarget target = buildTarget();
		log.info("Target for web service call constructed: " + target.toString());
		Response response = executeWebserviceRequest(target);
		if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			String webServiceResponseJson = response.readEntity(String.class);
			setWebServiceResponse(webServiceResponseJson);
		} else {
			log.error("Web service task did not execute properly, status code: " + response.getStatusInfo().getStatusCode());
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
		String replacedLink = replaceVariableExpressions(link);

		return parseWebTarget(replacedLink);
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
			String replacedPostBody = this.replaceVariableExpressions(postBody);
			if ("POST".equals(webServiceMethod)) {
				return invocationBuilder.post(javax.ws.rs.client.Entity.json(replacedPostBody));
			} else {
				return invocationBuilder.put(javax.ws.rs.client.Entity.json(replacedPostBody));
			}
		default:
			throw new IllegalArgumentException(webServiceMethod + " is not implemented yet");
			// return invocationBuilder.method(webServiceMethod);
		}
	}

	@Override
	public void terminate() {
		if (!getState().equals(State.RUNNING)) {
			log.info(String.format("%s not terminated, because the activity isn't in state RUNNING", this.getControlNode().getName()));
			return;
		}
		if (!getControlNode().hasPostCondition()) {
			log.info("Web service task has no output set, received data can not be stored.");
			return;
		}
		if (webServiceJson == null) {
			log.info("No response found, received data can not be stored.");
	        return;
	    }

		for (DataObject dataObject : getOutputDataObjects()) {
			AtomicDataStateCondition condition = dataObject.getCondition();
			Map<DataAttribute, String> dataAttributeToJsonPath = getControlNode().getJsonPathMapping().get(condition);
			DataAttributeInstanceWriter.writeDataAttributeInstances(dataObject, dataAttributeToJsonPath, webServiceJson);
		}

		super.terminate();
	}

	@Override
	public WebServiceTask getControlNode() {
		return (WebServiceTask) super.getControlNode();
	}

	public String getWebServiceResponse() {
		return webServiceJson;
	}

	public void setWebServiceResponse(String webServiceResponse) {
		this.webServiceJson = webServiceResponse;
	}
}
