package de.hpi.bpt.chimera.execution.controlnodes.activity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.ResourceTask;
import org.apache.log4j.Logger;

public class ResourceTaskInstance extends AbstractActivityInstance {
    private static final Logger log = Logger.getLogger(ResourceTaskInstance.class);

    /**
     * Executes the resourceTask by making 2 API calls.
     * 1st call starts the optimization and receives and object which informs about the actual state of the Optimization.
     * 2nd call uses the ID from the first call and requests the result.
     */
    @Override
    public void execute(){

        String host = getControlNode().getHost();
        String firstID = getControlNode().getID();
        String contentType = getControlNode().getContentType();
        if(host.isEmpty()){
            return;
        }

        try{

            String firstQuery = buildFirstQuery(host, firstID);
            HttpResponse<JsonNode> firstResponse = getAPICall(firstQuery, contentType);

            String secondID = processFirstReply(firstResponse.getBody().toString());
            String secondQuery = buildSecondQuery(host, secondID);
            HttpResponse<JsonNode> secondResponse = getAPICall(secondQuery, contentType);

            while(!isSuccessful(secondResponse.getBody().toString())){
                Thread.sleep(500);
                secondResponse = getAPICall(secondQuery, contentType);
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    public ResourceTaskInstance(ResourceTask activity, FragmentInstance fragmentInstance) {
        super(activity, fragmentInstance);
        forbidAutomaticStart();
    }

    @Override
    public ResourceTask getControlNode() {
        return (ResourceTask) super.getControlNode();
    }

    /**
     * Processes the first response by getting the ID required for the second query
     * @param json json in String form
     * @return the ID required for the second query
     */
    private static String processFirstReply(String json){
        String id;
        JsonObject o = getDataArrtibute(json);
        id = o.get("id").getAsString(); // ID REQUIRED FOR SECOND QUERY

        return id;
    }

    /**
     * Checks if the execution is successful by looking for the existence for successful attribute
     * @param json json in String form
     * @return true if successful, false otherwise
     */
    private static boolean isSuccessful(String json){
        JsonObject o1 = getDataArrtibute(json);
        String str2 = o1.get("attributes").toString();
        JsonObject o2 = new Gson().fromJson(str2, JsonObject.class);

        return o2.has("successful");
    }

    /**
     * This is implemented as a Method to prevent redundancy
     * @param json json in String form
     * @return a JsonObject
     */
    private static JsonObject getDataArrtibute(String json){
        JsonObject obj1 = new Gson().fromJson(json, JsonObject.class);
        String str1 = obj1.get("data").toString();
        JsonObject o1 = new Gson().fromJson(str1, JsonObject.class);
        return o1;
    }


    /**
     * Performs the API call
     * @param query the Url to be queried from
     * @param contentType Content Type
     * @return built query combined with the content-type
     * @throws Exception when failed
     */
    private static HttpResponse<JsonNode> getAPICall(String query, String contentType) throws Exception{
        return Unirest.get(query).header("Content-Type", contentType).asJson();
    }

    /**
     * Build the first query String
     * @param host the web Url
     * @param id ID of the optimization algorithm
     * @return a concatenated Url
     */
    private static String buildFirstQuery(String host, String id){
        StringBuilder query = new StringBuilder();
        return query.append(host).append("/optimization/recipes/").append(id).append("/execute").toString();
    }

    /**
     * Builds the second query String
     * @param host the web Url
     * @param id ID of the optimization algorithm
     * @return a concatenated Url
     */
    private static String buildSecondQuery(String host, String id){
        StringBuilder query = new StringBuilder();
        return query.append(host).append("/optimization/executions/").append(id).toString();
    }

    @Override
    public void terminate() {
        super.terminate();
    }
}
