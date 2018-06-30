package com.ge.rfr.helper;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.helper.SimpleMessageListener;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.wiser.Wiser;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.mockito.Mockito.mock;

/**
 * Helps with mocks of external services that are used by the embedded Spring Boot application for testing.
 */
public class SmtpServerHelper {

    private static final Logger logger = LoggerFactory.getLogger(SmtpServerHelper.class);

    // MimeMessage parsing wants a JavaMail session, even if not needed
    private final Session session = Session.getDefaultInstance(new Properties());

    // This is a fully functional embedded SMTP server
    private final Wiser wiser = new Wiser(0);

    private final MailSpy mailSpy = mock(MailSpy.class);

    public void start() throws Exception {

        wiser.getServer().setMessageHandlerFactory(new SimpleMessageListenerAdapter(new MailListener()));

        wiser.start();
        logger.info("Started SMTP server on port {}", wiser.getServer().getPort());
    }

    public void stop() throws Exception {
        try {
            wiser.stop();
        } catch (Exception e) {
            logger.error("Unable to stop SMTP server.", e);
        }
    }

    public int getSmtpPort() {
        return wiser.getServer().getPort();
    }

    public MailSpy getMailSpy() {
        return mailSpy;
    }

    private class MailListener implements SimpleMessageListener {

        @Override
        public boolean accept(String from, String recipient) {
            return true; // Accept all mails
        }

        @Override
        public void deliver(String from, String recipient, InputStream data) throws IOException {

            byte[] messageBody = ByteStreams.toByteArray(data);

            // Parse the messagebody as a MIME message
            MimeMessage mimeMessage;
            try {
                mimeMessage = new MimeMessage(session, new ByteArrayInputStream(messageBody));
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            // Send it to our Mockito mock so it's recorded for verifies
            mailSpy.mailReceived(from, recipient, mimeMessage);

        }

    }

}
