package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbAttributeInstance;

/**
 * Created by jaspar.mang on 19.03.15.
 */
public class AttributeInstance {
    final int attributeInstance_id;
    final int attribute_id;
    final int dataObjectInstance_id;
    Object value;

    DbAttributeInstance dbAttributeInstance = new DbAttributeInstance();

    public AttributeInstance(int attribute_id, int dataObjectInstance_id){
        this.attribute_id = attribute_id;
        this.dataObjectInstance_id = dataObjectInstance_id;
        this.attributeInstance_id = dbAttributeInstance.createNewAttributeInstance(attribute_id, "", dataObjectInstance_id);
    }


}
