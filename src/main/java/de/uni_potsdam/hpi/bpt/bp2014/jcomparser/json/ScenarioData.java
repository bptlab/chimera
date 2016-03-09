package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.FragmentInserter;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataObject;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 *
 */
public class ScenarioData {
    private static Logger logger = Logger.getLogger(ScenarioData.class.getName());

    private String scenarioName;
    private String scenarioEditorId;
    private int versionNumber;

    List<Fragment> fragments;
    private List<TerminationCondition> terminationConditions;
    private List<DataObject> dataObjects;
    private JSONObject scenarioJson;
    private DomainModel domainModel;

    private int scenarioDbId;

    public ScenarioData(final String element) {
        try {
            JSONObject scenarioJson = new JSONObject(element);
            this.scenarioName = scenarioJson.getString("name");
            this.scenarioEditorId = scenarioJson.getString("_id");
            this.versionNumber = scenarioJson.getInt("revision");

            JSONObject domainModelJson = scenarioJson.getJSONObject("domainmodel");
            this.domainModel = new DomainModel(domainModelJson.toString());
            this.fragments = generateFragmentList(scenarioJson);

            associateStatesWithDataClasses(fragments, getNameToDataclass(domainModel));
            this.dataObjects = extractDataObjects(fragments, domainModel);
        } catch (JSONException e) {
            logger.fatal("Invalid scenario json provided");
            logger.trace(e);
            throw new JSONException("No valid scenario json provided");
        }
    }

    public int save() {
        this.scenarioDbId = saveScenario();
        domainModel.setScenarioID(this.scenarioDbId);

        for (DataObject dataObject : this.dataObjects) {
            dataObject.setScenarioId(this.scenarioDbId);
            dataObject.save();
        }

        domainModel.save();

        FragmentInserter inserter = new FragmentInserter();
        for (Fragment fragment : fragments) {
            fragment.setScenarioId(this.scenarioDbId);
            inserter.save(fragment, domainModel);
        }
        return this.scenarioDbId;
    }


    /**
     * Looks if a DataObject for the given DataNodes in the fragment already exists.
     * If so, adds the DataNodes to the DataObject.
     * If not, creates a new DataObject and adds the DataNodes afterwards.
     * @param fragments List of all Fragments
     * @param model the DomainModel containing all DataClasses
     * @return List of all DataObjects
     */
    private List<DataObject> extractDataObjects(List<Fragment> fragments, DomainModel model) {
        Map<String, DataObject> nameToDataObject = new HashMap<>();

        Map<String, DataClass> nameToDataClass = new HashMap<>();
        for (DataClass dataClass : model.getDataClasses()) {
            nameToDataClass.put(dataClass.getDataClassName(), dataClass);
        }

        for (Fragment fragment : fragments) {
            List<DataNode> dataNodes = fragment.getDataNodes();
            for (DataNode node : dataNodes) {
                if (!nameToDataObject.containsKey(node.getName())) {
                    DataClass belongingDataClass = nameToDataClass.get(node.getName());
                    DataObject dataObject = new DataObject(belongingDataClass);
                    nameToDataObject.put(node.getName(), dataObject);
                }
                DataObject dataObject = nameToDataObject.get(node.getName());
                dataObject.addDataNode(node);
            }
        }
        return new ArrayList<>(nameToDataObject.values());
    }

    private void associateStatesWithDataClasses(List<Fragment> fragments,
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
            dataClassNameToDatabaseId.put(dataClass.getDataClassName(), dataClass);
        }
        return dataClassNameToDatabaseId;
    }


    /**
     * Generates a List of Fragments from the Json object.
     */
    private List<Fragment> generateFragmentList(JSONObject scenarioJson) {
        JSONArray fragmentStrings = scenarioJson.getJSONArray("fragments");
        List<Fragment> generatedFragments = new ArrayList<>();
        for (int i = 0; i < fragmentStrings.length(); i++) {
            JSONObject fragmentJson = fragmentStrings.getJSONObject(i);
            Fragment fragment = new Fragment(fragmentJson.getString("content"),
                    fragmentJson.getInt("revision"),
                    fragmentJson.getString("name"),
                    fragmentJson.getString("_id")
            );
            generatedFragments.add(fragment);
        }
        return generatedFragments;
    }

    /**
     * Extracts the TerminationCondition from the String in the JSON.
     * Finds the DataObject for the given Name.
     */
    private void setTerminationCondition() {
        JSONArray jsonTerminationConditions = scenarioJson.getJSONArray("terminationconditions");
        Map<String, Integer> stateToDatabaseId = new DbState().getStateToIdMap();
        this.terminationConditions = TerminationCondition.parseTerminationConditions(
                jsonTerminationConditions, this.dataObjects, scenarioDbId, stateToDatabaseId);
    }

    private void saveTerminationConditions() {
        terminationConditions.forEach(TerminationCondition::save);
    }
}
