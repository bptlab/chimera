package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;

import com.google.gson.Gson;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.util.PropertyLoader;

public class DBRegistrant {

    private final static Logger logger = Logger.getLogger(DBRegistrant.class);

    private static String dbTypeToJson(DataClass dbType) {
        Gson gson = new Gson();
        String schemaName = dbType.getName();
        DBTypeJsonObject json = new DBTypeJsonObject();
        json.setSchemaName(schemaName);

        List<String> dataAttributes = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ \"id\" : \"String\", ");
        buffer.append("{ \"DO_State\" : \"String\", ");

        for (DataAttribute attr : dbType.getDataAttributes()) {
            StringBuffer buffer2 = new StringBuffer();

            buffer2.append("\"");
            buffer2.append(attr.getName());
            buffer2.append("\"");
            buffer2.append(" : ");
            buffer2.append("\"");
            buffer2.append(attr.getType());
            buffer2.append("\"");

            dataAttributes.add(buffer2.toString());
        }
        buffer.append(String.join(", ", dataAttributes));
        buffer.append(" }");

        json.setAttributes(buffer.toString());

        return (gson.toJson(json));
    }

    public static Client getClient() {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);

        return client;
    }

    private static void insertDataObject(DataObject dataObject) {
        Gson gson = new Gson();
        Client client = getClient();

        DBTypeJsonObject json = new DBTypeJsonObject();
        json.setSchemaName(dataObject.getDataClass().getName());
        StringBuffer buffer = new StringBuffer();

        buffer.append("{ \"id\" : \"");
        buffer.append(dataObject.getId());
        buffer.append("\", ");
        List<String> dataAttributes = new ArrayList<>();

        for(DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstances()) {
            StringBuffer buffer2 = new StringBuffer();

            buffer2.append(" \"");
            buffer2.append(dataAttributeInstance.getDataAttribute().getName());
            buffer2.append("\"");
            buffer2.append(" : ");

            if(dataAttributeInstance.getDataAttribute().getType().equals("Integer") || dataAttributeInstance.getDataAttribute().getType().equals("Double")){
                buffer2.append(dataAttributeInstance.getValue());
            } else {
                buffer2.append(" \"");
                buffer2.append(dataAttributeInstance.getValue());
                buffer2.append("\"");
            }

            dataAttributes.add(buffer2.toString());

        }

        buffer.append(String.join(", ", dataAttributes));
        buffer.append(", DO_State : \"");
        buffer.append(dataObject.getObjectLifecycleState());
        buffer.append("\"");
        buffer.append("}");

        json.setAttributes(buffer.toString());
        String jsonString = gson.toJson(json);
        System.out.println(jsonString);

        try {
            Response response = client.target("http://host.docker.internal:3001/autoInsert").request().post(Entity.json(jsonString));
            if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
                logger.warn("Unexpected response while inserting DB Type. Status:" + response.getStatus());
            } else {
                logger.info("Successfully inserted the DBTypes at MongoDB");
            }
        } catch (ProcessingException e) {
            logger.error(e);
            logger.warn("Could not insert db type");
        }

    }

    public static void processDataObjectsForInsertion(List<DataObject> dataObjectList) {

        for(DataObject dataObject : dataObjectList) {
            if(dataObject.getDataClass().isDBClass()){
                insertDataObject(dataObject);
            }
        }

    }

    public static void registerDBType(DataClass dbType) {

        String jsonString = dbTypeToJson(dbType);
        Client client = getClient();

        System.out.println(jsonString);

        try {
            Response response = client.target("http://host.docker.internal:3001/autoCreate").request().post(Entity.json(jsonString));
            if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
                logger.warn("Unexpected response while registering DB Type. Status:" + response.getStatus());
            } else {
                logger.info("Successfully registered the DBTypes at MongoDB");
            }
        } catch (ProcessingException e) {
            logger.warn("Could not register db type");
        }
    }
}
