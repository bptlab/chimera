package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.*;
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
    private int fragmentId;
    private int versionNumber;
    private FragmentXmlWrapper fragment;

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getName() {
        return fragmentName;
    }

    public void setName(String name) {
        this.fragmentName = name;
    }

    public Fragment(String fragmentXml, int versionNumber, String fragmentName,
                    int scenarioId, int fragmentId) {
        this.fragment = buildFragment(fragmentXml);
        this.fragmentName = fragmentName;
        this.versionNumber = versionNumber;
        this.scenarioId = scenarioId;
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
            JAXBContext jaxbContext = JAXBContext.newInstance(Fragment.class);
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
    /**
     *
     * @return List of Input sets
     */
    public List<InputSet> getInputSets() {
        List<InputSet> sets = new ArrayList<>();
        Map<String, Node> idToNode = createMapFromIdToNode();
        for (Task task : this.fragment.getTasks()) {
            List<Edge> associations = new ArrayList<>();
            List<Node> dataNodes = new ArrayList<>();
            for (DataInputAssociation assoc : task.getDataInputAssociations()) {
                associations.add(assoc);
                Node dataNode =  idToNode.get(assoc.getSourceRef());
                dataNodes.add(dataNode);
            }
            if (associations.size() > 0) {
                InputSet set = new InputSet();
                set.setNode(task.convertToNode());
                set.setAssociations(associations);
                set.setDataNodes(dataNodes);
                sets.add(set);
            }
        }

        return sets;
    }

    private Map<String, Node> createMapFromIdToNode() {
        List<Node> nodes = getNodes();
        Map<String, Node> idToNode = new HashMap<>();
        for (Node node : nodes) {
            idToNode.put(node.getId(), node);
        }

        return idToNode;
    }

    /**
     *
     * @return Returns something
     */
    public List<OutputSet> getOutputSets() {
        List<OutputSet> outputSets = new ArrayList<>();
        Map<String, Node> idToNode = createMapFromIdToNode();

        for (Task task : this.fragment.getTasks()) {
            List<Edge> associations = new ArrayList<>();
            List<Node> dataNodes = new ArrayList<>();
            for (DataOutputAssociation assoc : task.getDataOutputAssociations()) {
                associations.add(assoc);
                Node dataNode = idToNode.get(assoc.getTargetRef());
                dataNodes.add(dataNode);
            }
            if (associations.size() > 0) {
                OutputSet set = new OutputSet();
                set.setNode(task.convertToNode());
                set.setAssociations(associations);
                set.setDataNodes(dataNodes);
                outputSets.add(set);
            }
        }
        return outputSets;
    }

    /**
     *
     * @return Return something
     */
    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(getExclusiveGateways());
        nodes.addAll(getTaskNodes());
        nodes.addAll(getDataObjectNodes());
        nodes.addAll(getBoundaryEventNodes());
        nodes.add(getEndEventNode());
        nodes.add(getStartEventNode());
        return nodes;
    }

    private Node getEndEventNode() {
        Node endEvent = new Node();
        endEvent.setId(this.fragment.getEndEvent().getId());
        endEvent.setType("EndEvent");
        endEvent.setText(this.fragment.getEndEvent().getName());
        endEvent.setGlobal(false);
        return endEvent;
    }

    private Node getStartEventNode() {
        Node startEvent = new Node();
        startEvent.setId(this.fragment.getStartEvent().getId());
        startEvent.setType("StartEvent");
        startEvent.setText(this.fragment.getStartEvent().getName());
        startEvent.setGlobal(false);
        return startEvent;
    }

    private List<Node> getBoundaryEventNodes() {
        List<Node> events = new ArrayList<>();

        for (BoundaryEvent event : this.fragment.getBoundaryEvents()) {
            Node boundaryEvent = new Node();
            boundaryEvent.setId(event.getId());
            boundaryEvent.setGlobal(false);
            boundaryEvent.setText(event.getName());
            boundaryEvent.setType("BoundaryEvent");
            events.add(boundaryEvent);
        }

        return events;
    }

    private List<Node> getExclusiveGateways() {
        List<Node> overallNodes = new ArrayList<>();
        for (ExclusiveGateway exclusiveGateway : this.fragment.getXorGateways()) {
            Node node = new Node();
            node.setType("ExclusiveGateway");
            node.setText(exclusiveGateway.getName());
            node.setId(exclusiveGateway.getId());
            node.setGlobal(false);
            overallNodes.add(node);
        }
        return overallNodes;
    }

    private List<Node> getDataObjectNodes() {
        List<Node> dataObjectNodes = new ArrayList<>();
        for (DataObjectReference dataObjectReference : this.fragment.getDataObjectReferences()) {
            Node node = new Node();
            // Complete name for now is a String name \n [state]
            String completeName = dataObjectReference.getName();
            String[] splittedName = completeName.split("\\s+");
            node.setState(splittedName[1].substring(1, splittedName[1].length() - 1));
            node.setText(splittedName[0]);
            node.setGlobal(false);
            node.setId(dataObjectReference.getId());
            node.setType("DataObject");
            dataObjectNodes.add(node);
        }
        return dataObjectNodes;
    }

    private List<Node> getTaskNodes() {
        List<Node> overallNodes = new ArrayList<>();
        for (Task task : this.fragment.getTasks()) {
            Node node = new Node();
            node.setType("Task");
            node.setText(task.getName());
            node.setId(task.getId());
            node.setGlobal(false);
            overallNodes.add(node);
        }
        return overallNodes;
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

}
