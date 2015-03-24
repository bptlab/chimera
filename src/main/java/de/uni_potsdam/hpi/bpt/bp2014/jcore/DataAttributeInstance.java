package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataAttributeInstance;

/**
 * Created by jaspar.mang on 19.03.15.
 */
public class DataAttributeInstance {
    final int dataAttributeInstance_id;
    final int dataAttribute_id;
    final int dataObjectInstance_id;
    final DataObjectInstance dataObjectInstance;
    Object value;
    final String type;
    final String name;

    DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();

    public DataAttributeInstance(int dataAttribute_id, int dataObjectInstance_id, DataObjectInstance dataObjectInstance){
        this.dataAttribute_id = dataAttribute_id;
        this.dataObjectInstance_id = dataObjectInstance_id;
        this.dataObjectInstance = dataObjectInstance;
        this.type = dbDataAttributeInstance.getType(dataAttribute_id);
        if (dbDataAttributeInstance.existDataAttributeInstance(dataAttribute_id, dataObjectInstance_id)) {
            //creates an existing Attribute Instance using the database information
            this.dataAttributeInstance_id = dbDataAttributeInstance.getDataAttributeInstanceID(dataAttribute_id, dataObjectInstance_id);
        } else {
            //creates a new Attribute Instance also in database
            this.dataAttributeInstance_id = dbDataAttributeInstance.createNewDataAttributeInstance(dataAttribute_id, dataObjectInstance_id);
        }
        this.value = dbDataAttributeInstance.getValue(dataAttributeInstance_id);
        this.name = dbDataAttributeInstance.getName(dataAttribute_id);
    }

    public void setValue(int dataAttributeInstance_id, Object value){
        this.value = value;
        dbDataAttributeInstance.setValue(dataAttributeInstance_id, value);
    }

    //Getter


    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public int getDataAttributeInstance_id() {
        return dataAttributeInstance_id;
    }

    public int getDataAttribute_id() {
        return dataAttribute_id;
    }

    public String getName() {
        return name;
    }

    public DataObjectInstance getDataObjectInstance() {
        return dataObjectInstance;
    }
}
