package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.FragmentInserter;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation.FragmentValidator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.JAXBException;
import java.util.*;

/**
 *
 */
public class ScenarioData {
    private static Logger logger = Logger.getLogger(ScenarioData.class.getName());
    private final JSONObject scenarioJson;

    private String scenarioName;
    private String scenarioEditorId;
    private int versionNumber;

    List<Fragment> fragments;
    private List<TerminationCondition> terminationConditions;
    private List<DataObject> dataObjects;
    private DomainModel domainModel;

    private int scenarioDbId;

    public ScenarioData(final String element) throws JAXBException {
        try {
            this.scenarioJson = new JSONObject(element);
            this.scenarioName = scenarioJson.getString("name");
            this.scenarioEditorId = scenarioJson.getString("_id");
            this.versionNumber = scenarioJson.getInt("revision");

            JSONObject domainModelJson = scenarioJson.getJSONObject("domainmodel");
            this.domainModel = new DomainModel(domainModelJson.toString());
            this.fragments = generateFragmentList(scenarioJson, domainModel);

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


        domainModel.save();

        for (DataObject dataObject : this.dataObjects) {
            dataObject.setScenarioId(this.scenarioDbId);
            dataObject.save();
        }


        FragmentInserter inserter = new FragmentInserter();
        for (Fragment fragment : fragments) {
            fragment.setScenarioId(this.scenarioDbId);
            inserter.save(fragment, domainModel);
        }

        setTerminationCondition(scenarioJson);
        terminationConditions.forEach(TerminationCondition::save);

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
        for (Fragment fragment : fragments) {
            extractDataObjectsFromFragment(fragment, nameToDataObject, model);
        }
        return new ArrayList<>(nameToDataObject.values());
    }

    private Map<String, DataClass> extractNameToDataclass(DomainModel model) {
        Map<String, DataClass> nameToDataClass = new HashMap<>();
        for (DataClass dataClass : model.getDataClasses()) {
            nameToDataClass.put(dataClass.getDataClassName(), dataClass);
        }
        return nameToDataClass;
    }

    private void extractDataObjectsFromFragment(
            Fragment fragment, Map<String, DataObject> nameToDataObject,
            DomainModel model) {
        Map<String, DataClass> nameToDataClass = extractNameToDataclass(model);
        List<DataNode> dataNodes = fragment.getDataNodes();
        for (DataNode node : dataNodes) {
            if (!nameToDataClass.containsKey(node.getName())) {
                throw new IllegalArgumentException(String.format(
                        "Data node %s references invalid dataclass", node.getName()));
            }

            if (!nameToDataObject.containsKey(node.getName())) {

                DataClass belongingDataClass = nameToDataClass.get(node.getName());
                DataObject dataObject = new DataObject(belongingDataClass);
                nameToDataObject.put(node.getName(), dataObject);
            }
            DataObject dataObject = nameToDataObject.get(node.getName());
            dataObject.addDataNode(node);
        }
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
    private List<Fragment> generateFragmentList(JSONObject scenarioJson, DomainModel domainModel) throws JAXBException {
        JSONArray fragmentStrings = scenarioJson.getJSONArray("fragments");
        List<Fragment> generatedFragments = new ArrayList<>();
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
                jsonTerminationConditions, this.dataObjects, scenarioDbId, stateToDatabaseId);
    }


}
