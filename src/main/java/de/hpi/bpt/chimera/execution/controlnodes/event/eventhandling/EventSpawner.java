package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;

import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.util.PropertyLoader;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

public class EventSpawner {

	private static final String EVENT_URL = PropertyLoader.getProperty("unicorn.url") + PropertyLoader.getProperty("unicorn.path.deploy");
	private static final String EVENT_PATH = PropertyLoader.getProperty("unicorn.path.event");
	private static final Logger log = Logger.getLogger(EventSpawner.class);

	public static boolean spawnEvent(AbstractEventInstance eventInstance) {
		// TODO: check whether eventInstance is an sendEvent
		try {
			DataObject inputObject = getInputObject(eventInstance);
			Response response = buildAndSendEvent(inputObject);
			return response.getStatus() == 200;
		} catch (IllegalArgumentException e) {
			log.error(e);
			return false;
		}
	}

	private static DataObject getInputObject(AbstractEventInstance eventInstance) {
		List<DataObject> inputObjects = eventInstance.getSelectedDataObjects();
		if (inputObjects.size() != 1) {
			throw new IllegalArgumentException("input objects of SendEvent is not distinct or there are no input objects");
		}
		DataObject inputObject = inputObjects.get(0);

		if (!inputObject.getDataClass().isEvent()) {
			throw new IllegalArgumentException("dataclass of input object is not an eventclass");
		}

		return inputObject;
	}

	private static Response buildAndSendEvent(DataObject inputObject) {
		Document eventXml = buildEventFromDataObject(inputObject);
		return sendEvent(eventXml);
	}

	private static Document buildEventFromDataObject(DataObject inputObject) {
		List<DataAttributeInstance> attributes = inputObject.getDataAttributeInstances();
		String eventName = inputObject.getDataClass().getName();

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.newDocument();

			Element rootElement = createRootElement(doc, eventName);
			doc.appendChild(rootElement);
			appendAttributes(doc, rootElement, attributes);

			return doc;
		} catch (ParserConfigurationException e) {
			log.error("Event xml from send event/task could not be build.", e);
			return null;
		}
	}

	private static Response sendEvent(Document eventXml) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(EVENT_URL).path(EVENT_PATH);
		Response response = target.request(MediaType.APPLICATION_XML).post(Entity.xml(eventXml));
		if (response.getStatus() != 200) {
			log.warn("Event was not sent correctly. Response status: " + response.getStatus());
		}
		return response;
	}

	// <FoilEvent xmlns="" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="FoilEvent.xsd">
	private static Element createRootElement(Document doc, String eventName) {
		Element ele = doc.createElement(eventName);
		ele.setAttribute("xmlns", "");
		ele.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		ele.setAttribute("xsi:noNamespaceSchemaLocation", eventName + ".xsd");
		return ele;
	}

	private static void appendAttributes(Document doc, Element rootElement, List<DataAttributeInstance> attributes) {
		attributes.stream().forEach(attr -> {
			Element el = doc.createElement(attr.getDataAttribute().getName());
			el.appendChild(doc.createTextNode(attr.getValue().toString()));
			rootElement.appendChild(el);
		});
	}
}
