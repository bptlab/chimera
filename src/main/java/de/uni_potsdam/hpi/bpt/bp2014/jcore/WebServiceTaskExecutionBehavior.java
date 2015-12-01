package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbWebServiceTask;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the execution behavior for webservice tasks.
 */
public class WebServiceTaskExecutionBehavior extends TaskExecutionBehavior {
	private static Logger log = Logger.getLogger(
			WebServiceTaskExecutionBehavior.class.getName());

	/**
	 * DB Connection class.
	 */
	private DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();

	/**
	 * Initializes the webservice task.
	 *
	 * @param activityInstanceId The id of the webservice task.
	 * @param scenarioInstance    The instance of the ScenarioInstance.
	 * @param controlNodeInstance The AbstractControlNodeInstance (ActivityInstance).
	 */
	public WebServiceTaskExecutionBehavior(int activityInstanceId,
			ScenarioInstance scenarioInstance,
			AbstractControlNodeInstance controlNodeInstance) {
		super(activityInstanceId, scenarioInstance, controlNodeInstance);
	}

	@Override public void execute() {
		String link = dbWebServiceTask
				.getLinkForControlNode(getControlNodeInstance().getControlNodeId());
		for (DataAttributeInstance dataAttributeInstance : getScenarioInstance()
				.getDataAttributeInstances().values()) {
			link = link.replace("#" + (dataAttributeInstance.getDataObjectInstance())
							.getName() + "."
							+ dataAttributeInstance.getName(),
					dataAttributeInstance.getValue().toString());
		}
		Client client = ClientBuilder.newClient();
		String[] url = link.split("\\?");
		WebTarget webResource = client.target(url[0]);
		if (url.length > 1) {
			String[] params = url[1].split("&");
			for (String param : params) {
				String[] values = param.split("=");
				webResource = webResource.queryParam(values[0], values[1]);
			}
		}
		Invocation.Builder invocationBuilder = webResource.request(
				MediaType.APPLICATION_JSON);
		Response response;
		try {
			switch (dbWebServiceTask.getMethod(getControlNodeInstance().getControlNodeId())) {
			case "POST":
				String post = dbWebServiceTask.getPOST(getControlNodeInstance().getControlNodeId());
				for (DataAttributeInstance dataAttributeInstance : getScenarioInstance()
						.getDataAttributeInstances().values()) {
					post = post.replace(
							"#" + (dataAttributeInstance.getDataObjectInstance()).getName() + "."
									+ dataAttributeInstance.getName(),
							dataAttributeInstance.getValue().toString());
				}
				response = invocationBuilder.post(Entity.json(post));
				break;
			case "PUT":
				post = dbWebServiceTask.getPOST(getControlNodeInstance().getControlNodeId());
				for (DataAttributeInstance dataAttributeInstance : getScenarioInstance()
						.getDataAttributeInstances().values()) {
					post = post.replace(
							"#" + (dataAttributeInstance.getDataObjectInstance()).getName() + "."
									+ dataAttributeInstance.getName(),
							dataAttributeInstance.getValue().toString());
				}
				response = invocationBuilder.put(Entity.json(post));
				break;
			default:
				response = invocationBuilder.get();
			}
			if (response.getStatus() >= 200 && response.getStatus() <= 226) {
				String json = response.readEntity(String.class);
				this.writeDataAttributes(json);
			}
		} catch(JSONException e) {
			log.error("Error by getting URL", e);
		}
		this.setCanTerminate(true);
	}

	/**
	 * Sets the specific data attribute values to the content from the request.
	 *
	 * @param content from GET Request.
	 */
	private void writeDataAttributes(String content) {
		LinkedList<Integer> dataAttributeIds = dbWebServiceTask
				.getAttributeIdsForControlNode(
						getControlNodeInstance().getControlNodeId());
		for (int dataAttributeId : dataAttributeIds) {
			LinkedList<String> keys = dbWebServiceTask
					.getKeys(getControlNodeInstance()
									.getControlNodeId(),
							dataAttributeId);
			JSONObject jsonContent = new JSONObject(content);
			JSONArray jsonArray = null;
			boolean isJSONArray = false;
			for (int i = 0; i < keys.size() - 1; i++) {
				try {
//					if (jsonContent != null) {
						jsonContent = jsonContent.getJSONObject(
								keys.get(i));
//					}
					jsonArray = null;
					isJSONArray = false;
				} catch (Exception e1) {
					try {
//						if (jsonArray != null) {
							jsonContent = jsonArray.getJSONObject(
									new Integer(keys.get(i)));
//						}
						jsonArray = null;
						isJSONArray = false;
					} catch (Exception e2) {
						try {
							jsonArray = jsonContent.getJSONArray(
									keys.get(i));
							jsonContent = null;
							isJSONArray = true;
						} catch (Exception e3) {
							jsonArray = jsonArray.getJSONArray(
									new Integer(keys.get(i)));
							jsonContent = null;
							isJSONArray = true;
						}
					}
				}
			}
			for (DataAttributeInstance dataAttributeInstance : getScenarioInstance()
					.getDataAttributeInstances().values()) {
				if (dataAttributeInstance.getDataAttributeId() == dataAttributeId) {
					if (isJSONArray) {
						dataAttributeInstance.setValue(jsonArray.get(
								new Integer(keys.getLast())));
					} else {
						if (jsonContent != null) {
							dataAttributeInstance
									.setValue(jsonContent.get(
									keys.getLast()));
						}
					}
				}
			}
		}
	}
}
