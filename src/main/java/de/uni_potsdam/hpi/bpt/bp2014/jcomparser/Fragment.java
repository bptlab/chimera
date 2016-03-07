package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.Set;
import java.util.stream.Collectors;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.*;
import de.uni_potsdam.hpi.bpt.bp2014.util.CollectionUtil;
import jersey.repackaged.com.google.common.collect.Sets;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 */
public class Fragment {

    private final int scenarioId;
    private String fragmentName;
    private String fragmentId;
    private int versionNumber;
    private FragmentXmlWrapper fragment;

    public Fragment(String fragmentXml, int versionNumber, String fragmentName,
                    int scenarioId, String fragmentId) {
        this.fragment = buildFragment(fragmentXml);
        this.fragmentName = fragmentName;
        this.versionNumber = versionNumber;
        this.scenarioId = scenarioId;
        this.fragmentId = fragmentId;
    }

    public int save() {
        Connector connector = new Connector();
        int databaseId = connector.insertFragmentIntoDatabase(fragmentName,
                scenarioId, fragmentId, versionNumber);
        return databaseId;
    }

    private FragmentXmlWrapper buildFragment(String fragmentXml) {
        Document doc = getXmlDocFromString(fragmentXml);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(FragmentXmlWrapper.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (FragmentXmlWrapper) jaxbUnmarshaller.unmarshal(doc);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document getXmlDocFromString(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OutputSet> getOutputSets() {
        List<OutputSet> sets = new ArrayList<>();
        Map<String, DataNode> idToDataNode = new HashMap<>();
        for (DataNode dataNode : this.getDataNodes()) {
            idToDataNode.put(dataNode.getId(), dataNode);
        }

        for (Task task : this.fragment.getTasks()) {
            sets.addAll(getOutputSetsForTask(task, idToDataNode));
        }
        return sets;
    }

    private List<OutputSet> getOutputSetsForTask(Task task, Map<String, DataNode> idToDataNode) {
        Map<String, List<DataNode>> dataNodeToStates = new HashMap<>();
        for (DataOutputAssociation assoc : task.getDataOutputAssociations()) {
            DataNode dataNode =  idToDataNode.get(assoc.getTargetRef());
            if(!dataNodeToStates.containsKey(dataNode.getName())) {
                dataNodeToStates.put(dataNode.getName(), new ArrayList<>());
            }
            dataNodeToStates.get(dataNode.getName()).add(dataNode);
        }
        List<List<DataNode>> datanodeCombinations = CollectionUtil.computeCartesianProduct(
                new ArrayList<>(dataNodeToStates.values()));

        return datanodeCombinations.stream().map(combination ->
                new OutputSet(task, combination)).collect(Collectors.toList());
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

        for (Task task : this.fragment.getTasks()) {
            sets.addAll(getInputSetsForTask(task, idToDataNode));
        }
        return sets;
    }

    public List<InputSet> getInputSetsForTask(Task task, Map<String, DataNode> idToDataNode) {
        Map<String, List<DataNode>> dataNodeToStates = new HashMap<>();
        for (DataInputAssociation assoc : task.getDataInputAssociations()) {
            DataNode dataNode =  idToDataNode.get(assoc.getSourceRef());
            if(!dataNodeToStates.containsKey(dataNode.getName())) {
                dataNodeToStates.put(dataNode.getName(), new ArrayList<>());
            }
            dataNodeToStates.get(dataNode.getName()).add(dataNode);
        }
        List<List<DataNode>> datanodeCombinations = CollectionUtil.computeCartesianProduct(
                new ArrayList<>(dataNodeToStates.values()));

        return datanodeCombinations.stream().map(combination ->
                new InputSet(task, combination)).collect(Collectors.toList());
    }

    private Map<String, Task> createMapFromIdToTask() {
        List<Task> tasks = this.fragment.getTasks();
        Map<String, Task> idToNode = new HashMap<>();
        for (Task task : tasks) {
            idToNode.put(task.getId(), task);
        }

        return idToNode;
    }

    /**
     *
     * @return Return a list of all control nodes in a fragment
     */
    public List<AbstractControlNode> getControlNodes() {
        List<AbstractControlNode> nodes = new ArrayList<>();
        nodes.addAll(this.fragment.getXorGateways());
        nodes.addAll(this.fragment.getTasks());
        nodes.addAll(this.fragment.getIntermediateEvents());
        nodes.addAll(this.fragment.getBoundaryEvents());
        nodes.addAll(this.fragment.getEventBasedGateways());
        nodes.add(this.fragment.getEndEvent());
        nodes.add(this.fragment.getStartEvent());
        return nodes;
    }

    public List<BoundaryEvent> getBoundaryEventNodes() {
        return this.fragment.getBoundaryEvents();
    }




    public List<DataInputAssociation> getDataInputAssociations() {
        List<DataInputAssociation> overallEdges = new ArrayList<>();
        for (Task task : this.fragment.getTasks()) {
            for (DataInputAssociation association : task.getDataInputAssociations()) {
                overallEdges.add(association);
            }
        }

        return overallEdges;
    }

    public List<DataOutputAssociation> getDataOutputAssociations() {
        List<DataOutputAssociation> overallEdges = new ArrayList<>();
        for (Task task : this.fragment.getTasks()) {
            for (DataOutputAssociation association : task.getDataOutputAssociations()) {
                overallEdges.add(association);
            }
        }
        return overallEdges;
    }

    public List<SequenceFlow> getSequenceFlow() {
        return this.fragment.getSequenceFlow();
    }

    public List<DataNode> getDataNodes() {
        return this.fragment.getDataNodes();
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(String fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getName() {
        return fragmentName;
    }

    public void setName(String name) {
        this.fragmentName = name;
    }

}
