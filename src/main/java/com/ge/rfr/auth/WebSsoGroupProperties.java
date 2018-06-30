package com.ge.rfr.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures the mapping between WebSSO groups and the corresponding user roles in RFR.
 */
@ConfigurationProperties(prefix = "sso")
@Component
public class WebSsoGroupProperties {

    private Map<String, UserRole[]> groups = new HashMap<>();

    private String tokenCacheSpec = "";

    public Map<String, UserRole[]> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, UserRole[]> groups) {
        this.groups = groups;
    }

    public String getTokenCacheSpec() {
        return tokenCacheSpec;
    }

    public void setTokenCacheSpec(String tokenCacheSpec) {
        this.tokenCacheSpec = tokenCacheSpec;
    }

}
