package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Created by jaspar.mang on 28.01.15.
 */
public class EmailTaskExecutionBehavior extends TaskExecutionBehavior {
    int port;
    String serverAddress;
    String receiverMail;
    String sendMail;
    String subject;
    String message;


    @Override
    public void execute(){
        this.setFakeValues();
        this.sendMail();
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
