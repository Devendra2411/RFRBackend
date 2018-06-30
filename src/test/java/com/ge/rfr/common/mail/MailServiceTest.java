package com.ge.rfr.common.mail;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> msgCaptor;

    private MailService service;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        MailConfiguration mailConfiguration = new MailConfiguration();
        mailConfiguration.setFromAddress("rfr@ge.com");
        mailConfiguration.setApplicationLinkBase("http://rfr.com/");
        service = new MailService(mailConfiguration, mailSender);
    }

    @Test
    public void testSendMail() throws Exception {/*

        // Set Up Model
        User fromUser = new User("11111112", "Karl", "Test");
        User toUser = new User("11111111", "Firstname", "Lastname");
        Map<String, Object> model = new HashMap<>();
        RfrProject project = new RfrProject();
        project.setName("Test Project");
        model.put("project", project);

        // Send the mail
        service.sendMail(fromUser, toUser, "workflowAssignment.ftl", model);

        // Check that it has been sent
        verify(mailSender).send(msgCaptor.capture());

        // Verify the message that was sent
        SimpleMailMessage msg = msgCaptor.getValue();
        assertThat(msg.getFrom()).isEqualTo("Karl Test <11111112@mail.ad.ge.com>");
        assertThat(msg.getTo()).containsExactly("Firstname Lastname <11111111@mail.ad.ge.com>");
        assertThat(msg.getSubject()).isEqualTo("You have been assigned to project 'Test Project'");
        assertThat(msg.getText()).isEqualToIgnoringWhitespace("Dear Firstname Lastname, "
                + "Karl Test (11111112) has assigned you to project Test Project in RFR. "
                + "You can access the project at the following location: "
                + "http://rfr.com/#/rfr-projects/0");

    */}

}