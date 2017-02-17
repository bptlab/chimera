package de.hpi.bpt.chimera.jcomparser.json;

import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jcomparser.jaxb.DataNode;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcomparser.saving.Fragment;
import de.hpi.bpt.chimera.jcomparser.saving.FragmentInserter;
import de.hpi.bpt.chimera.jcomparser.validation.FragmentValidator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.JAXBException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class ScenarioData {
	private static Logger logger = Logger.getLogger(ScenarioData.class.getName());
	List<Fragment> fragments;
	private JSONObject scenarioJson;
	private String scenarioName;
	private int versionNumber;
	private List<TerminationCondition> terminationConditions;
	private DomainModel domainModel;

	private List<StartQuery> startQueries;

	private int id;

	/**
	 * Only for test purposes do not use in production.
	 */
	public ScenarioData() {
	}

	public ScenarioData(final String element) throws JAXBException {
		try {
			this.scenarioJson = new JSONObject(element);
			this.scenarioName = scenarioJson.getString("name");
			this.versionNumber = scenarioJson.getInt("revision");

			JSONObject domainModelJson = scenarioJson.getJSONObject("domainmodel");
			this.domainModel = new DomainModel(domainModelJson.toString());

			// key 'startconditions' always there, but empty
			if (scenarioJson.has("startconditions")) {
				JSONArray startQueryArray = scenarioJson.getJSONArray("startconditions");
				this.startQueries = StartQuery.parseStartQueries(startQueryArray);
			} else {
				this.startQueries = new ArrayList<>();
			}

			this.fragments = generateFragmentList(scenarioJson, domainModel);

			associateStatesWithDataClasses(fragments, getNameToDataclass(domainModel));
			associateDataNodesWithDataClasses(fragments, domainModel);
		} catch (JSONException e) {
			logger.fatal("Invalid scenario json provided");
			logger.trace(e);
			throw new JSONException("No valid scenario json provided");
		}
	}


	public int save() {
		this.id = saveScenario();
		domainModel.setScenarioId(this.id);
		domainModel.save();

		FragmentInserter inserter = new FragmentInserter();
		for (Fragment fragment : fragments) {
			fragment.setScenarioId(this.id);
			inserter.save(fragment, this.domainModel.getDataClasses());
		}

		setTerminationCondition(scenarioJson);
		terminationConditions.forEach(TerminationCondition::save);

		this.startQueries.forEach(x -> x.save(id, domainModel.getDataClasses()));
		this.startQueries.forEach(x -> x.register(this.id));

		return this.id;
	}

	private Map<String, DataClass> extractNameToDataclass(DomainModel model) {
		Map<String, DataClass> nameToDataClass = new HashMap<>();
		for (DataClass dataClass : model.getDataClasses()) {
			nameToDataClass.put(dataClass.getName(), dataClass);
		}
		return nameToDataClass;
	}

	public void associateDataNodesWithDataClasses(List<Fragment> fragments, DomainModel model) {
		Map<String, DataClass> nameToDataClass = extractNameToDataclass(model);
		List<DataNode> dataNodes = fragments.stream().map(Fragment::getDataNodes).flatMap(Collection::stream).collect(Collectors.toList());
		for (DataNode node : dataNodes) {
			if (!nameToDataClass.containsKey(node.getName())) {
				throw new IllegalArgumentException(String.format("Data node %s references invalid dataclass", node.getName()));
			}
			DataClass dataClass = nameToDataClass.get(node.getName());
			dataClass.addDataNode(node);
		}
	}

	public void associateStatesWithDataClasses(List<Fragment> fragments, Map<String, DataClass> nameToDataclass) {
		for (Fragment fragment : fragments) {
			List<DataNode> dataNodes = fragment.getDataNodes();
			for (DataNode node : dataNodes) {
				DataClass correspondingDataClass = nameToDataclass.get(node.getName());
				if (!correspondingDataClass.getStates().contains(node.getState())) {
					correspondingDataClass.getStates().add(node.getState());
				}
			}
		}
	}

	private int saveScenario() {
		Connector connector = new Connector();
		return connector.insertScenario(this.scenarioName, this.versionNumber);
	}

	private Map<String, DataClass> getNameToDataclass(DomainModel domainModel) {
		Map<String, DataClass> dataClassNameToDatabaseId = new HashMap<>();
		for (DataClass dataClass : domainModel.getDataClasses()) {
			dataClassNameToDatabaseId.put(dataClass.getName(), dataClass);
		}
		return dataClassNameToDatabaseId;
	}


	/**
	 * Generates a List of Fragments from the Json object.
	 */
	public List<Fragment> generateFragmentList(JSONObject scenarioJson, DomainModel domainModel) throws JAXBException {
		JSONArray fragmentStrings = scenarioJson.getJSONArray("fragments");
		List<Fragment> generatedFragments = new ArrayList<>();
		if (fragmentStrings.length() == 0) {
			throw new IllegalArgumentException("No fragments specified");
		}
		for (int i = 0; i < fragmentStrings.length(); i++) {
			JSONObject fragmentJson = fragmentStrings.getJSONObject(i);
			try {
				Fragment fragment = new Fragment(fragmentJson.getString("content"), fragmentJson.getInt("revision"), fragmentJson.getString("name"));
				FragmentValidator.validateFragment(fragment, domainModel);
				generatedFragments.add(fragment);
			} catch (JAXBException e) {
				logger.error(e);
				logger.error("Fragment could not be added");
				String errorMsg = String.format("Invalid fragment Xml provided for fragment %s", fragmentJson.getString("name"));
				throw new JAXBException(errorMsg);
			}
		}
		return generatedFragments;
	}

	/**
	 * Extracts the TerminationCondition from the String in the JSON.
	 * Finds the DataObject for the given Name.
	 */
	private void setTerminationCondition(JSONObject scenarioJson) {
		JSONArray jsonTerminationConditions = scenarioJson.getJSONArray("terminationconditions");
		Map<String, Integer> stateToDatabaseId = new DbState().getStateToIdMap();
		this.terminationConditions = TerminationCondition.parseTerminationConditions(jsonTerminationConditions, domainModel.getDataClasses(), id, stateToDatabaseId);
	}

	public int getId() {
		return id;
	}


	public List<StartQuery> getStartQueries() {
		return startQueries;
	}

	public DomainModel getDomainModel() {
		return domainModel;
	}
}
