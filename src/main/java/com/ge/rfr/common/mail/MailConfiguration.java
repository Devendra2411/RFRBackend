package com.ge.rfr.common.mail;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "mail")
@Component
public class MailConfiguration {

    /**
     * The base URL for building links to the application in E-Mails.
     */
    @NotNull
    private String applicationLinkBase;

    /**
     * Used as the envelope sender address for SMTP when RFR sends E-Mail.
     */
    @NotNull
    @Email
    private String fromAddress;

    /**
     * Used to build mail addresses from SSOs. This string is simply appended
     * to the SSO to build the mail recipient.
     */
    @NotEmpty
    private String ssoMailAddressSuffix = "@mail.ad.ge.com";

    public String getApplicationLinkBase() {
        return applicationLinkBase;
    }

    public void setApplicationLinkBase(String applicationLinkBase) {
        this.applicationLinkBase = applicationLinkBase;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getSsoMailAddressSuffix() {
        return ssoMailAddressSuffix;
    }

    public void setSsoMailAddressSuffix(String ssoMailAddressSuffix) {
        this.ssoMailAddressSuffix = ssoMailAddressSuffix;
    }
}
