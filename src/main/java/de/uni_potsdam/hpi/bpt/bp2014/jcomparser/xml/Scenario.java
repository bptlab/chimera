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
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

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
    private Map<String, DataObject> dataObjects;
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
     * The version of the current Scenario.
     */
    private int versionNumber;
    /**
     * Marks if the scenario needs to be saved.
     * If none of the fragments is changed and none is added or removed, and there is no newer
     * version of the scenario, it does not need to be saved and
     * the variable holds value false.
     */
    private boolean needsToBeSaved;
    /**
     * If the scenario contains new fragments that a older version in the database does not
     * contain, all running instances are migrated to this scenario.
     * Variable holds value true if such a migration is necessary.
     */
    private boolean migrationNecessary;
    /**
     * If migration is necessary, we need the latest version of the scenario that should be migrated.
     */
    private int migratingScenarioVersion = -1;
    private String domainModelURI;
    private Element domainModelXML;
    private DomainModel domainModel;
    /**
     * If migration is necessary, this variable contains all the fragments that are new and not in the older version.
     */
    private List<Fragment> newFragments;

    /**
     * Creates a new Scenario Object and saves the PE-ServerURL.
     * The Scenario needs knowledge of the serverURL, if and
     * only if it should be initialized from XML.
     * Hence, it will load the fragments from the ProcessEditor.
     *
     * @param serverURL The URL of the ProcessEditor Server.
     */
    public Scenario(final String serverURL) {
        processeditorServerUrl = serverURL;
    }


    /**
     * This Method initializes the scenario from an XML.
     * Be Aware, that a scenario consists of fragments, which will
     * be loaded automatically;
     *
     * @param element The XML-representation for the scenario.
     */
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {
        this.scenarioXML = element;
        setScenarioName();
        setScenarioID();
        generateFragmentList();
        setDomainModelURL();
        setDomainModel();
        createDataObjects();
        setTerminationCondition();
        setVersionNumber();
        checkIfVersionAlreadyInDatabase();

    }

    private void setDomainModel() {
        domainModelXML = fetchDomainModelXML();
        if(domainModelXML != null) {
            domainModel = new DomainModel(processeditorServerUrl);
            domainModel.initializeInstanceFromXML(domainModelXML);
        }
    }

    private Element fetchDomainModelXML() {
        try {
            Retrieval jRetrieval = new Retrieval();
            String versionXML = jRetrieval.getHTMLwithAuth(
                    processeditorServerUrl,
                    domainModelURI);
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(versionXML));
            DocumentBuilder db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(is);
            return doc.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setDomainModelURL() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/properties/property" +
                "[@name='domainModelURI']/@value";
        try {
            domainModelURI = xPath.compile(xPathQuery).evaluate(this.scenarioXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the versions of all fragments and of this scenario are already in the database.
     * If so, the scenario does not need to be saved once again
     */
    private void checkIfVersionAlreadyInDatabase() {
        Connector connector = new Connector();
        long fragmentModelID;
        int newestFragmentDatabaseVersion;
        int scenarioVersion = connector.getNewestScenarioVersion(scenarioID);
        boolean changesMade = false;
        newFragments = new LinkedList<>();
        for (Fragment fragment : fragments) {
            fragmentModelID = fragment.getFragmentID();
            newestFragmentDatabaseVersion = connector.getNewestFragmentVersion(fragmentModelID, scenarioID);
            // case 1: We don't have a fragment with this modelid in the database
            if (newestFragmentDatabaseVersion == -1) {
                needsToBeSaved = true;
                // ... this might have two reasons:
                // 1) the scenario is not in the database yet
                if (scenarioVersion == -1) {
                    migrationNecessary = false;
                }
                // 2) a new fragment has been added
                else {
                    migrationNecessary = true;
                    newFragments.add(fragment);
                    migratingScenarioVersion = scenarioVersion;
                }
            }
            // case 2: an existing fragment has been modified: we got a newer version of the fragment here
            else if (newestFragmentDatabaseVersion < fragment.getVersion()) {
                needsToBeSaved = true;
                changesMade = true;
            }
        }
        // this evaluation is necessary as otherwise the value of migrationNecessary is influenced by the
        // ordering of the fragments
        if (changesMade) {
            migrationNecessary = false;
        }
        // case 3: we have a newer version of the scenario (e.g. fragment has been removed)
        // or scenario does not exist in database (scenarioVersion = -1)
        // (if scenarioVersion is -1 we get here only if this is a scenario without any fragments)
        if (scenarioVersion < versionNumber) {
            needsToBeSaved = true;
        }
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
                NodeList versions = (NodeList) xPath
                        .compile(xPathQuery)
                        .evaluate(versionXML, XPathConstants.NODESET);
                int maxID = 0;
                // the current version is the latest one
                for (int i = 0; i < versions.getLength(); i++) {
                    xPathQuery = "@id";
                    int currentID = Integer.parseInt((String) xPath
                            .compile(xPathQuery)
                            .evaluate(versions.item(i),
                                    XPathConstants.STRING));
                    if (maxID < currentID) {
                        maxID = currentID;
                    }
                }
                versionNumber = maxID;
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the XML which contains all the versions.
     * Connects to the ProcessEditor Server, in order
     * to fetch all versions of the current scenario.
     *
     * @return A Dom-Element containing all the version information.
     */
    private Element fetchVersionXML() {
        try {
            Retrieval jRetrieval = new Retrieval();
            String versionXML = jRetrieval.getHTMLwithAuth(
                    processeditorServerUrl,
                    processeditorServerUrl + "models/" +
                            scenarioID + "/versions");
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(versionXML));
            DocumentBuilder db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(is);
            return doc.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
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
        String xPathQuery = "/model/properties/property" +
                "[@name='Termination State']/@value";
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
        String xPathQuery = "/model/properties/property" +
                "[@name = 'Termination Data Object']/@value";
        try {
            return xPath
                    .compile(xPathQuery)
                    .evaluate(this.scenarioXML);
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
        if (needsToBeSaved) {
            Connector conn = new Connector();
            this.databaseID = conn.insertScenarioIntoDatabase(
                    this.scenarioName,
                    scenarioID,
                    versionNumber);
            saveFragments();
            domainModel.save();
            saveDataObjects();
            saveConsistsOf();
            if (terminatingDataObject != null && terminatingDataNode != null) {
                saveTerminationCondition();
            }
            saveReferences();
            if (migrationNecessary){
                migrateRunningInstances();
            }
            return this.databaseID;
        }
        return -1;
    }

    /**
     * Migrate running instances with the modelId of this scenario and with the migratingScenarioVersion.
     */
    private void migrateRunningInstances() {
        Connector connector = new Connector();
        int oldScenarioDbID = connector.getScenarioID(scenarioID, migratingScenarioVersion);
        // get the scenarioInstanceIDs of all running instances that need to be migrated
        // and migrate them (means changing their old reference to the scenario to this scenario)
        connector.migrateScenarioInstance(oldScenarioDbID, databaseID);
        //migrate FragmentInstances
        for(Fragment fragment : fragments) {
            // as there is no fragmentinstance for this new fragment in the database so far,
            // we don't need to change references
            if (!newFragments.contains(fragment)) {
                fragment.migrate(oldScenarioDbID);
            }
        }
        migrateDataObjects(oldScenarioDbID);
    }

    private void migrateDataObjects(int oldScenarioDbID) {
        Connector connector = new Connector();
        int oldDataObjectDbID;
        for (Map.Entry<String, DataObject> dataObject : dataObjects.entrySet()) {
            oldDataObjectDbID = connector.getDataObjectID(oldScenarioDbID, dataObject.getKey());
            if (oldDataObjectDbID > 0 ) {
                connector.migrateDataObjectInstance(oldDataObjectDbID, dataObject.getValue().getDatabaseId());
            }
        }
    }

    /**
     * Save the referenced activities of the Scenario to the database.
     * There fore search for all pairs of activities, that are referenced.
     * And save these pairs inside the database. (Order is irrelevant,
     * because the {@link de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector}
     * does this for you.
     */
    private void saveReferences() {
        /* Key is the ID used inside the model, value are a List of
         * all IDs used inside the database. */
        HashMap<Long, List<Integer>> activities =
                getActivityDatabaseIDsForEachActivityModelID();
        Connector conn = new Connector();
        for (List<Integer> databaseIDs : activities.values()) {
            if (databaseIDs.size() > 1) {
                // The next two loops find all pairs of referenced activities.
                for (int i = 0; i < databaseIDs.size() - 1; i++) {
                    for (int j = i + 1; j < databaseIDs.size(); j++) {
                        conn.insertReferenceIntoDatabase(databaseIDs.get(i),
                                databaseIDs.get(j));
                    }
                }
            }
        }
    }

    /**
     * Get all referenced activities with their model ID and databaseIDs.
     * If two activities are referenced their model IDs are the same,
     * but they have different IDs inside the database.
     *
     * @return A map of all activity-model-IDs to a list of their database IDs.
     */
    private HashMap<Long, List<Integer>> getActivityDatabaseIDsForEachActivityModelID() {
        HashMap<Long, List<Integer>> result = new HashMap<>();
        for (Fragment fragment : fragments) {
            Map<Long, Node> fragmentNodes = fragment.getControlNodes();
            for (Map.Entry<Long, Node> node : fragmentNodes.entrySet()) {
                if (node.getValue().isTask()) {
                    if (result.get(node.getKey()) == null) {
                        List<Integer> activityDatabaseIDs =
                                new ArrayList<Integer>();
                        activityDatabaseIDs.add(
                                node.getValue().getDatabaseID());
                        result.put(node.getKey(), activityDatabaseIDs);
                    } else {
                        result.get(node.getKey())
                                .add(node.getValue().getDatabaseID());
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
        // first parameter currently irrelevant,
        // due to the restriction of the terminationCondition
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
        dataObjects = new HashMap<String, DataObject>();
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
            //look for all fragments in the scenarioXML and save their node
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery =
                    "/model/nodes/node[property[@name = '#type' and " +
                            "@value = 'net.frapu.code.visualization.pcm.PCMFragmentNode']]";
            NodeList fragmentNodes = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.scenarioXML, XPathConstants.NODESET);
            this.fragments = new LinkedList<Fragment>();

            for (int i = 0; i < fragmentNodes.getLength(); i++) {
                // get the ID of the current node
                xPathQuery = "property[@name = 'fragment mid']/@value";
                String currentNodeID = xPath
                        .compile(xPathQuery)
                        .evaluate(fragmentNodes.item(i));
                this.fragments.add(createAndInitializeFragment(currentNodeID));
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a Fragment based on a specific fragmentID.
     * Therefore it fetches the XML from a Server.
     *
     * @param fragmentID The ID of the Fragment.
     * @return The newly created Fragment Object.
     */
    private Fragment createAndInitializeFragment(String fragmentID) {
        Retrieval retrieval = new Retrieval();
        String fragmentXML = retrieval.getHTMLwithAuth(
                this.processeditorServerUrl,
                this.processeditorServerUrl +
                        "models/" + fragmentID + ".pm");
        Fragment fragment = new Fragment(processeditorServerUrl);
        fragment.initializeInstanceFromXML(stringToDocument(fragmentXML));
        return fragment;
    }

    /**
     * Casts a XML from its String Representation to a w3c Document.
     *
     * @param xml The String representation of the XML.
     * @return The from String created Document.
     */
    private Document stringToDocument(final String xml) {
        try {
            DocumentBuilder db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
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

    // Getters - Currently used for Tests only

    /**
     * Returns the name of the Scenario.
     *
     * @return The Name of the Scenario
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * Returns the ID of the scenario used in the XML.
     *
     * @return The id from the XML.
     */
    public long getScenarioID() {
        return scenarioID;
    }

    /**
     * Returns the Node Object, which represents the XML.
     *
     * @return the XML of representing the Scenario.
     */
    public org.w3c.dom.Node getScenarioXML() {
        return scenarioXML;
    }

    /**
     * Returns a List of all Fragments.
     * Be aware, that changes to this List
     * will change the state of the scenario.
     *
     * @return the List of fragments.
     */
    public List<Fragment> getFragments() {
        return fragments;
    }

    /**
     * The URL of the ProcessEditor Server.
     *
     * @return The PE-Server URL.
     */
    public String getProcesseditorServerUrl() {
        return processeditorServerUrl;
    }

    /**
     * Returns the Database ID of the Scenario.
     * Returns -1 if Scenario is not saved to the database.
     *
     * @return The ID used inside the database.
     */
    public int getDatabaseID() {
        return databaseID;
    }

    /**
     * Returns a List of all DataObjects of the Scenario.
     * Changes will affect the scenario directly.
     *
     * @return the DataObjects of the scenario.
     */
    public Map<String, DataObject> getDataObjects() {
        return dataObjects;
    }

    /**
     * Returns the DataNode, which is part of the TerminationCondition.
     *
     * @return the dataNode of the TerminationCondition.
     */
    public Node getTerminatingDataNode() {
        return terminatingDataNode;
    }

    /**
     * Returns the DataObject, which is represented by the TerminatingDataNode.
     *
     * @return the DataObject which is part of the TerminationCondition.
     */
    public DataObject getTerminatingDataObject() {
        return terminatingDataObject;
    }

    /**
     * Returns an integer Representing the version of the Scenario.
     *
     * @return the Version of the Scenario.
     */
    public int getVersionNumber() {
        return versionNumber;
    }

    /**
     * Returns a boolean which marks if migration is necessary.
     * Used for testCases only so far.
     *
     * @return 1 if migration is necessary (0 otherwise)
     */
    public boolean isMigrationNecessary() {
        return migrationNecessary;
    }

    public String getDomainModelURI() {
        return domainModelURI;
    }

    public Element getDomainModelXML() {
        return domainModelXML;
    }

    public DomainModel getDomainModel() {
        return domainModel;
    }

}
