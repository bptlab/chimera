package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;

import java.util.UUID;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;

import com.google.gson.Gson;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.util.PropertyLoader;

public class EventRegistrant {
	private final static Logger logger = Logger.getLogger(EventRegistrant.class);

	private final static String REGISTRATION_URL = PropertyLoader.getProperty("unicorn.url") + PropertyLoader.getProperty("unicorn.path.deploy") + PropertyLoader.getProperty("unicorn.path.eventtype");

	public static final String STATE_ATTRIBUTE_NAME = "DO_state";

	/**
	 * Register the EventType in the Unicorn event processing platform. Used to
	 * register Events after parsing a CaseModel
	 * 
	 * @param eventType
	 *            - A DataClass with {@code isEvent=true}
	 */
	public static void registerEventType(DataClass eventType) {

		String xsd;
		String schemaName = eventType.getName();
		EventTypeJsonObject json = new EventTypeJsonObject();
		Gson gson = new Gson();

		eventType.setTimestampName("GeneratedTimestamp");
		for (DataAttribute attr : eventType.getDataAttributes()) {
			if ("GeneratedTimestamp".equals(attr.getName())) {
				eventType.setTimestampName("GeneratedTimestamp" + UUID.randomUUID().toString());
				break;
			}
		}

		xsd = generateXsd(eventType);

		json.setSchemaName(schemaName);
		json.setTimestampName(eventType.getTimestampName());
		json.setXsd(xsd);

		String jsonString = gson.toJson(json);
		Client client = ClientBuilder.newClient();
		client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
		client.property(ClientProperties.READ_TIMEOUT, 1000);
		try {
			Response response = client.target(REGISTRATION_URL).request().post(Entity.json(jsonString));
			if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
				logger.warn("Unexpected response while registering Event Type. Status:" + response.getStatus());
			} else {
				logger.info("Successfully registered the EventTypes at Unicorn");
			}
		} catch (ProcessingException e) {
			logger.warn("Could not register event type");
		}
	}

	private static String generateXsd(DataClass eventType) {
		// TODO rework with Documentbuilder, see send events
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		buffer.append("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"");
		buffer.append(eventType.getName());
		buffer.append(".xsd\"\n");
		buffer.append("targetNamespace=\"");
		buffer.append(eventType.getName());
		buffer.append(".xsd\" elementFormDefault=\"qualified\">\n");
		buffer.append("<xs:element name=\"");
		buffer.append(eventType.getName());
		buffer.append("\">\n");
		buffer.append("<xs:complexType>\n");
		buffer.append("<xs:sequence>\n");
		for (DataAttribute attr : eventType.getDataAttributes()) {
			buffer.append("<xs:element name=\"");
			buffer.append(attr.getName());
			buffer.append("\" type=\"xs:");
			buffer.append(attr.getType());
			buffer.append("\"\nminOccurs=\"1\" maxOccurs=\"1\" />\n");
		}
		// now the DO state attribute
		buffer.append("<xs:element name=\"");
		buffer.append(STATE_ATTRIBUTE_NAME);
		buffer.append("\" type=\"xs:String\" \n minOccurs=\"1\" maxOccurs=\"1\" />\n");
		buffer.append("</xs:sequence>\n");
		buffer.append("</xs:complexType>\n");
		buffer.append("</xs:element>\n");
		buffer.append("</xs:schema>");

		return buffer.toString();
	}
}
