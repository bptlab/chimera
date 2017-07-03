package de.hpi.bpt.reschedule;

import bpt.chimera.scripttasklibrary.IChimeraContext;
import bpt.chimera.scripttasklibrary.IChimeraDelegate;
import de.hpi.bpt.chimera.database.DbEmailConfiguration;
import de.hpi.bpt.chimera.util.PropertyLoader;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class InformParticipants implements IChimeraDelegate{
    private final DbEmailConfiguration emailConfiguration = new DbEmailConfiguration();
    private String hostName;
    private int port;
    private String receiverMail;
    private String sendMail;
    private String password;
    private String subject;
    private String message;
    private int controlNodeId;

    @Override
    public void execute(IChimeraContext iChimeraContext) {
        String mail = iChimeraContext.getParam("Participant", "email").toString();
        String date = iChimeraContext.getParam("Schedule", "date").toString();
        String room = iChimeraContext.getParam("Schedule", "room").toString();

        iChimeraContext.setParam("Participant", "email", "hallo welt");

        /* problemlos */
    }

    /**
     * Sets the attributes for the e mail reading the information from
     * properties and database.
     */
    private void setValues() {
        hostName = PropertyLoader.getProperty("mailtask.hostname");
        port = Integer.valueOf(PropertyLoader.getProperty("mailtask.port"));
        sendMail = PropertyLoader.getProperty("mailtask.mail.username");
        password = PropertyLoader.getProperty("mailtask.mail.password");
    }

    private void sendMail(IChimeraContext iChimeraContext, String receiverMail, String subject, String message, String sendMail2) {
        Email email = new SimpleEmail();
        email.setHostName(hostName);
        email.setSmtpPort(port);
        email.setAuthenticator(new DefaultAuthenticator(sendMail, password));
        email.setSSLOnConnect(true);
        try {
            email.setFrom(sendMail2);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(receiverMail);
            email.send();
        } catch (EmailException e) {
            iChimeraContext.log("Error by sending e-Mail - wrong e-Mail properties or wrong config.properties:");
        }
    }
}
