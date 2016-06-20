package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataClass;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;
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

    private ScenarioInstance instance;

    private static final String EVENT_URL = PropertyLoader.getProperty("unicorn.url");
    private static final String EVENT_PATH = PropertyLoader.getProperty("unicorn.path.event");

    private static final Logger log = Logger.getLogger(EventSpawner.class);

    public EventSpawner(ScenarioInstance instance) {
        this.instance = instance;
    }

    public boolean spawnEvent(int controlNodeId) {
        DataObject inputObject = getInputObject(controlNodeId);
        assertIsEvent(inputObject);
        Response response = buildAndSendEvent(inputObject);
        return response.getStatus() == 200;
    }

    private DataObject getInputObject(int controlNodeId) {
        List<DataObject> possibleInputs = instance.getDataManager()
                .getAvailableInput(controlNodeId);
        assert possibleInputs.size() == 1 : "There is only one input object allowed for send events/tasks.";
        DataObject inputObject = possibleInputs.get(0);
        return inputObject;
    }

    private void assertIsEvent(DataObject inputObject) {
        boolean isEvent = new DbDataClass().isEvent(
                inputObject.getDataClassId());
        assert isEvent : "The input for the send event/task is not an event object.";
    }

    private Response buildAndSendEvent(DataObject inputObject) {
        Document eventXml = buildEventFromDataObject(inputObject);
        return sendEvent(eventXml);
    }

    private Document buildEventFromDataObject(DataObject inputObject) {
        List<DataAttributeInstance> attributes =
                inputObject.getDataAttributeInstances();
        String eventName = inputObject.getName();

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.newDocument();

            Element rootElement = doc.createElement(eventName);
            doc.appendChild(rootElement);
            appendAttributes(doc, rootElement, attributes);

            return doc;
        } catch (ParserConfigurationException e) {
            log.error("Event xml from send event/task could not be build.", e);
            return null;
        }
    }

    private Response sendEvent(Document eventXml) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(EVENT_URL).path(EVENT_PATH);
        Response response = target.request(MediaType.APPLICATION_XML)
                .post(Entity.xml(eventXml));
        if (response.getStatus() != 200) {
            log.warn("Event was not sent correctly. Response status: " + response.getStatus());
        }
        return response;
    }

    private void appendAttributes(Document doc, Element rootElement, List<DataAttributeInstance> attributes) {
        attributes.stream().forEach(
                attr -> {
                    Element el = doc.createElement(attr.getName());
                    el.appendChild(doc.createTextNode(attr.getValue().toString()));
                    rootElement.appendChild(el);
        });
    }
}
