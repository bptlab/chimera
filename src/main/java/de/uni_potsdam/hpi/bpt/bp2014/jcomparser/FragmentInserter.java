package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.SequenceFlow;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DomainModel;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.InputSet;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.OutputSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for maintaining the correct order to insert elements
 * from the fragment into the database. This is important because some control
 * structures operate on the database Ids of elements which have to be inserted before
 * them.
 */
public class FragmentInserter {

    /**
     * Method which saves a fragment to the
     * @param fragment Fragment which should be inserted into the database.
     * @param domainModel The data classes of the
     * @return return database Id of the inserted fragment
     */

    public int save(Fragment fragment, DomainModel domainModel) {
        int fragmentDatabaseId = fragment.save();
        Map<String, Integer> nodeToDatabaseId = saveControlNodes(fragmentDatabaseId, fragment);
        saveControlFlow(fragment, nodeToDatabaseId);
        saveDataFlow(fragment);
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

        return nodeToDatabaseId;
    }

    /**
     * Saves the input and output sets to the database.
     */
    private void saveSets(Fragment fragment) {
        for (InputSet set : fragment.getInputSets()) {
            set.save();
        }

        for (OutputSet set : fragment.getOutputSets()) {
            set.save();
        }
    }


}
