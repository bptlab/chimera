package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.FragmentInserter;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 *
 */
public class Scenario {
    private String scenarioName;
    private String scenarioEditorId;
    private int versionNumber;

    private int scenarioDbId;

    private List<TerminationCondition> terminationConditions;

    private List<DataObject> dataObjects;
    private JSONObject scenarioJson;
    private DomainModel domainModel;

    /**
     *
     * @param element an element.
     */
    public void initializeInstanceFromJson(final String element) {
        try {
            this.scenarioJson = new JSONObject(element);
            this.scenarioName = scenarioJson.getString("name");
            this.scenarioEditorId = scenarioJson.getString("_id");
            this.versionNumber = scenarioJson.getInt("revision");
            this.scenarioDbId = saveScenario();

            JSONObject domainModelJson = scenarioJson.getJSONObject("domainmodel");
            domainModel = createAndInitializeDomainModel(domainModelJson);
            domainModel.setScenarioID(this.scenarioDbId);
            List<Fragment> fragments = generateFragmentList(scenarioJson);
            associateStatesWithDataClasses(fragments, getNameToDataclass(domainModel));
            setTerminationCondition();

            domainModel.save();
            saveTerminationConditions();

            this.dataObjects = extractDataObjects(fragments, domainModel);
            for (DataObject dataObject : dataObjects) {
                dataObject.setScenarioId(this.scenarioDbId);
                dataObject.save();
            }

            FragmentInserter inserter = new FragmentInserter();
            for (Fragment fragment : fragments) {
                inserter.save(fragment, domainModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                if (! nameToDataObject.containsKey(node.getName())) {
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
            dataClassNameToDatabaseId.put(
                    dataClass.getDataClassName(), dataClass);
        }
        return dataClassNameToDatabaseId;
    }


    /**
     * Generates a List of Fragments from the .
     */
    private List<Fragment> generateFragmentList(JSONObject scenarioJson) {
        JSONArray fragmentStrings = scenarioJson.getJSONArray("fragments");
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < fragmentStrings.length(); i++) {
            JSONObject fragmentJson = fragmentStrings.getJSONObject(i);
            Fragment fragment = new Fragment(fragmentJson.getString("content"),
                    fragmentJson.getInt("revision"),
                    fragmentJson.getString("name"),
                    this.scenarioDbId,
                    fragmentJson.getString("_id")
            );
            fragments.add(fragment);
        }
        return fragments;
    }

    /**
     * This method takes the JSON and initializes the domainModel.
     *
     * @return the domainModel or null if there was no JSON given.
     */
    private DomainModel createAndInitializeDomainModel(JSONObject domainModelJson) {
        DomainModel model = new DomainModel();
        model.initializeInstanceFromJson(domainModelJson.toString());
        return model;
    }


    public int getScenarioDbId() {
        return scenarioDbId;
    }
    
    /**
     * Extracts the TerminationCondition from the String in the JSON.
     * Finds the DataObject for the given Name.
     */
    private void setTerminationCondition() {
        JSONArray jsonTerminationConditions = scenarioJson.getJSONArray("terminationconditions");

        // iterate over all termination condition sets
        for (int i = 0; i < jsonTerminationConditions.length(); i++) {

            // get dataobjects and states for one termination condition set
            String[] tcStrings = jsonTerminationConditions.getString(i).split(",");

            for(String tcString : tcStrings) {
                String dataClassString = tcString.split("[")[0];
                String stateString = tcString.replaceAll(".*\\[|\\].*", "");

                int dataObjectId = findDataObjectByClass(dataClassString).getDatabaseId();
                int stateId = findStateIdByName(stateString);

                this.terminationConditions.add(new TerminationCondition(
                        stateId,
                        dataObjectId,
                        getScenarioDbId())
                );

            }

        }
    }

    private void saveTerminationConditions() {
        terminationConditions.forEach(TerminationCondition::save);
    }

    /**
     * Find a DataObject by the name of its Data Class.
     * @param dataClassName name of the dataClass
     * @return DataObject that has the given class
     */
    private DataObject findDataObjectByClass(String dataClassName) {
        return this.dataObjects.stream()
                .filter(a -> a.getDataClassName().equals(dataClassName))
                .findFirst().get();
    }

    /**
     * Find the id of a DataNode State by the name.
     * @param stateName name of the state
     * @return id of the state
     */
    private int findStateIdByName(String stateName) {
        DbState dbState = new DbState();
        return dbState.getStateId(stateName);
    }

}
