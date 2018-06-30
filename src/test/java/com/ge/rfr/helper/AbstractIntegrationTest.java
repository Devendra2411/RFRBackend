package com.ge.rfr.helper;

import ch.qos.logback.classic.jul.JULHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.common.errors.ErrorDetails;
import com.ge.rfr.common.errors.ValidationErrorDetails;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;
import org.apache.commons.mail.util.MimeMessageParser;
import org.assertj.core.api.Fail;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import javax.mail.internet.MimeMessage;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.ContextResolver;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public abstract class AbstractIntegrationTest {

    
    private final static ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new JavaTimeModule())
            .registerModule(new GuavaModule());

    private static AppHelper appHelper;

    private static JerseyClient client;

    protected MailSpy mailSpy;

    private AppHelper getAppHelper() throws Exception {
        if (appHelper == null) {
            appHelper = new AppHelper();
            appHelper.start();
        }
        return appHelper;
    }

    @BeforeClass
    public void setUpApp() throws Exception {
        getAppHelper(); // Starts the server

        appHelper.autowire(this);
    }

    @BeforeMethod
    public void resetMailMock() {
        // Get the mail mock and reset it for this test
        mailSpy = appHelper.getMailSpy();
        Mockito.reset(mailSpy);
    }

    private class ObjectMapperResolver implements ContextResolver<ObjectMapper> {

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return mapper;
        }

    }

    protected JerseyWebTarget getClient(TestUser testUser) throws Exception {

        if (client == null) {
            // Get a java.util.logging logger delegating to SLF4j
            Logger logger = JULHelper.asJULLogger("client.requests");

            client = new JerseyClientBuilder()
                    .register(new ObjectMapperResolver())
                    .register(new JacksonFeature())
                    .register(MultiPartFeature.class)
                    // Log the first 5kb of any request/response
                    .register(new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_TEXT, 5000))
                    .build();
        }

        return client.target(getAppHelper().getBaseUrl())
                .register((ClientRequestFilter) clientRequestContext -> {
                    clientRequestContext.getHeaders().putSingle("Authorization", "Bearer " + testUser.name());
                });

    }

    public void onAfterSuite() throws Exception {
        if (client != null) {
            client.close();
        }
        if (appHelper != null) {
            appHelper.stop();
        }
    }

    protected void validatePristineChangeTracking(ChangeTracking tracking, TestUser user) {

        SsoUser ssoUser = user.getSsoUser();
        assertEquals(ssoUser.getSso(), tracking.getCreatedBy().getSso());
        assertEquals(ssoUser.getFirstName(), tracking.getCreatedBy().getFirstName());
        assertEquals(ssoUser.getLastName(), tracking.getCreatedBy().getLastName());

        assertNotNull(tracking.getCreatedDate());
        int elapsedSeconds = (int) Duration.between(tracking.getCreatedDate(), Instant.now()).getSeconds();
        assertThat(elapsedSeconds)
                .overridingErrorMessage("Object creation should be between 0 and 40 seconds in the past.")
                .isLessThanOrEqualTo(40)
                .isGreaterThanOrEqualTo(0);

        assertEquals(ssoUser.getSso(), tracking.getModifiedBy().getSso());
        assertEquals(ssoUser.getFirstName(), tracking.getModifiedBy().getFirstName());
        assertEquals(ssoUser.getLastName(), tracking.getModifiedBy().getLastName());

        assertNotNull(tracking.getModifiedDate());
        // For a new object, the creation date should be equal to the modification date
        assertEquals(tracking.getCreatedDate(), tracking.getModifiedDate());

    }

    // Validate that a given change tracking object has the same creation parameters as the other given change tracking,
    // and that it reflects a recent modification by the given user
    protected void validateModifiedChangeTracking(ChangeTracking tracking, ChangeTracking originalTracking, TestUser modifyingUser) {

        // Change tracking for the creation should be equal
        assertThat(tracking.getCreatedBy()).isEqualTo(originalTracking.getCreatedBy());
        assertThat(tracking.getCreatedDate()).isEqualTo(originalTracking.getCreatedDate());

        // Modification user should be the given user
        assertThat(tracking.getModifiedBy()).isEqualTo(User.fromSsoUser(modifyingUser.getSsoUser()));

        // Modification time should be within the last 5 seconds, but greater than creation time
        assertNotNull(tracking.getModifiedDate());
        assertThat(tracking.getModifiedDate().isAfter(tracking.getCreatedDate()))
                .overridingErrorMessage("Object modification date should be after creation time.");
        int elapsedSeconds = (int) Duration.between(tracking.getModifiedDate(), Instant.now()).getSeconds();
        assertThat(elapsedSeconds)
                .overridingErrorMessage("Object modification date should be between 0 and 5 seconds in the past.")
                .isLessThanOrEqualTo(5)
                .isGreaterThanOrEqualTo(0);

    }

    /**
     * Executes the given runnable and assumes that a Jersey BadRequestException will be thrown.
     * Also assumes that the bad request response body contains a JSON object describing a validation
     * error, and returns that error.
     */
    protected ValidationErrorDetails assumeValidationError(ThrowingRunnable runnable) throws Exception {
        try {
            runnable.run();
            Fail.fail("Expected request to fail");
            throw new RuntimeException(); // Will not be reached due to fail
        } catch (BadRequestException e) {
            ErrorDetails errorDetails = e.getResponse().readEntity(ErrorDetails.class);
            assertThat(errorDetails).isInstanceOf(ValidationErrorDetails.class);
            return (ValidationErrorDetails) errorDetails;
        }
    }

    /**
     * Asserts that an E-Mail was sent from one user to another, and returns the parsed E-Mail as a result.
     */
    protected MimeMessageParser assertMailSent(TestUser from, TestUser to) throws Exception {
        return assertMailSent(from, to.toUser());
    }

    /**
     * Asserts that an E-Mail was sent from one user to another, and returns the parsed E-Mail as a result.
     */
    protected MimeMessageParser assertMailSent(TestUser from, User to) throws Exception {

        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSpy).mailReceived(
                eq(from.getSsoUser().getSso() + "@mail.ad.ge.com"),
                eq(to.getSso() + "@mail.ad.ge.com"),
                messageCaptor.capture()
        );

        MimeMessage mailMessage = messageCaptor.getValue();
        return new MimeMessageParser(mailMessage).parse();

    }

    @FunctionalInterface
    protected interface ThrowingRunnable {
        void run() throws Exception;
    }

    
}