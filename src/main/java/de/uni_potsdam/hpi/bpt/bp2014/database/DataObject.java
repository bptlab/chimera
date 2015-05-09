package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 * This class is the representation of a dataObject.
 */
public class DataObject {
    private int id;
    private int stateID;

    /**
     * Initializes the DataObject.
     *
     * @param id      This is the database ID of a data object.
     * @param stateID This is the database ID of a state.
     */
    public DataObject(int id, int stateID) {
        this.id = id;
        this.stateID = stateID;
    }

    /**
     * Getter.
     */

    public int getId() {
        return id;
    }

    public int getStateID() {
        return stateID;
    }


}
