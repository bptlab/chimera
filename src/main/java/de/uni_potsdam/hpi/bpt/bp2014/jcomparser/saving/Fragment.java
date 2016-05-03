package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.AbstractDataControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.BoundaryEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataInputAssociation;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataOutputAssociation;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.FragmentXmlWrapper;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.SequenceFlow;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Task;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.WebServiceTask;
import de.uni_potsdam.hpi.bpt.bp2014.util.CollectionUtil;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class Fragment {
    private static Logger logger = Logger.getLogger(Fragment.class.getName());

    private int scenarioId;
    private String fragmentName;
    private String fragmentEditorId;
    private int versionNumber;
    private FragmentXmlWrapper fragmentXml;

    public Fragment(String fragmentXml, int versionNumber, String fragmentName,
                    String fragmentEditorId) throws JAXBException {
        this.fragmentXml = buildFragment(fragmentXml);
        this.fragmentName = fragmentName;
        this.versionNumber = versionNumber;
        this.fragmentEditorId = fragmentEditorId;
    }

    public int save() {
        Connector connector = new Connector();
        return connector.insertFragmentIntoDatabase(fragmentName,
                scenarioId, fragmentEditorId, versionNumber);
    }

    private FragmentXmlWrapper buildFragment(String fragmentXml) throws JAXBException {
        Document doc = getXmlDocFromString(fragmentXml);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(FragmentXmlWrapper.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (FragmentXmlWrapper) jaxbUnmarshaller.unmarshal(doc);
        } catch (JAXBException e) {
            logger.error(e);
            throw new JAXBException("Fragment xml was not valid");
        }
    }

    private Document getXmlDocFromString(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            logger.error(e);
            throw new IllegalArgumentException("Creation of xml fragment failed");
        }
    }

    public List<OutputSet> getOutputSets() {
        List<OutputSet> sets = new ArrayList<>();
        Map<String, DataNode> idToDataNode = new HashMap<>();
        for (DataNode dataNode : this.getDataNodes()) {
            idToDataNode.put(dataNode.getId(), dataNode);
        }

        for (AbstractDataControlNode node : this.fragmentXml.getAllDataControlNodes()) {
            sets.addAll(getOutputSetsForNode(node, idToDataNode));
        }

        return sets;
    }

    private List<OutputSet> getOutputSetsForNode(AbstractDataControlNode node, Map<String, DataNode> idToDataNode) {
        Map<String, List<DataNode>> dataNodeToStates = new HashMap<>();
        for (DataOutputAssociation assoc : node.getDataOutputAssociations()) {
            DataNode dataNode =  idToDataNode.get(assoc.getTargetRef());
            if(!dataNodeToStates.containsKey(dataNode.getName())) {
                dataNodeToStates.put(dataNode.getName(), new ArrayList<>());
            }
            dataNodeToStates.get(dataNode.getName()).add(dataNode);
        }
        List<List<DataNode>> datanodeCombinations = CollectionUtil.computeCartesianProduct(
                new ArrayList<>(dataNodeToStates.values()));

        return datanodeCombinations.stream().map(combination ->
                new OutputSet(node, combination)).collect(Collectors.toList());
    }


    /**
     *
     * @return List of Input sets
     */
    public List<InputSet> getInputSets() {
        List<InputSet> sets = new ArrayList<>();
        Map<String, DataNode> idToDataNode = new HashMap<>();
        for (DataNode dataNode : this.getDataNodes()) {
            idToDataNode.put(dataNode.getId(), dataNode);
        }

        for (AbstractDataControlNode node : this.fragmentXml.getAllDataControlNodes()) {
            sets.addAll(getInputSetsForNode(node, idToDataNode));
        }

        return sets;
    }

    public List<InputSet> getInputSetsForNode(AbstractDataControlNode node, Map<String, DataNode> idToDataNode) {
        Map<String, List<DataNode>> dataNodeToStates = new HashMap<>();
        for (DataInputAssociation assoc : node.getDataInputAssociations()) {
            DataNode dataNode =  idToDataNode.get(assoc.getSourceRef());
            if(!dataNodeToStates.containsKey(dataNode.getName())) {
                dataNodeToStates.put(dataNode.getName(), new ArrayList<>());
            }
            dataNodeToStates.get(dataNode.getName()).add(dataNode);
        }

        if (!dataNodeToStates.isEmpty()) {
            List<List<DataNode>> datanodeCombinations = CollectionUtil.computeCartesianProduct(
                    new ArrayList<>(dataNodeToStates.values()));
            return datanodeCombinations.stream().map(combination ->
                    new InputSet(node, combination)).collect(Collectors.toList());
        } else {
            return new ArrayList<InputSet>();
        }
    }

    private Map<String, Task> createMapFromIdToTask() {
        List<AbstractDataControlNode> tasks = this.fragmentXml.getAllActivities();
        Map<String, Task> idToNode = new HashMap<>();
        for (AbstractDataControlNode task : tasks) {
            idToNode.put(task.getId(), (Task) task);
        }

        return idToNode;
    }

    /**
     *
     * @return Return a list of all control nodes in a fragment
     */
    public List<AbstractControlNode> getControlNodes() {
        List<AbstractControlNode> nodes = new ArrayList<>();
        nodes.addAll(this.fragmentXml.getXorGateways());
        nodes.addAll(this.fragmentXml.getAndGateways());
        nodes.addAll(this.fragmentXml.getAllActivities());
        nodes.addAll(this.fragmentXml.getReceiveTasks());
        nodes.addAll(this.fragmentXml.getIntermediateEvents());
        nodes.addAll(this.fragmentXml.getBoundaryEvents());
        nodes.addAll(this.fragmentXml.getEventBasedGateways());
        nodes.add(this.fragmentXml.getEndEvent());
        nodes.add(this.fragmentXml.getStartEvent());
        return nodes;
    }

    public List<BoundaryEvent> getBoundaryEventNodes() {
        return this.fragmentXml.getBoundaryEvents();
    }


    public List<SequenceFlow> getSequenceFlow() {
        return this.fragmentXml.getSequenceFlow();
    }

    public List<DataNode> getDataNodes() {
        return this.fragmentXml.getDataNodes();
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getFragmentEditorId() {
        return fragmentEditorId;
    }

    public void setFragmentEditorId(String fragmentEditorId) {
        this.fragmentEditorId = fragmentEditorId;
    }

    public String getName() {
        return fragmentName;
    }

    public void setName(String name) {
        this.fragmentName = name;
    }

    public List<Task> getTasks() {
        return this.fragmentXml.getTasks();
    }

    public List<WebServiceTask> getWebServiceTasks() {
        return this.fragmentXml.getWebServiceTasks();
    }

    public List<AbstractDataControlNode> getAllActivities() {
        return this.fragmentXml.getAllActivities();
    }
}
