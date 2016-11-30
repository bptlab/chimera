package de.hpi.bpt.chimera.jcomparser.saving;

import de.hpi.bpt.chimera.jcomparser.jaxb.*;
import de.hpi.bpt.chimera.util.CollectionUtil;
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
	private int versionNumber;
	private String xmlString;
	private FragmentXmlWrapper xmlWrapper;

	public Fragment(String xmlString, int versionNumber, String fragmentName) throws JAXBException {
		this.xmlString = xmlString;
		this.xmlWrapper = buildFragment(xmlString);
		this.fragmentName = fragmentName;
		this.versionNumber = versionNumber;
	}

	public int save() {
		Connector connector = new Connector();
		int fragmentId = connector.insertFragment(fragmentName, scenarioId, versionNumber);
		connector.insertFragmentXml(fragmentId, this.xmlString);
		return fragmentId;
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

		for (AbstractDataControlNode node : this.xmlWrapper.getAllDataControlNodes()) {
			sets.addAll(getOutputSetsForNode(node, idToDataNode));
		}

		return sets;
	}

	public List<OutputSet> getOutputSetsForNode(AbstractDataControlNode node, Map<String, DataNode> idToDataNode) {
		Map<String, List<DataNode>> dataNodeToStates = new HashMap<>();
		if (node.getDataOutputAssociations().isEmpty()) {
			return new ArrayList<>();
		}
		for (DataOutputAssociation assoc : node.getDataOutputAssociations()) {
			DataNode dataNode = idToDataNode.get(assoc.getTargetRef());
			if (!dataNodeToStates.containsKey(dataNode.getName())) {
				dataNodeToStates.put(dataNode.getName(), new ArrayList<>());
			}
			dataNodeToStates.get(dataNode.getName()).add(dataNode);
		}
		List<List<DataNode>> datanodeCombinations = CollectionUtil.computeCartesianProduct(new ArrayList<>(dataNodeToStates.values()));

		return datanodeCombinations.stream().map(combination -> new OutputSet(node, combination)).collect(Collectors.toList());
	}


	/**
	 * @return List of Input sets
	 */
	public List<InputSet> getInputSets() {
		List<InputSet> sets = new ArrayList<>();
		for (AbstractDataControlNode node : this.xmlWrapper.getAllDataControlNodes()) {
			sets.addAll(getInputSetsForNode(node));
		}
		return sets;
	}

	public List<InputSet> getInputSetsForNode(AbstractDataControlNode node) {
		Map<String, DataNode> idToDataNode = this.getDataNodes().stream().collect(Collectors.toMap(x -> x.getId(), x -> x));

		Map<String, List<DataNode>> dataNodeToStates = new HashMap<>();
		for (DataInputAssociation assoc : node.getDataInputAssociations()) {
			DataNode dataNode = idToDataNode.get(assoc.getSourceRef());
			if (!dataNodeToStates.containsKey(dataNode.getName())) {
				dataNodeToStates.put(dataNode.getName(), new ArrayList<>());
			}
			dataNodeToStates.get(dataNode.getName()).add(dataNode);
		}

		if (!dataNodeToStates.isEmpty()) {
			List<List<DataNode>> datanodeCombinations = CollectionUtil.computeCartesianProduct(new ArrayList<>(dataNodeToStates.values()));
			return datanodeCombinations.stream().map(combination -> new InputSet(node, combination)).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * @return Return a list of all control nodes in a fragment
	 */
	public List<AbstractControlNode> getControlNodes() {
		List<AbstractControlNode> nodes = new ArrayList<>();
		nodes.addAll(this.xmlWrapper.getXorGateways());
		nodes.addAll(this.xmlWrapper.getAndGateways());
		nodes.addAll(this.xmlWrapper.getAllActivities());
		nodes.addAll(this.xmlWrapper.getReceiveTasks());
		nodes.addAll(this.xmlWrapper.getSendTasks());
		nodes.addAll(this.xmlWrapper.getIntermediateCatchEvents());
		nodes.addAll(this.xmlWrapper.getIntermediateThrowEvents());
		nodes.addAll(this.xmlWrapper.getBoundaryEvents());
		nodes.addAll(this.xmlWrapper.getEventBasedGateways());
		nodes.add(this.xmlWrapper.getEndEvent());
		nodes.add(this.xmlWrapper.getStartEvent());
		return nodes;
	}

	public List<BoundaryEvent> getBoundaryEventNodes() {
		return this.xmlWrapper.getBoundaryEvents();
	}


	public List<SequenceFlow> getSequenceFlow() {
		return this.xmlWrapper.getSequenceFlow();
	}

	public List<DataNode> getDataNodes() {
		return this.xmlWrapper.getDataNodes();
	}

	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getName() {
		return fragmentName;
	}

	public void setName(String name) {
		this.fragmentName = name;
	}

	public List<Task> getTasks() {
		return this.xmlWrapper.getTasks();
	}

	public List<WebServiceTask> getWebServiceTasks() {
		return this.xmlWrapper.getWebServiceTasks();
	}

	public List<AbstractDataControlNode> getAllActivities() {
		return this.xmlWrapper.getAllActivities();
	}
}
