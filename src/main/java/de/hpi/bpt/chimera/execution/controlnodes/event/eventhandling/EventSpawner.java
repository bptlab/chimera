package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;

import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.util.PropertyLoader;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EventSpawner {

	private static final String EVENT_URL = PropertyLoader.getProperty("unicorn.url") + PropertyLoader.getProperty("unicorn.path.deploy");
	private static final String EVENT_PATH = PropertyLoader.getProperty("unicorn.path.event");


	private static final Logger log = Logger.getLogger(EventSpawner.class);

	private EventSpawner() {
	}

	public static boolean spawnEvent(AbstractEventInstance eventInstance) {
		// TODO: check whether eventInstance is an sendEvent
		try {
			List<DataObject> inputObjects = getInputObjects(eventInstance);
			for (DataObject inputObject : inputObjects) {
				Response response = buildAndSendEvent(inputObject);
				if (response.getStatus() != 200) {
					log.error("Event Spawner was not able to send all Events");
					return false;
				}
			}
			log.info("Succesfully spawned an event!");
			return true;
		} catch (Exception e) {
			log.error("Error while spawning an event for Unicorn", e);
			return false;
		}
	}

	private static List<DataObject> getInputObjects(AbstractEventInstance eventInstance) {
		if (!eventInstance.getControlNode().hasUniquePreCondition()) {
			throw new IllegalArgumentException("PreConditition of SendEvent is not unique.");
		}

		List<DataObject> possibleInputObjects = new ArrayList<>();
		for (AtomicDataStateCondition condition : eventInstance.getControlNode().getPreCondition().getAtomicDataStateConditions()) {
			if (!condition.getDataClass().isEvent()) {
				throw new IllegalArgumentException("dataclass of input object is not an eventclass");
			}

			List<DataObject> availableDataObjects = new ArrayList<>(eventInstance.getDataManager().getAvailableDataObjects(condition));

			if (availableDataObjects.size() == 1) {
				DataObject dataObject = availableDataObjects.get(0);
				possibleInputObjects.add(dataObject);
			} else {
				log.info(String.format("There is none or more than one possible DataObject with DataClass: %s and State: %s for the input of the event: %s", condition.getDataClassName(), condition.getStateName(), eventInstance.getControlNode().getId()));
			}
		}

		return possibleInputObjects;
	}

	private static Response buildAndSendEvent(DataObject inputObject) {
		Document eventXml = buildEventFromDataObject(inputObject);

		// DOMImplementationLS domImplementation = (DOMImplementationLS)
		// eventXml.getImplementation();
		// LSSerializer lsSerializer = domImplementation.createLSSerializer();
		DOMSource domSource = new DOMSource(eventXml);
		// FileOutputStream out = new FileOutputStream("test.xml");
		StringWriter out = new StringWriter();
		String xmlAsString = "";

		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(domSource, new StreamResult(out));
			xmlAsString = out.getBuffer().toString();
		} catch (Exception e) {
			log.error("Error while generating the string out of the XML-Document", e);
		}
		log.info("The EventXML which will be send to Unicorn is:" + xmlAsString);

		return sendEvent(xmlAsString);
	}

	private static Document buildEventFromDataObject(DataObject inputObject) {
		List<DataAttributeInstance> attributes = inputObject.getDataAttributeInstances();
		String state = inputObject.getObjectLifecycleState().getName();
		String eventName = inputObject.getDataClass().getName();

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.newDocument();


			Element rootElement = createRootElement(doc, eventName);
			doc.appendChild(rootElement);
			appendAttributes(doc, rootElement, attributes);
			
			// append DO state as attribute
			Element stateElement = doc.createElement(EventRegistrant.STATE_ATTRIBUTE_NAME);
			stateElement.appendChild(doc.createTextNode(state));
			rootElement.appendChild(stateElement);

			// appendTimestamp
			String timestamp = new Timestamp(System.currentTimeMillis()).toString();
			Element el = doc.createElement(inputObject.getDataClass().getTimestampName());
			el.appendChild(doc.createTextNode(String.valueOf(timestamp)));
			rootElement.appendChild(el);

			return doc;
		} catch (ParserConfigurationException e) {
			log.error("Event xml from send event/task could not be build.", e);
			return null;
		}
	}

	private static Response sendEvent(String eventXml) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(EVENT_URL).path(EVENT_PATH);
		Response response = target.request().post(Entity.xml(eventXml));
		if (response.getStatus() != 200) {
			log.warn("Event was not sent correctly. Response status: " + response.getStatus() + response.readEntity(String.class));
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
			el.appendChild(doc.createTextNode(String.valueOf(attr.getValue())));
			rootElement.appendChild(el);
		});

	}
}
