package de.hpi.bpt.chimera.jcomparser.saving;

import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcomparser.jaxb.SequenceFlow;
import de.hpi.bpt.chimera.jcomparser.json.DataClass;
import de.hpi.bpt.chimera.jcomparser.jaxb.BoundaryEvent;
import de.hpi.bpt.chimera.jcomparser.jaxb.DataNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is responsible for maintaining the correct order to insert elements
 * from the fragment into the database. This is important because some control
 * structures operate on the database Ids of elements which have to be inserted before
 * them.
 */
public class FragmentInserter {

    /**
     * Method which saves a fragment to the database.
     * @param fragment Fragment which should be inserted into the database.
     * @return return database Id of the inserted fragment
     */

    public int save(Fragment fragment, List<DataClass> dataClasses) {
        int fragmentDatabaseId = fragment.save();
        Map<String, Integer> nodeToDatabaseId = saveControlNodes(fragmentDatabaseId, fragment);
        saveControlFlow(fragment, nodeToDatabaseId);
        saveDataFlow(fragment);
        // this has to be saved after saving the data flow
        // because we need the data sets to link data nodes to their preceding control nodes
        savePathMapping(fragment, dataClasses);
        return fragmentDatabaseId;
    }

    private void saveControlFlow(Fragment fragment, Map<String, Integer> nodeToDatabaseId) {
        for (SequenceFlow flow : fragment.getSequenceFlow()) {
            flow.save(nodeToDatabaseId);
        }
    }

    private void saveDataFlow(Fragment fragment) {
        saveSets(fragment);
    }


    private Map<String, Integer> saveControlNodes(int fragmentId, Fragment fragment) {
        Map<String, Integer> nodeToDatabaseId = new HashMap<>();
        for (AbstractControlNode node : fragment.getControlNodes()) {
            node.setFragmentId(fragmentId);
            int databaseId = node.save();
            nodeToDatabaseId.put(node.getId(), databaseId);
        }
        saveBoundaryEventRelations(nodeToDatabaseId, fragment, fragmentId);
        return nodeToDatabaseId;
    }

    private void saveBoundaryEventRelations(Map<String, Integer> savedControlNodes,
            Fragment fragment, int fragmentId) {
        for (BoundaryEvent event : fragment.getBoundaryEventNodes()) {
            event.setFragmentId(fragmentId);
            event.saveConnectionToActivity(savedControlNodes);
        }
    }


    /**
     * Saves the input and output sets to the database.
     */
    private void saveSets(Fragment fragment) {
        fragment.getInputSets().forEach(InputSet::save);
        fragment.getOutputSets().forEach(OutputSet::save);
    }

    private void savePathMapping(Fragment fragment, List<DataClass> dataClasses) {
        for (DataNode dataNode : fragment.getDataNodes()) {
            int dataClassId = new DbDataNode()
                    .getDataClassIdForDataNode(dataNode.getDatabaseId());
            Optional<DataClass> dataObject = findDataObjectById(dataClasses, dataClassId);
            if (dataObject.isPresent()) {
                dataNode.insertPathMappingIntoDatabase(dataObject.get());
            }
        }
    }

    private Optional<DataClass> findDataObjectById(List<DataClass> dataObjects, int dataObjectId) {
        return dataObjects.stream().filter(x -> x.getDatabaseId() == dataObjectId).findFirst();
    }
}
