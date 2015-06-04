package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;


public class EmailTaskExecutionBehavior extends TaskExecutionBehavior {
    static Logger log = Logger.getLogger(EmailTaskExecutionBehavior.class.getName());
    private final int controlNode_id;
    private final DbEmailConfiguration emailConfiguration = new DbEmailConfiguration();
    private int port;
    private String serverAddress;
    private String receiverMail;
    private String sendMail;
    private String subject;
    private String message;


    /**
     * Initializes and creates an EmailTaskExecutionBehavior.
     *
     * @param activityInstance_id This is an id for an activity instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param controlNodeInstance This is an instance from the class ControlNodeInstance.
     */
    public EmailTaskExecutionBehavior(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        super(activityInstance_id, scenarioInstance, controlNodeInstance);
        controlNode_id = controlNodeInstance.controlNode_id;
    }

    @Override
    public void execute() {
        this.setValues();
        this.sendMail();
        this.setCanTerminate(true);
        this.setCanTerminate(true);
    }

    /**
     * Sets the attributes for the e mail reading the information from database.
     */
    private void setValues() {
        port = 587;
        serverAddress = "exchange.framsteg.org";
        receiverMail = emailConfiguration.getReceiverEmailAddress(controlNode_id);
        sendMail = emailConfiguration.getSendEmailAddress(controlNode_id);
        subject = emailConfiguration.getSubject(controlNode_id);
        message = emailConfiguration.getMessage(controlNode_id);
        this.setDataAttributes();
    }

    private void setDataAttributes() {
        for (DataAttributeInstance dataAttributeInstance : scenarioInstance.getDataAttributeInstances().values()) {
            message = message.replace(
                    "#" + (dataAttributeInstance.getDataObjectInstance()).getName()
                            + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
            subject = subject.replace(
                    "#" + (dataAttributeInstance.getDataObjectInstance()).getName()
                            + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
            receiverMail = receiverMail.replace(
                    "#" + (dataAttributeInstance.getDataObjectInstance()).getName()
                            + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
        }
        DbState dbState = new DbState();
        for (DataObjectInstance dataObjectInstance : scenarioInstance.getDataObjectInstances()) {
            message = message.replace(
                    "$" + dataObjectInstance.getName(), dbState.getStateName(dataObjectInstance.getState_id()));
        }
    }

    /**
     * Sends an e mail.
     */
    private void sendMail() {
        Email email = new SimpleEmail();
        email.setHostName(serverAddress);
        email.setSmtpPort(port);
        email.setAuthentication("bp2014w01@framsteg.org", "UjB9T8kAS8H9g4YC1f8U");
        try {
            email.setFrom(sendMail);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(receiverMail);
            email.send();
        } catch (EmailException e) {
            log.error("Error by sending e-Mail:", e);
        }
    }
}
