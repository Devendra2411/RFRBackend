package com.ge.rfr.common.mail;

import com.ge.rfr.common.model.User;
import freemarker.cache.ClassTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.*;
import freemarker.template.utility.NullWriter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Provides functions related to sending mail.
 */
@Service
public class MailService {

    private final JavaMailSender mailSender;

    private final Configuration freemarker;

    private final MailConfiguration configuration;

    public MailService(MailConfiguration configuration, JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.configuration = configuration;

        this.freemarker = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        this.freemarker.setTemplateLoader(new ClassTemplateLoader(MailService.class, "/templates/mail"));
    }

    /**
     * Sends a mail to the given user, using the given template name and map of template model values.
     *
     * @param from         The user that initiated the mail to be sent.
     * @param to           The user that the mail will be sent to.
     * @param templateName The name of the FreeMarker template to use for sending this E-Mail. Templates are loaded
     *                     from the classpath relative to this class.
     * @param model        The model values that will be exposed to the mail template.
     */
    public void sendMail(User from, User to, String templateName, Map<String, Object> model) throws IOException {

        Template template = freemarker.getTemplate(templateName);

        Environment env;
        try {
            env = template.createProcessingEnvironment(model, NullWriter.INSTANCE);
            env.getMainNamespace().put("recipient", to);
            env.getMainNamespace().put("sender", from);
            env.getMainNamespace().put("appUrl", configuration.getApplicationLinkBase());
            env.process();
        } catch (TemplateException e) {
            throw new RuntimeException("Unable to apply mail template " + templateName, e);
        }

        String subject = getStringVariable(env, "subject");
        String textBody = getStringVariable(env, "text_body");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getMailAddress(from));
        message.setTo(getMailAddress(to));
        message.setSubject(subject);
        message.setText(textBody);

        mailSender.send(message);

    }

    private String getMailAddress(User user) {
        // TODO: Sanitize the User credentials for the mail address
        return String.format(
                "%s %s <%s%s>", user.getFirstName(),
                user.getLastName(),
                user.getSso(),
                configuration.getSsoMailAddressSuffix()
        );
    }

    private String getStringVariable(Environment env, String name) {
        Environment.Namespace namespace = env.getMainNamespace();
        TemplateModel templateModel;
        try {
            templateModel = namespace.get(name);
        } catch (TemplateModelException e) {
            throw new IllegalStateException("The mail template doesn't set a variable '" + name + "'");
        }

        if (!(templateModel instanceof TemplateScalarModel)) {
            throw new IllegalStateException("The mail template sets variable '" + name + "', but it's not a string." +
                    " It's a " + templateModel);
        }

        try {
            return ((TemplateScalarModel) templateModel).getAsString().trim();
        } catch (TemplateModelException e) {
            throw new IllegalStateException("Unable to get variable '" + name + "' from template as string.", e);
        }
    }

}
