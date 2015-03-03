package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Created by jaspar.mang on 03.03.15.
 */

import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;


public class EmailAcceptanceTest {

    @Before
    public void setUp() {
        //clear Mock JavaMail box
        Mailbox.clearAll();

    }

    //Email Test Scenario 142
    @Test
    public void testScenario142() throws MessagingException, IOException, EmailException {
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(142);
        int activity1 = 363;
        System.out.println("Start Scenario 142");

        //check for new Email
        List<Message> inbox = Mailbox.get("TestReceiver@server.de");
        assertTrue(inbox.size() == 1);
        assertEquals("Test", inbox.get(0).getSubject());
        assertEquals("Test Message", inbox.get(0).getContent());

        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //check for new Email
        inbox = Mailbox.get("TestReceiver@server.de");
        assertTrue(inbox.size() == 2);
        assertEquals("Test", inbox.get(0).getSubject());
        assertEquals("Test Message", inbox.get(0).getContent());
        assertEquals("Test", inbox.get(1).getSubject());
        assertEquals("Test Message", inbox.get(1).getContent());
    }

    //Email Test Scenario 141
    @Test
    public void testScenario141() throws MessagingException, IOException, EmailException {
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(141);
        int activity1 = 358;
        System.out.println("Start Scenario 142");

        //check for new Email
        List<Message> inbox = Mailbox.get("TestReceiver@server.de");
        assertTrue(inbox.size() == 2);
        assertEquals("Test", inbox.get(0).getSubject());
        assertEquals("Test Message", inbox.get(0).getContent());
        assertEquals("Test", inbox.get(1).getSubject());
        assertEquals("Test Message", inbox.get(1).getContent());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //check for new Email
        inbox = Mailbox.get("TestReceiver@server.de");
        assertTrue(inbox.size() == 4);
        assertEquals("Test", inbox.get(0).getSubject());
        assertEquals("Test Message", inbox.get(0).getContent());
        assertEquals("Test", inbox.get(1).getSubject());
        assertEquals("Test Message", inbox.get(1).getContent());
        assertEquals("Test", inbox.get(2).getSubject());
        assertEquals("Test Message", inbox.get(2).getContent());
        assertEquals("Test", inbox.get(3).getSubject());
        assertEquals("Test Message", inbox.get(3).getContent());
    }
}
