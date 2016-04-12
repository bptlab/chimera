package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import com.google.gson.Gson;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.IPersistable;
import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This class represents an EventType.
 */
public class EventType extends DataClass implements IPersistable {
    private static Logger logger = Logger.getLogger(EventType.class);

    private final static String REGISTRATION_URL =
            PropertyLoader.getProperty("unicorn.url")
                    + PropertyLoader.getProperty("unicorn.path.deploy")
                    + PropertyLoader.getProperty("unicorn.path.eventtype");

    public EventType(final String element) {
        super(element);
        this.isEvent = 1;
        registerEventType();
    }

    /**
     * Register the Event Type in the Unicorn event processing platform.
     */
    private void registerEventType() {

        String xsd;
        String schemaName = this.name;
        EventTypeJsonObject json = new EventTypeJsonObject();
        Gson gson = new Gson();
        String timestampName = "";

        for(DataAttribute attr : this.getAttributes()) {
            if("timestamp".equals(attr.getDataAttributeName())) {
                timestampName = "timestamp";
                break;
            }
        }

        xsd = generateXsd();

        json.setSchemaName(schemaName);
        json.setTimestampName(timestampName);
        json.setXsd(xsd);

        String jsonString = gson.toJson(json);
        Client client = ClientBuilder.newClient();

        Response response = client.target(getRegistrationUrl()).request()
                .post(Entity.json(jsonString));

        if (response.getStatus() != 200) {
            logger.warn("Unexpected response while registering Event Type. Status:"
                    + response.getStatus());
        }

;    }

    private String generateXsd() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        buffer.append("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"");
        buffer.append(this.name);
        buffer.append(".xsd\"\n");
        buffer.append("targetNamespace=\"");
        buffer.append(this.name);
        buffer.append(".xsd\" elementFormDefault=\"qualified\">\n");
        buffer.append("<xs:element name=\"");
        buffer.append(this.name);
        buffer.append("\">\n");
        buffer.append("<xs:complexType>\n");
        buffer.append("<xs:sequence>\n");
        for(DataAttribute attr : this.getAttributes()) {
            buffer.append("<xs:element name=\"");
            buffer.append(attr.getDataAttributeName());
            buffer.append("\" type=\"xs:");
            buffer.append(attr.getDataAttributeType());
            buffer.append("\"\nminOccurs=\"1\" maxOccurs=\"1\" />\n");
        }
        buffer.append("</xs:sequence>\n");
        buffer.append("</xs:complexType>\n");
        buffer.append("</xs:element>\n");
        buffer.append("</xs:schema>");

        return buffer.toString();
    }

    private class EventTypeJsonObject {
        private String xsd;
        private String schemaName;
        private String timestampName;

        public String getTimestampName() {
            return timestampName;
        }

        public void setTimestampName(String timestampName) {
            this.timestampName = timestampName;
        }

        public String getSchemaName() {
            return schemaName;
        }

        public void setSchemaName(String schemaName) {
            this.schemaName = schemaName;
        }

        public String getXsd() {
            return xsd;
        }

        public void setXsd(String xsd) {
            this.xsd = xsd;
        }
    }

    public String getRegistrationUrl() {
        return REGISTRATION_URL;
    }

}
