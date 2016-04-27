package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest.HistoryRestService;
import de.uni_potsdam.hpi.bpt.bp2014.util.XmlUtil;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class XesExportTest extends JerseyTest {
    /**
     * The base url of the jcore rest interface.
     * Allows us to send requests to the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface}.
     */
    private WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(HistoryRestService.class);
    }

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Before
    public void setUpBase() {
        base = target("history/v2/");
    }


    @Test
    public void testXmlMarshalling() throws IOException, TransformerException {
        // The simple scenario contains only one activity do something
        String path = "src/test/resources/history/simpleScenario.json";
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(path);
        ScenarioTestHelper.beginActivityByName("Do something", instance);
        ScenarioTestHelper.terminateActivityInstanceByName("Do something", instance);

        Response response = base.path(String.format("export/%d", instance.getScenarioId())).request().get();
        assertEquals(200, response.getStatus());
        Document doc = XmlUtil.retrieveFromString(response.readEntity(String.class));
        Node rootElement = doc.getChildNodes().item(0);
        NodeList traces = rootElement.getChildNodes();
        assertEquals(1, traces.getLength());
        Node trace = traces.item(0);
        NodeList logEntries = trace.getChildNodes();
        // There should be 4 log entries init, running, terminated and init again because the
        // fragment is restarted.
        assertEquals(4, logEntries.getLength());

        System.out.println(XmlUtil.convertToString(doc));
    }


}
