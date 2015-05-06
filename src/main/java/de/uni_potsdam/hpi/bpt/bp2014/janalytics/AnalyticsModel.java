package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

public class AnalyticsModel {

    public boolean exampleAlgorithm1(int scenarioInstance_id) {
        
            String sql = "INSERT INTO `historydataattributeinstance` (`scenarioinstance_id`,`dataattributeinstance_id`,`newvalue`) " +
                    "SELECT (SELECT `scenarioinstance_id` FROM `dataattributeinstance`, dataobjectinstance WHERE dataobjectinstance.id = dataattributeinstance.dataobjectinstance_id AND dataattributeinstance.id = "+data_attribute_instance_id+") AS `scenarioinstance_id`," +
                    " `id` AS dataattributeinstance_id, " +
                    "(SELECT `value` FROM `dataattributeinstance` WHERE `id` = "+data_attribute_instance_id+") AS `newvalue` " +
                    "FROM `dataattributeinstance` WHERE `id` = "+data_attribute_instance_id;
            return this.executeInsertStatement(sql);
        }
    }

}
