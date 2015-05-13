package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataAttributeInstance;
/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

/**
 *
 */
public class DataAttributeInstance {
    final int dataAttributeInstance_id;
    final int dataAttribute_id;
    final int dataObjectInstance_id;
    final DataObjectInstance dataObjectInstance;
    final String type;
    final String name;
    Object value;
    DbDataAttributeInstance dbDataAttributeInstance = new DbDataAttributeInstance();

    /**
     * @param dataAttribute_id
     * @param dataObjectInstance_id
     * @param dataObjectInstance
     */
    public DataAttributeInstance(int dataAttribute_id, int dataObjectInstance_id, DataObjectInstance dataObjectInstance) {
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

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    // ****************************************** Getter *********************************//

    /**
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of the data attribute instance. It get also written in the database.
     *
     * @param value to set.
     */
    public void setValue(Object value) {
        this.value = value;
        dbDataAttributeInstance.setValue(dataAttributeInstance_id, value);
    }

    /**
     * @return
     */
    public int getDataAttributeInstance_id() {
        return dataAttributeInstance_id;
    }

    /**
     * @return
     */
    public int getDataAttribute_id() {
        return dataAttribute_id;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public DataObjectInstance getDataObjectInstance() {
        return dataObjectInstance;
    }
}
