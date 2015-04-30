package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbWebServiceTask;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;

/**
 * Created by jaspar.mang on 29.04.15.
 */
public class WebServiceTaskExecutionBehavior extends TaskExecutionBehavior {
    DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();

    public WebServiceTaskExecutionBehavior(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        super(activityInstance_id, scenarioInstance, controlNodeInstance);
    }


    @Override
    public void execute() {
        String link = dbWebServiceTask.getLinkForControlNode(controlNodeInstance.getControlNode_id());
        Client client = ClientBuilder.newClient();
        WebTarget webResource = client.target(link);
        Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        if (response.getStatus() == 200) {
            this.writeDataAttributes(response.readEntity(String.class));
        }
        this.setCanTerminate(true);
    }

    /**
     * Sets the specific data attribute values to the content from the request.
     * @param content from GET Request.
     */
    private void writeDataAttributes(String content){
        LinkedList<Integer> dataAttributeIds = dbWebServiceTask.getAttributeIdsForControlNode(controlNodeInstance.getControlNode_id());
        for (int dataAttributeId : dataAttributeIds) {
            LinkedList<String> keys = dbWebServiceTask.getKeys(controlNodeInstance.getControlNode_id(), dataAttributeId);
            JSONObject JSONContent = new JSONObject(content);
            int i;
            for (i = 0; i < keys.size() - 1; i++) {
                JSONContent = JSONContent.getJSONObject(keys.get(i));
            }
            for (DataAttributeInstance dataAttributeInstance : scenarioInstance.getDataAttributeInstances().values()) {
                if (dataAttributeInstance.getDataAttribute_id() == dataAttributeId) {
                    dataAttributeInstance.setValue(JSONContent.get(keys.get(i)));
                }
            }
            System.out.println(JSONContent.get(keys.get(i)));
        }
    }
}
