package com.ge.rfr.helper;

import javax.mail.internet.MimeMessage;

/**
 * This interface is notified by the embedded SMTP server of incoming messages.
 */
public interface MailSpy {

    /**
     * This is called by the internal SMTP server when it receives a message.
     * Can be used with Mockito.verify to check that a mail has been received.
     */
    void mailReceived(String sender, String recipient, MimeMessage mail);

}
