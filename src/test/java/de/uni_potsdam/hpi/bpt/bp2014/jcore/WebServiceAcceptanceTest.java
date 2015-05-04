package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by jaspar.mang on 04.05.15.
 */
public class WebServiceAcceptanceTest extends JerseyTest{

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.class);
    }

    @Test
    public void testGet(){
        System.out.println("\n ------------------ test Scenario 156 ------------------\n");
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(156);
        int activity1 = 524;
        int activity2 = 184;
        int activity3 = 191;
        int activity4 = 193;
        int activity5 = 187;
        int activity6 = 189;

        System.out.println("Start Scenario 156");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        int activity1instance_id = executionService.getScenarioInstance(scenarioInstance).getRunningControlNodeInstances().getFirst().getControlNodeInstance_id();
        executionService.setDataAttributeValues(scenarioInstance, activity1instance_id, new HashMap<Integer, String>());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        Collection<DataAttributeInstance> dataAttributeInstances = executionService.getScenarioInstance(scenarioInstance).getDataAttributeInstances().values();
        String value12 = "";
        String value13 = "";
        for (DataAttributeInstance dataAttributeInstance : dataAttributeInstances){
            if (dataAttributeInstance.getDataAttribute_id() == 12){
                value12 = dataAttributeInstance.getValue().toString();
            }else if (dataAttributeInstance.getDataAttribute_id() == 13){
                value13 = dataAttributeInstance.getValue().toString();
            }
        }
        assertEquals("[9281]", value12);
        assertEquals("Schmutzgrad pruefen", value13);
    }
}
