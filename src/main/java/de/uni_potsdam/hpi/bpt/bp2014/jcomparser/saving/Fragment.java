package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.Olc;
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

        for (Task task : this.fragmentXml.getTasks()) {
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

        for (Task task : this.fragmentXml.getTasks()) {
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
        List<Task> tasks = this.fragmentXml.getTasks();
        Map<String, Task> idToNode = new HashMap<>();
        for (Task task : tasks) {
            idToNode.put(task.getId(), task);
        }

        return idToNode;
    }

    /**
     * This method validates the fragment against the given OLCs. Note that in case there is no OLC
     * for a given DataClass all transitions for this class are considered valid.
     * @param olcs A map of DataClasses (identified by name) to their respective OLCs.
     * @return true if the fragment matches all given OLCs (false if there is a violation)
     */
    public Boolean isOLCValid(Map<String, Olc> olcs) {
        //idea: get a map of DataObjects with possible states for the input / output of each task.
        //test for all transitions whether they fit the OLC (per task)
        //this seems inefficient and complicated ... is there a better way?

        Map<String, DataNode> idToDataNode = new HashMap<>();
        for (DataNode dataNode : this.getDataNodes()) {
            idToDataNode.put(dataNode.getId(), dataNode);
        }

        for (Task task : this.fragmentXml.getTasks()) {
            Map<String, List<String>> sourceDataNodesToStates = new HashMap<>();
            for (DataInputAssociation assoc : task.getDataInputAssociations()) {
                DataNode dataNode =  idToDataNode.get(assoc.getSourceRef());
                if (!sourceDataNodesToStates.containsKey(dataNode.getName())) {
                    sourceDataNodesToStates.put(dataNode.getName(), new ArrayList<>());
                }
                sourceDataNodesToStates.get(dataNode.getName()).add(dataNode.getState());
            }

            Map<String, List<String>> targetDataNodesToStates = new HashMap<>();
            for (DataOutputAssociation assoc : task.getDataOutputAssociations()) {
                DataNode dataNode =  idToDataNode.get(assoc.getTargetRef());
                if (!targetDataNodesToStates.containsKey(dataNode.getName())) {
                    targetDataNodesToStates.put(dataNode.getName(), new ArrayList<>());
                }
                targetDataNodesToStates.get(dataNode.getName()).add(dataNode.getState());
            }

            //iterate over source DataNodes
            for (Map.Entry<String, List<String>> entry : sourceDataNodesToStates.entrySet()) {
                //We need an OLC to validate the DataClass.
                if (!olcs.containsKey(entry.getKey())) continue;
                //now iterate over states
                for (String state : entry.getValue()) {
                    //state doesn't exist in the respective OLC for the DataClass
                    if (!olcs.get(entry.getKey()).nameToOutgoing.containsKey(state)) return false;
                    //only look a DataNodes where there is actually a transition
                    if (targetDataNodesToStates.containsKey(entry.getKey())) {
                        //let's eventually compare the output states :|
                        if (!olcs.get(entry.getKey()).nameToOutgoing.get(state).containsAll(
                                targetDataNodesToStates.get(entry.getKey()))) return false;
                    }
                }
            }
        }

        //since there are no invalid transitions, the fragment matches the OLCs :)
        return true;
    }

    /**
     *
     * @return Return a list of all control nodes in a fragment
     */
    public List<AbstractControlNode> getControlNodes() {
        List<AbstractControlNode> nodes = new ArrayList<>();
        nodes.addAll(this.fragmentXml.getXorGateways());
        nodes.addAll(this.fragmentXml.getTasks());
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
}
