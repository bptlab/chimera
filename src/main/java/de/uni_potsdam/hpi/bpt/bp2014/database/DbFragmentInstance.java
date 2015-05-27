package de.uni_potsdam.hpi.bpt.bp2014.database;


/**
 * This class is the representation of a fragment instance in the database.
 * It provides the functionality to create and terminate a fragment instance as well as checking the existence of a fragment instance.
 */
public class DbFragmentInstance extends DbObject {
    /**
     * This method checks if a fragment is present in the database.
     *
     * @param fragment_id         This is the database ID of a fragment.
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @return true if the fragment exists else false.
     */
    public Boolean existFragment(int fragment_id, int scenarioInstance_id) {
        String sql = "SELECT id FROM fragmentinstance WHERE fragmentinstance.terminated = 0 AND scenarioinstance_id = " + scenarioInstance_id + " AND fragment_id = " + fragment_id;
        return this.executeExistStatement(sql);
    }

    /**
     * This method creates and saves a new fragment instance to the database.
     *
     * @param fragment_id         This is the database ID of a fragment.
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @return -1 if something went wrong else the database ID of the newly created fragment instance.
     */
    public int createNewFragmentInstance(int fragment_id, int scenarioInstance_id) {
        String sql = "INSERT INTO fragmentinstance (fragment_id, scenarioinstance_id) VALUES (" + fragment_id + ", " + scenarioInstance_id + ")";
        return this.executeInsertStatement(sql);
    }

    /**
     * This method gives you the fragment instance ID of a fragment from a specific scenario instance.
     *
     * @param fragment_id         This is the database ID of a fragment.
     * @param scenarioInstance_id This is the database ID of a scenario instance.
     * @return the database ID of the fragment instance if existing else -1.
     */
    public int getFragmentInstanceID(int fragment_id, int scenarioInstance_id) {
        String sql = "SELECT id FROM fragmentinstance WHERE fragmentinstance.terminated = 0 AND scenarioinstance_id = " + scenarioInstance_id + " AND fragment_id = " + fragment_id;
        return this.executeStatementReturnsInt(sql, "id");

    }

    /**
     * This method terminates a fragment instance.
     *
     * @param fragmentInstance_id This is the database ID of a fragment instance.
     */
    public void terminateFragmentInstance(int fragmentInstance_id) {
        String sql = "UPDATE fragmentinstance SET fragmentinstance.terminated = 1 WHERE id = " + fragmentInstance_id;
        this.executeUpdateStatement(sql);
    }

    /**
     * This method returns the ID of the fragment the fragmentinstance belons to.
     *
     * @param fragmentinstance_id ID of the fragmentinstance
     * @return FragmentID.
     */
    public int getFragmentID(int fragmentinstance_id) {
        String sql = "SELECT fragment_id FROM fragmentinstance WHERE id = " + fragmentinstance_id;
        return this.executeStatementReturnsInt(sql, "fragment_id");
    }
}
