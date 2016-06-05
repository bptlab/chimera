package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.FragmentInserter;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation.FragmentValidator;
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
    private JSONObject scenarioJson;

    private String scenarioName;
    private String scenarioEditorId;
    private int versionNumber;

    List<Fragment> fragments;
    private List<TerminationCondition> terminationConditions;
    private DomainModel domainModel;

    private List<StartQuery> startQueries;

    private int scenarioDbId;

    /**
     * Only for test purposes do not use in production.
     */
    public ScenarioData() {}

    public ScenarioData(final String element) throws JAXBException {
        try {
            this.scenarioJson = new JSONObject(element);
            this.scenarioName = scenarioJson.getString("name");
            this.scenarioEditorId = scenarioJson.getString("_id");
            this.versionNumber = scenarioJson.getInt("revision");

            JSONObject domainModelJson = scenarioJson.getJSONObject("domainmodel");
            this.domainModel = new DomainModel(domainModelJson.toString());

            if (scenarioJson.has("startconditions")) {
                JSONArray startQueryArray = scenarioJson.getJSONArray("startconditions");
                this.startQueries = StartQuery.parseStartQueries(
                        startQueryArray, domainModel.getDataClasses());
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
        this.scenarioDbId = saveScenario();
        domainModel.setScenarioId(this.scenarioDbId);
        domainModel.save();

        FragmentInserter inserter = new FragmentInserter();
        for (Fragment fragment : fragments) {
            fragment.setScenarioId(this.scenarioDbId);
            inserter.save(fragment, this.domainModel.getDataClasses());
        }

        setTerminationCondition(scenarioJson);
        terminationConditions.forEach(TerminationCondition::save);

        this.startQueries.forEach(x -> x.save(scenarioDbId));
        this.startQueries.forEach(x -> x.register(this.scenarioDbId));

        return this.scenarioDbId;
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
        List<DataNode> dataNodes = fragments.stream().map(Fragment::getDataNodes)
                .flatMap(Collection::stream).collect(Collectors.toList());
        for (DataNode node : dataNodes) {
            if (!nameToDataClass.containsKey(node.getName())) {
                throw new IllegalArgumentException(String.format(
                        "Data node %s references invalid dataclass", node.getName()));
            }
            DataClass dataClass = nameToDataClass.get(node.getName());
            dataClass.addDataNode(node);
        }
    }

    public void associateStatesWithDataClasses(List<Fragment> fragments,
                                                Map<String, DataClass> nameToDataclass) {
        for (Fragment fragment : fragments) {
            List<DataNode> dataNodes = fragment.getDataNodes();
            for (DataNode node : dataNodes) {
                DataClass correspondingDataClass = nameToDataclass.get(node.getName());
                correspondingDataClass.getStates().add(node.getState());
            }
        }
    }

    private int saveScenario() {
        Connector connector = new Connector();
        return connector.insertScenarioIntoDatabase(
                this.scenarioName, this.scenarioEditorId, this.versionNumber);
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
                Fragment fragment = new Fragment(fragmentJson.getString("content"),
                        fragmentJson.getInt("revision"),
                        fragmentJson.getString("name"),
                        fragmentJson.getString("_id")
                );
                FragmentValidator.validateFragment(fragment, domainModel);
                generatedFragments.add(fragment);
            } catch (JAXBException e) {
                logger.error(e);
                logger.error("Fragment could not be added");
                String errorMsg = String.format("Invalid fragment Xml provided for fragment %s",
                        fragmentJson.getString("name"));
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
        this.terminationConditions = TerminationCondition.parseTerminationConditions(
                jsonTerminationConditions, domainModel.getDataClasses(), scenarioDbId, stateToDatabaseId);
    }

    public int getScenarioDbId() {
        return scenarioDbId;
    }


    public List<StartQuery> getStartQueries() {
        return startQueries;
    }

    public DomainModel getDomainModel() {
        return domainModel;
    }
}
