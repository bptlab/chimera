package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataAttributeInstance;

/**
 * Created by jaspar.mang on 19.03.15.
 */
public class DataAttributeInstance {
    final int dataAttributeInstance_id;
    final int attribute_id;
    final int dataObjectInstance_id;
    final DataObjectInstance dataObjectInstance;
    Object value;

    DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();

    public DataAttributeInstance(int attribute_id, int dataObjectInstance_id, DataObjectInstance dataObjectInstance){
        this.attribute_id = attribute_id;
        this.dataObjectInstance_id = dataObjectInstance_id;
        this.dataObjectInstance = dataObjectInstance;

        if (dbDataAttributeInstance.existDataAttributeInstance(attribute_id, dataObjectInstance_id)) {
            //creates an existing Attribute Instance using the database information
            this.dataAttributeInstance_id = dbDataAttributeInstance.getDataAttributeInstanceID(attribute_id, dataObjectInstance_id);
        } else {
            //creates a new Attribute Instance also in database
            this.dataAttributeInstance_id = dbDataAttributeInstance.createNewDataAttributeInstance(attribute_id, "", dataObjectInstance_id);
        }
    }


}
