package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a Scenario-Model.
 * It can be parsed from an XML and written to a Database.
 */
public class Scenario implements IDeserialisable, IPersistable {

    /**
     * The Name of the Scenario.
     */
    private String scenarioName;

    /**
     * The model Id of the scenario.
     */
    private long scenarioID;
    /**
     * The XML which holds all information from the model.
     */
    private org.w3c.dom.Node scenarioXML;
    /**
     * A List of fragments which are part of the scenario.
     */
    private List<Fragment> fragments;
    /**
     * The url of the process Editor.
     */
    private String processeditorServerUrl;
    /**
     * The database ID of the scenario.
     */
    private int databaseID;
    /**
     * A Map of the names of dataObjects and the objects themselves.
     */
    private Map<String, DataObject> dataObjects =
            new HashMap<String, DataObject>();
    /**
     * The DataNode which is part of the termination Condition.
     */
    private Node terminatingDataNode;
    /**
     * terminatingDataObject is a dataObject.
     * It is part of the termination condition.
     */
    private DataObject terminatingDataObject;
    /**
     * The version of the current Scenario
     */
    private int versionNumber;


    public Scenario(String serverURL) {
        processeditorServerUrl = serverURL;
    }

    /**
     * This Method initializes the scanario from an XML.
     * Be Aware, that a scenario consists of fragments, which will
     * be loaded automatically;
     *
     * @param element
     */
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {
        this.scenarioXML = element;
        setScenarioName();
        setScenarioID();
        generateFragmentList();
        createDataObjects();
        setTerminationCondition();
        setVersionNumber();
    }

