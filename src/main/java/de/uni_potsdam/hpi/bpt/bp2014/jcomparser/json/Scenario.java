package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

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
    private String scenarioId;
    private int versionNumber;

    /**
     *
     * @param element an element.
     */
    public void initializeInstanceFromJson(final String element) {
        try {
            JSONObject scenarioJson = new JSONObject(element);
            this.scenarioName = scenarioJson.getString("name");
            this.scenarioId = scenarioJson.getString("_id");
            this.versionNumber = scenarioJson.getInt("revision");
            int scenarioDatabaseId = saveScenario();

            JSONObject domainModelJson = scenarioJson.getJSONObject("domainmodel");
            DomainModel domainModel = createAndInitializeDomainModel(domainModelJson);
            domainModel.setScenarioID(scenarioDatabaseId);
            List<Fragment> fragments = generateFragmentList(scenarioJson);
            associateStatesWithDataClasses(fragments, getNameToDataclass(domainModel));

            domainModel.save();

            List<DataObject> dataObjects = extractDataObjects(fragments, domainModel);
            for (DataObject dataObject : dataObjects) {
                dataObject.setScenarioId(scenarioDatabaseId);
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
                this.scenarioName, this.scenarioId, this.versionNumber);

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
                    this.scenarioId,
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
    /**
     -        * Extracts the TerminationCondition from the XML.
     -        * Corresponding values will be saved to the corresponding fields.
     -        */

    /*
    private void setTerminationCondition() {
        this.terminationCondition = new LinkedList<>();
        JSONArray terminationConditions = scenarioJson.getJSONArray("termination_conditions");
        for (int i = 0; i < terminationConditions.length(); i++) {
            Map<DataObject, String> setMap = new HashMap<>();
            JSONObject terminationCondition = terminationConditions.getJSONObject(i);
            Iterator keys = terminationCondition.keys();
            while (keys.hasNext()) {
                String dataObject = keys.next().toString();
                String state = terminationCondition.getString(dataObject);
                setMap.put(this.dataObjects.get(dataObject), state);
            }
            this.terminationCondition.add(setMap);
        }
    }
    */

}
