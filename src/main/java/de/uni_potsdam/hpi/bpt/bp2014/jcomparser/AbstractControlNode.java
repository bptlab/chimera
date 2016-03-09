package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

/**
 * Possible control nodes are gateways, activities, webtasks and events. Each of them
 * get saved in the ControlNode table and can be part of control flow.
 */
public abstract class AbstractControlNode {


    protected int databaseId;

    protected int fragmentId;

    /**
     * Saves the control node to the database and returns it's database id.
     * @return database id which was created by autoincrement.
     */
    public abstract int save();

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public abstract String getId();
}
