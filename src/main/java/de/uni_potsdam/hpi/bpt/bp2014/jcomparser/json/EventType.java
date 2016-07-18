package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import com.google.gson.Gson;
import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * This class represents an EventType.
 */
public class EventType extends DataClass {
    private static Logger logger = Logger.getLogger(EventType.class);

    private final static String REGISTRATION_URL =
            PropertyLoader.getProperty("unicorn.url")
                    + PropertyLoader.getProperty("unicorn.path.deploy")
                    + PropertyLoader.getProperty("unicorn.path.eventtype");

    public EventType(final String element) {
        super(element);
        this.isEvent = 1;
    }

    @Override
    public int save(int scenarioId) {
        registerEventType();
        return super.save(scenarioId);
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
            if("timestamp".equalsIgnoreCase(attr.getName())) {
                timestampName = attr.getName();
                break;
            }
        }

        xsd = generateXsd();

        json.setSchemaName(schemaName);
        json.setTimestampName(timestampName);
        json.setXsd(xsd);

        String jsonString = gson.toJson(json);
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);
        try {
            Response response = client.target(REGISTRATION_URL).request()
                    .post(Entity.json(jsonString));
            if (response.getStatus() != 204) {
                logger.warn("Unexpected response while registering Event Type. Status:"
                        + response.getStatus());
            }
        } catch (ProcessingException e) {
            logger.warn("Could not register event type");
        }
     }

    private String generateXsd() {
        // TODO rework with Documentbuilder, see send events
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
            buffer.append(attr.getName());
            buffer.append("\" type=\"xs:");
            buffer.append(attr.getType());
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
