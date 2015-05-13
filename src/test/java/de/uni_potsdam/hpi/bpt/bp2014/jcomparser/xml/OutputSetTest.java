package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.ibatis.common.jdbc.ScriptRunner;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.ws.rs.client.WebTarget;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OutputSetTest extends AbstractTest{

    private Document document = new DocumentImpl();
    private List<Element> dataFlows;
    private List<Edge> dataFlowEdges;
    private Node activity;
    private List<OutputSet> outputSets;
    private List<Node> dataNodes;

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2_schema.sql";
    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2_AcceptanceTests.sql";
    }
    /**
     * The base url of the jcore rest interface.
     * Allows us to send requests to the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface}.
     */
    private WebTarget base;

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }
    @Before
    public void setUp() {
        setUpDataFlows();
        setUpEdges();
        setUpNodes();
    }

    private void setUpDataFlows() {
        dataFlows = new ArrayList<>();
        Element dataFlow = document.createElement("edge");
        dataFlow.appendChild(createProperty("label", ""));
        dataFlow.appendChild(createProperty("#id", "4"));
        dataFlow.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Association"));
        dataFlow.appendChild(createProperty("#sourceNode", "1"));
        dataFlow.appendChild(createProperty("#targetNode", "2"));
        dataFlows.add(dataFlow);
        dataFlow = document.createElement("edge");
        dataFlow.appendChild(createProperty("label", ""));
        dataFlow.appendChild(createProperty("#id", "5"));
        dataFlow.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Association"));
        dataFlow.appendChild(createProperty("#sourceNode", "1"));
        dataFlow.appendChild(createProperty("#targetNode", "3"));
        dataFlows.add(dataFlow);
    }

    public void setUpEdges() {
        dataFlowEdges = new ArrayList<Edge>();
        for (Element dataFlow : dataFlows) {
            Edge edge = new Edge();
            edge.initializeInstanceFromXML(dataFlow);
            dataFlowEdges.add(edge);
        }
    }

    public void setUpNodes() {
        HashMap<Long, Node> nodes = new HashMap<Long, Node>();
        dataNodes = new ArrayList<>();

        // activity/ConsumerNode
        activity = new Node();
        activity.setId(1L);
        nodes.put(1L, activity);

        // dataNodes
        Node datanode = new Node();
        datanode.setId(2L);
        nodes.put(2L, datanode);
        dataNodes.add(datanode);

        datanode = new Node();
        datanode.setId(3L);
        nodes.put(3L, datanode);
        dataNodes.add(datanode);

        for (Edge edge : dataFlowEdges) {
            edge.setControlNodes(nodes);
        }
    }

    public void setUpOutputSet() {
        outputSets = OutputSet.createOutputSetForTaskAndEdges(activity, dataFlowEdges);
    }

    @Test
    public void testOneOutputSet() {
        dataNodes.get(0).setText("DO");
        dataNodes.get(1).setText("DO1");
        setUpOutputSet();
        Assert.assertEquals("There is actually just one outputSet", 1, outputSets.size());
        Assert.assertEquals("The producer-Node has not been set correctly", activity, outputSets.get(0).getNode());

        Assert.assertEquals("The output-Nodes have not been set correctly", 2, outputSets.get(0).getDataObjects().size());
        Assert.assertTrue("The output-Nodes have not been set correctly", outputSets.get(0).getDataObjects().contains(dataNodes.get(0)));
        Assert.assertTrue("The output-Nodes have not been set correctly", outputSets.get(0).getDataObjects().contains(dataNodes.get(1)));

        Assert.assertEquals("The associations have not been set correctly", 2, outputSets.get(0).getAssociations().size());
        Assert.assertTrue("The associations have not been set correctly", outputSets.get(0).getAssociations().contains(dataFlowEdges.get(0)));
        Assert.assertTrue("The associations have not been set correctly", outputSets.get(0).getAssociations().contains(dataFlowEdges.get(1)));

        Assert.assertTrue("Something went wrong saving the outputset", outputSets.get(0).save() > 0);
    }

    @Test
    public void testTwoOutputSets() {
        dataNodes.get(0).setText("DO");
        dataNodes.get(1).setText("DO");
        setUpOutputSet();
        Assert.assertEquals("There should be two inputSets", 2, outputSets.size());

        Assert.assertEquals("The consumer-Node has not been set correctly", activity, outputSets.get(0).getNode());
        Assert.assertEquals("The consumer-Node has not been set correctly", activity, outputSets.get(1).getNode());

        Assert.assertEquals("The output-Nodes have not been set correctly", 1, outputSets.get(0).getDataObjects().size());
        Assert.assertEquals("The output-Nodes have not been set correctly", 1, outputSets.get(1).getDataObjects().size());
        if (dataNodes.get(0) == outputSets.get(0).getDataObjects().get(0))
            Assert.assertEquals("The output-Nodes have not been set correctly", dataNodes.get(1), outputSets.get(1).getDataObjects().get(0));
        else if (dataNodes.get(0) == outputSets.get(1).getDataObjects().get(0))
            Assert.assertEquals("The output-Nodes have not been set correctly", dataNodes.get(1), outputSets.get(0).getDataObjects().get(0));
        else
            Assert.fail("The output-Nodes have not been set correctly");

        Assert.assertEquals("The associations have not been set correctly", 1, outputSets.get(0).getAssociations().size());
        Assert.assertEquals("The associations have not been set correctly", 1, outputSets.get(1).getAssociations().size());
        if (dataFlowEdges.get(0) == outputSets.get(0).getAssociations().get(0))
            Assert.assertEquals("The associations have not been set correctly", dataFlowEdges.get(1), outputSets.get(1).getAssociations().get(0));
        else if (dataFlowEdges.get(0) == outputSets.get(1).getAssociations().get(0))
            Assert.assertEquals("The associations have not been set correctly", dataFlowEdges.get(1), outputSets.get(0).getAssociations().get(0));
        else
            Assert.fail("The associations have not been set correctly");
        Assert.assertTrue("Something went wrong saving the outputset", outputSets.get(0).save() > 0);
        Assert.assertTrue("Something went wrong saving the outputset", outputSets.get(1).save() > 0);
    }

    private Element createProperty(String name, String value) {
        if (null == document) {
            return null;
        }
        Element property = document.createElement("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        return property;
    }
}