    /**
     * Extracts the Version from the XML.
     * Corresponding values will be saved to the corresponding fields.
     */
    private void setVersionNumber() {
        Element versionXML = fetchVersionXML();
        if (versionXML != null) {
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery = "/versions/version";
            try {
                NodeList versions = (NodeList) xPath.compile(xPathQuery).evaluate(versionXML, XPathConstants.NODESET);
                int maxID = 0;
                // We assume that the version that needs to be saved is the newest one
                //TODO: Do we want to save versions that are currently not in the Database?
                for (int i = 0; i < versions.getLength(); i++) {
                    xPathQuery = "@id";
                    int currentID = Integer.parseInt((String) xPath.compile(xPathQuery).evaluate(versions.item(i), XPathConstants.STRING));
                    if (maxID < currentID)
                        maxID = currentID;
                }
                versionNumber = maxID;
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the XML which contains all the versions of the current scenario from the processEditorServer
     */
    private Element fetchVersionXML() {
        try {
            Retrieval jRetrieval = new Retrieval();
            String versionXML = jRetrieval.getHTMLwithAuth(
                    processeditorServerUrl,
                    processeditorServerUrl + "models/" + scenarioID + "/versions");
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(versionXML));
            DocumentBuilder db = null;
            db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(is);
            return doc.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extracts the TerminationCondition from the XML.
     * Corresponding values will be saved to the corresponding fields.
     */
    private void setTerminationCondition() {
        String objectName = getTerminatingObjectName();
        String objectState = getTerminatingObjectState();
        if (objectName.equals("") || objectState.equals("[]")) {
            return;
        }

        for (Map.Entry<String, DataObject> dataObject
                : dataObjects.entrySet()) {
            if (dataObject.getKey().equals(objectName)) {
                for (Node dataNode : dataObject.getValue().getDataNodes()) {
                    // TODO: state in terminationCondition in Scenario-XML and in Node of Fragment-XML should not differ in the usage of "[]"
                    if (objectState.equals("[" + dataNode.getState() + "]")) {
                        terminatingDataNode = dataNode;
                    }
                }
                terminatingDataObject = dataObject.getValue();
            }
        }
    }

    /**
     * Gets the state which is part of the termination Condition from the xml.
     *
     * @return returns the state if possible else null.
     */
    private String getTerminatingObjectState() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/properties/property[@name = 'Termination State']/@value";
        try {
            return xPath.compile(xPathQuery).evaluate(this.scenarioXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extracts the data name of the DataObject from the termination condition.
     * It plays a role in the termination Condition from the xml.
     *
     * @return null if extraction failed, else the name.
     */
    private String getTerminatingObjectName() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/properties/property[@name = 'Termination Data Object']/@value";
        try {
            return xPath.compile(xPathQuery).evaluate(this.scenarioXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extracts and saves Scenario ID from the ModelXML.
     */
    private void setScenarioID() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@id";
        try {
            this.scenarioID = Long.parseLong(xPath
                    .compile(xPathQuery)
                    .evaluate(this.scenarioXML));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int save() {
        Connector conn = new Connector();
        this.databaseID = conn.insertScenarioIntoDatabase(this.scenarioName, scenarioID, versionNumber);
        saveFragments();
        saveDataObjects();
        saveConsistsOf();
        if (terminatingDataObject != null && terminatingDataNode != null) {
            saveTerminationCondition();
        }
        saveReferences();
        return this.databaseID;
    }

    /**
     * Save the referenced activities of the Scenario to the database
     */
    private void saveReferences() {
        HashMap<Integer, List<Integer>> activities = getActivityDatabaseIDsForEachActivityModelID();
        Connector conn = new Connector();
        for (Map.Entry<Integer, List<Integer>> activity : activities.entrySet()) {
            // if List has size 1, there is no reference to this activity as they are indicated by the same model-ID
            if (activity.getValue().size() > 1) {
                int firstIndex = 0;
                int secondIndex = firstIndex + 1;
                for (int i = 0; i < activity.getValue().size()-1; i++) {
                    while (secondIndex <= activity.getValue().size()-1) {
                        conn.insertReferenceIntoDatabase(activity.getValue().get(firstIndex), activity.getValue().get(secondIndex));
                        secondIndex++;
                    }
                    firstIndex++;
                    secondIndex = firstIndex + 1;
                }
            }
        }
    }

    /**
     * for each model-ID of the activities collect database-Ids of activities that share the same model-ID
     */
    private HashMap<Integer,List<Integer>> getActivityDatabaseIDsForEachActivityModelID() {
        HashMap<Integer, List<Integer>> result = new HashMap<>();
        for (Fragment fragment : fragments) {
            Map<Integer, Node> fragmentNodes = fragment.getControlNodes();
            for (Map.Entry<Integer, Node> node : fragmentNodes.entrySet()) {
                if (node.getValue().isTask()) {
                    if (result.get(node.getKey()) == null) {
                        List<Integer> activityDatabaseIDs = new ArrayList<Integer>();
                        activityDatabaseIDs.add(node.getValue().getDatabaseID());
                        result.put(node.getKey(), activityDatabaseIDs);
                    } else {
                        result.get(node.getKey()).add(node.getValue().getDatabaseID());
                    }
                }
            }
        }
        return result;
    }

    /**
     * Saves the terminationCondition of the Fragment to the database.
     */
    private void saveTerminationCondition() {
        Connector conn = new Connector();
        // first parameter currently irrelevant due to the restriction of the terminationCondition
        conn.insertTerminationConditionIntoDatabase(1,
                terminatingDataObject.getDatabaseId(),
                terminatingDataObject.getStates().get(
                        terminatingDataNode.getState()),
                databaseID);
    }

    /**
     * Saves the Fragments of the Scenario to the Database.
     * First the Scenarios have to be saved and
     * the Fragments have to be initialized.
     */
    private void saveFragments() {
        for (Fragment fragment : fragments) {
            fragment.setScenarioID(databaseID);
            fragment.save();
        }
    }

    /**
     * Saves the DataObjects of the Scenario to the Database.
     * First the Scenarios have to be saved and
     * the DataObjects have to be initialized.
     */
    private void saveDataObjects() {
        for (DataObject dataObject : dataObjects.values()) {
            dataObject.setScenarioId(databaseID);
            dataObject.save();
        }
    }


    /**
     * Saves the relation of DataSets to DataNodes to the Database.
     * Take care that all DtaNode and Input and Output nodes are implemented
     */
    private void saveConsistsOf() {
        for (Fragment frag : fragments) {
            saveInputSetsConsistOf(frag);
            saveOutputSetsConsistOf(frag);
        }
    }

    /**
     * Saves the relation of a output set to data nodes.
     * Only the nodes of one Fragment will be represented.
     *
     * @param frag The fragment which contains the Output set
     */
    private void saveOutputSetsConsistOf(final Fragment frag) {
        Connector connector = new Connector();
        for (OutputSet oSet : frag.getOutputSets()) {
            for (Node dataNode : oSet.getOutputs()) {
                connector.insertDataSetConsistOfDataNodeIntoDatabase(
                        oSet.getDatabaseId(),
                        dataNode.getDatabaseID());
            }
        }
    }

    /**
     * Saves the relation of input sets to data nodes.
     * Only one fragment will be taken into consideration.
     *
     * @param frag The fragment which contains the Input set
     */
    private void saveInputSetsConsistOf(final Fragment frag) {
        Connector connector = new Connector();
        for (InputSet iSet : frag.getInputSets()) {
            for (Node dataNode : iSet.getInputs()) {
                connector.insertDataSetConsistOfDataNodeIntoDatabase(
                        iSet.getDatabaseId(),
                        dataNode.getDatabaseID());
            }
        }
    }

    /**
     * Saves the dataObjects of the scenario to the database.
     * Fragments (with Control Nodes) have to be created and saved first.
     */
    private void createDataObjects() {
        for (Fragment fragment : fragments) {
            for (Node node : fragment.getControlNodes().values()) {
                if (node.isDataNode()) {
                    if (null == dataObjects.get(node.getText())) {
                        dataObjects.put(node.getText(), new DataObject());
                    }
                    dataObjects.get(node.getText()).addDataNode(node);
                }
            }
        }
    }

    /**
     * Generates a List of Fragments from the ScenarioXML.
     */
    private void generateFragmentList() {
        try {
            //look for all fragments in the scenarioXML and save their IDs
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery =
                    "/model/nodes/node/property[@name = '#type' and " +
                    "@value = 'net.frapu.code.visualization.pcm.PCMFragmentNode']" +
                    "/preceding-sibling::property[@name='fragment mid']/@value";
            NodeList fragmentIDsList = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.scenarioXML, XPathConstants.NODESET);

            // create URI from fragmentID and retrieve xml for all fragments of the scenario
            Retrieval jRetrieval = new Retrieval();
            this.fragments = new ArrayList<Fragment>(fragmentIDsList.getLength());
            String currentFragmentXML;
            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            Document doc;

            for (int i = 0; i < fragmentIDsList.getLength(); i++) {
                currentFragmentXML = jRetrieval.getHTMLwithAuth(
                        this.processeditorServerUrl,
                        this.processeditorServerUrl +
                                "models/" +
                                fragmentIDsList.item(i).getNodeValue() +
                                ".pm");
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(
                        new InputSource(
                                new ByteArrayInputStream(
                                        currentFragmentXML.getBytes("utf-8"))));
                doc.getDocumentElement().normalize();
                Fragment fragment = new Fragment(processeditorServerUrl);
                fragment.initializeInstanceFromXML(
                        (org.w3c.dom.Node) doc.getDocumentElement());
                this.fragments.add(fragment);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the name of the Scenario.
     * The name is based on the value of the "name" attribute
     * of the ScenarioXML Root-Element.
     */
    private void setScenarioName() {

        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            this.scenarioName = xPath
                    .compile(xPathQuery)
                    .evaluate(this.scenarioXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
