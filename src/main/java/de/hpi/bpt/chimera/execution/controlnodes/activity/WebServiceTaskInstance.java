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
	 * If the input string contains a variable expression, i.e. {@code #DataClass} or {@code #DataClass.attributeName}, 
	 * this method tries to find a {@link DataObject} of this data class among the selected data objects.
	 * In the first case ({@code #DataClass}) the variable expression is replaced with the current state of the 
	 * found data object (or "<not found>" if no such data object was found).
	 * In the second case ({@code #DataClass.attributeName}) the attribute instances of the found data object are 
	 * searched for one that matches {@code attributeName}. If such attribute instance is found, its value is used
	 * to replace the variable expression in the input string. Otherwise, it is replaced with "<not found>".
	 * 
	 * If the input string contains multiple variable expression the first one is replaced and the method calls itself
	 * recursively with the resulting string. The recursion ends, when the input no longer contains any variable 
	 * expressions.
	 *  
	 * If the input string does not contain a variable expression, it is returned unchanged.
	 * 
 	 * @param toReplace - the input string which might contain variable expressions {@code #DataClass} or {@code #DataClass.attributeName}
	 * @return the input string with the variable expression replaced by the referenced data object state or data attribute value
	 */
	private String replaceVariableExpressions(String toReplace) {
		Pattern p = Pattern.compile("#(\\w+)(?:\\.(\\w+))?\\b");
		Matcher m = p.matcher(toReplace);
		if (! m.find()) { // no variable used in input, end recursion
			return toReplace;
		}
		final int attributeNameGroup = 2;
		final int dataClassNameGroup = 1;
		String dataClassName = m.group(dataClassNameGroup);
		Optional<String> attrName = Optional.ofNullable(m.group(attributeNameGroup));
		Optional<DataObject> foundDO = getSelectedDataObjects().stream()
											.filter(d -> dataClassName.equals(d.getDataClass().getName()))
											.findFirst();
		if (! foundDO.isPresent()) { // no DO found for data class referenced in variable expression
			log.error(String.
					format("None of the selected data objects of the task '%s' matches the data class '%s' referenced in the variable expression %s.", 
							getControlNode().getName(), dataClassName, m.group()));
			// replace first match and recursive call to replace other potential variable expressions
			String replacedFirstOccurrence = m.replaceFirst("<not found>");
			return replaceVariableExpressions(replacedFirstOccurrence);
		}
		if (! attrName.isPresent()) { // no attribute referenced -> replace "#DataClass" with its state
			// replace first match and recursive call to replace other potential variable expressions
			String replacedFirstOccurrence = m.replaceFirst(foundDO.get().getObjectLifecycleState().getName());
			return replaceVariableExpressions(replacedFirstOccurrence);
		}
		Optional<DataAttributeInstance> foundDAI = 
					foundDO.get().getDataAttributeInstances().stream()
					.filter(dai -> attrName.get().equals(dai.getDataAttribute().getName()))
					.findFirst();
		if (! foundDAI.isPresent()) { // no DAI found for attribute referenced in variable expression
			log.error(String.
					format("The found data object of class '%s' does not have a attribute with name '%s' specified in the variable expression %s.", 
							dataClassName, attrName.get(), m.group()));
			// replace first match and recursive call to replace other potential variable expressions
			String replacedFirstOccurrence = m.replaceFirst("<not found>");
			return replaceVariableExpressions(replacedFirstOccurrence);
		}
		Object value = foundDAI.get().getValue();
		if (value == null) { // attribute value is null
			log.error(String.
					format("The attribute value of the variable expression '%s' is 'null'.", m.group()));
			// replace first match and recursive call to replace other potential variable expressions
			String replacedFirstOccurrence = m.replaceFirst("<value is 'null'>");
			return replaceVariableExpressions(replacedFirstOccurrence);			
		}
		// replace first match and recursive call to replace other potential variable expressions
		String replacedFirstOccurrence = m.replaceFirst(value.toString());
		return replaceVariableExpressions(replacedFirstOccurrence);		
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
