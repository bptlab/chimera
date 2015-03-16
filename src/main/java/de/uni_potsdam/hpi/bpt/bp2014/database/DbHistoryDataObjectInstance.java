package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.Map;

/**
 * Created by Jan Selke on 16.03.2015.
 */
public class DbHistoryDataObjectInstance extends DbObject {

    public int createEntry(int object_instance_id, int state_id) {
        String sql = "INSERT INTO `historydataobjectinstance` (`scenarioinstance_id`,`dataobjectinstance_id`,`old_state_id`,`old_state_name`,`new_state_id`,`new_state_name`) SELECT (SELECT `scenarioinstance_id` FROM `dataobjectinstance` WHERE `id` = "+object_instance_id+") AS`scenarioinstance_id`, `id`, (SELECT `state_id` FROM `dataobjectinstance` WHERE `id` = "+object_instance_id+") AS `old_state_id`, (SELECT `name` FROM `dataobjectinstance`, `state` WHERE `state`.id = `state_id` AND `dataobjectinstance`.id = "+object_instance_id+") AS `old_state_name`, "+state_id+" AS `new_state_id`, (SELECT `name`from state WHERE `id` = "+state_id+") AS `new_state_name` FROM `dataobjectinstance` WHERE `id` = "+object_instance_id;
        return this.executeInsertStatement(sql);
    }

    public int createNewDataObjectInstanceEntry (int id){
        String sql ="INSERT INTO `historydataobjectinstance` (`scenarioinstance_id`,`dataobjectinstance_id`, `new_state_id`,`new_state_name`) SELECT (SELECT `scenarioinstance_id` FROM `dataobjectinstance` WHERE `id` = "+id+") AS`scenarioinstance_id`, `id`, (SELECT `state_id` FROM `dataobjectinstance` WHERE `id` = "+id+") AS `new_state_id`, (SELECT `name` FROM `dataobjectinstance`, `state` WHERE `state`.id = `state_id` AND `dataobjectinstance`.id = "+id+") AS `new_state_name` FROM `dataobjectinstance` WHERE `id` = "+id;
        return this.executeInsertStatement(sql);
    }

    public Map<Integer, Map<String, Object>> getLogEntriesForScenarioInstance(int scenarioInstanceId){
        String sql = "SELECT * FROM historydataobjectinstance WHERE scenarioinstance_id = "+scenarioInstanceId;
        return this.executeStatementReturnsMapWithMapWithKeys(sql, "scenarioinstance_id", "timestamp","dataobjectinstance_id", "old_state_id", "old_state_name", "new_state_id", "new_state_name");
    }



}
