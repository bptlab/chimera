package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Created by jaspar.mang on 28.01.15.
 */
public class EmailTaskExecutionBehavior extends TaskExecutionBehavior {
    private int port;
    private String serverAddress;
    private String receiverMail;
    private String sendMail;
    private String subject;
    private String message;
    private int controlNode_id;
    private DbEmailConfiguration emailConfiguration = new DbEmailConfiguration();


    public EmailTaskExecutionBehavior(int controlNode_id){
        this.controlNode_id = controlNode_id;
    }

    @Override
    public void execute(){
        this.setValues();
        this.sendMail();
    }

    private void setValues(){
        port = 1024;
        serverAddress = "localhost";
        receiverMail = emailConfiguration.getReceiverEmailAddress(controlNode_id);
        sendMail = emailConfiguration.getSendEmailAddress(controlNode_id);
        subject = emailConfiguration.getSubject(controlNode_id);
        message = emailConfiguration.getMessage(controlNode_id);
    }

    private void setFakeValues(){
        port = 1024;
        serverAddress = "localhost";
        sendMail = "sender@server.de";
        receiverMail = "receiver@server.de";
        subject = "this is an eMail";
        message = "Hello, i'm writing you.";
    }

    private void sendMail(){
        Email email = new SimpleEmail();
        email.setHostName(serverAddress);
        email.setSmtpPort(port);
        try {
            email.setFrom(sendMail);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(receiverMail);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
