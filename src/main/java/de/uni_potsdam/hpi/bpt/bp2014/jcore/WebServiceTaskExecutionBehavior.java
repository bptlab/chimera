package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbWebServiceTask;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;


/**
 * This is the execution behavior for webservice tasks
 */
public class WebServiceTaskExecutionBehavior extends TaskExecutionBehavior {
    static Logger log = Logger.getLogger(WebServiceTaskExecutionBehavior.class.getName());

    /**
     * DB Connection class.
     */
    DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();

    /**
     * Initializes the webservice task.
     *
     * @param activityInstance_id The id of the webservice task.
     * @param scenarioInstance    The instance of the ScenarioInstance.
     * @param controlNodeInstance The instance of the ControlNodeInstance (ActivityInstance).
     */
    public WebServiceTaskExecutionBehavior(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        super(activityInstance_id, scenarioInstance, controlNodeInstance);
    }

    @Override
    public void execute() {
        String link = dbWebServiceTask.getLinkForControlNode(controlNodeInstance.getControlNode_id());
        for (DataAttributeInstance dataAttributeInstance : scenarioInstance.getDataAttributeInstances().values()) {
            link = link.replace(
                    "#" + (dataAttributeInstance.getDataObjectInstance()).getName()
                            + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
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
        Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
        Response response;
        try {
            switch (dbWebServiceTask.getMethod(controlNodeInstance.getControlNode_id())) {
                case "POST":
                    String post = dbWebServiceTask.getPOST(controlNodeInstance.getControlNode_id());
                    for (DataAttributeInstance dataAttributeInstance : scenarioInstance.getDataAttributeInstances().values()) {
                        post = post.replace(
                                "#" + (dataAttributeInstance.getDataObjectInstance()).getName()
                                        + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
                    }
                    response = invocationBuilder.post(Entity.json(post));
                    break;
                case "PUT":
                    post = dbWebServiceTask.getPOST(controlNodeInstance.getControlNode_id());
                    for (DataAttributeInstance dataAttributeInstance : scenarioInstance.getDataAttributeInstances().values()) {
                        post = post.replace(
                                "#" + (dataAttributeInstance.getDataObjectInstance()).getName()
                                        + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
                    }
                    response = invocationBuilder.put(Entity.json(post));
                    break;
                default:
                    response = invocationBuilder.get();
            }
            if (response.getStatus() >= 200 && response.getStatus() <= 226) {
                this.writeDataAttributes(response.readEntity(String.class));
            }
        } catch (Exception e) {
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
        LinkedList<Integer> dataAttributeIds = dbWebServiceTask.getAttributeIdsForControlNode(controlNodeInstance.getControlNode_id());
        for (int dataAttributeId : dataAttributeIds) {
            LinkedList<String> keys = dbWebServiceTask.getKeys(controlNodeInstance.getControlNode_id(), dataAttributeId);
            JSONObject jsonContent = new JSONObject(content);
            JSONArray jsonArray = null;
            boolean isJSONArray = false;
            int i;
            for (i = 0; i < keys.size() - 1; i++) {
                try {
                    jsonContent = jsonContent.getJSONObject(keys.get(i));
                    jsonArray = null;
                    isJSONArray = false;
                } catch (Exception e1) {
                    try {
                        jsonContent = jsonArray.getJSONObject(new Integer(keys.get(i)));
                        jsonArray = null;
                        isJSONArray = false;
                    } catch (Exception e2) {
                        try {
                            jsonArray = jsonContent.getJSONArray(keys.get(i));
                            jsonContent = null;
                            isJSONArray = true;
                        } catch (Exception e3) {
                            jsonArray = jsonArray.getJSONArray(new Integer(keys.get(i)));
                            jsonContent = null;
                            isJSONArray = true;
                        }
                    }
                }
            }
            for (DataAttributeInstance dataAttributeInstance : scenarioInstance.getDataAttributeInstances().values()) {
                if (dataAttributeInstance.getDataAttribute_id() == dataAttributeId) {
                    if (isJSONArray) {
                        dataAttributeInstance.setValue(jsonArray.get(new Integer(keys.get(i))));
                    } else {
                        dataAttributeInstance.setValue(jsonContent.get(keys.get(i)));
                    }
                }
            }
        }
    }
}
