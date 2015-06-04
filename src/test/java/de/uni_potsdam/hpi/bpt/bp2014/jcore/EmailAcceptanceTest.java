package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
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


/**
 * Test the function of the email tasks.
 */
public class EmailAcceptanceTest extends AbstractDatabaseDependentTest {
    /**
     * Receiver of the emails.
     */
    String receiver = "bp2014w1@byom.de";

    @Before
    public void setUp() {
        //clear Mock JavaMail box
        Mailbox.clearAll();
    }

    /**
     * Email Test Scenario 142.
     */
    @Test
    public void testScenario142() throws MessagingException, IOException, EmailException {
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(142);
        int activity1 = 363;
        System.out.println("Start Scenario 142");

        //check for new Email
        List<Message> inbox = Mailbox.get(receiver);
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
        inbox = Mailbox.get(receiver);
        assertTrue(inbox.size() == 2);
        assertEquals("Test", inbox.get(0).getSubject());
        assertEquals("Test Message", inbox.get(0).getContent());
        assertEquals("Test", inbox.get(1).getSubject());
        assertEquals("Test Message", inbox.get(1).getContent());
    }

    /**
     * Email Test Scenario 141.
     *
     * @throws MessagingException
     * @throws IOException
     * @throws EmailException
     */
    @Test
    public void testScenario141() throws MessagingException, IOException, EmailException {
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(141);
        int activity1 = 358;
        System.out.println("Start Scenario 142");

        //check for new Email
        List<Message> inbox = Mailbox.get(receiver);
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
        inbox = Mailbox.get(receiver);
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

    /**
     * Email Test Scenario 145, XOR e-mail test.
     *
     * @throws MessagingException
     * @throws IOException
     * @throws EmailException
     */
    @Test
    public void testScenario145() throws MessagingException, IOException, EmailException {
        ExecutionService executionService = new ExecutionService();
        int scenarioID = 145;
        int scenarioInstance = executionService.startNewScenarioInstance(scenarioID);
        int activity1 = 389;
        int activity2 = 396;
        int activity3 = 399;
        int activity4 = 407;
        int activity5 = 393;
        int activity6 = 397;
        System.out.println("Start Scenario 145");

        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1, activity2, activity3, activity4}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 2
        System.out.println("do activity " + activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity1, activity3, activity4}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity1, activity3, activity4, activity5, activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity5, activity6, activity3, activity4}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity5, activity6, activity3, activity4}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity6
        System.out.println("do activity " + activity6);
        executionService.beginActivity(scenarioInstance, activity6);
        //assertArrayEquals(new Integer[]{activity3, activity4}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        //executionService.terminateActivity(scenarioInstance, activity6);
        assertArrayEquals(new Integer[]{activity3, activity4, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //check for new Email
        List<Message> inbox = Mailbox.get(receiver);
        assertTrue(inbox.size() == 1);
        assertEquals("Test", inbox.get(0).getSubject());
        assertEquals("Test Message", inbox.get(0).getContent());
    }

    /**
     * Email Test Scenario 146, XOR e-mail test.
     *
     * @throws MessagingException
     * @throws IOException
     * @throws EmailException
     */
    @Test
    public void testScenario146() throws MessagingException, IOException, EmailException {
        ExecutionService executionService = new ExecutionService();
        int scenarioID = 146;
        int scenarioInstance = executionService.startNewScenarioInstance(scenarioID);
        int activity1 = 415;
        int activity2 = 421;
        int activity3 = 416;
        int activity4 = 419;

        System.out.println("Start Scenario 145");

        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity2, activity3, activity4}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);

        //do activity 3
        System.out.println("do activity " + activity3);
        executionService.beginActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{activity4}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{activity4}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //check for new Email
        List<Message> inbox = Mailbox.get(receiver);
        assertTrue(inbox.size() == 1);
        assertEquals("Test", inbox.get(0).getSubject());
        assertEquals("Test Message", inbox.get(0).getContent());

        //do activity 4
        System.out.println("do activity " + activity4);
        executionService.beginActivity(scenarioInstance, activity4);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity4);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);

    }

    /**
     * Email Test Scenario 151.
     *
     * @throws MessagingException
     * @throws IOException
     * @throws EmailException
     */
    @Test
    public void testScenario151() throws MessagingException, IOException, EmailException {
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(151);
        int activity1 = 490;
        System.out.println("Start Scenario 151");


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
        List<Message> inbox = Mailbox.get(receiver);
        assertTrue(inbox.size() == 1);
        assertEquals("Test", inbox.get(0).getSubject());
        assertEquals("Lieber Kunde Stephan Karphen, Sie bekommen am 2015 12500 ueberwiesen. #Test lol test", inbox.get(0).getContent());

    }
}
