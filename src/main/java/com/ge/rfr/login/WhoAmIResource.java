package com.ge.rfr.login;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ge.rfr.auth.UserRole;
import com.ge.rfr.auth.WebSsoGroupProperties;

@RestController
@RequestMapping("/who-am-i")
public class WhoAmIResource {

    @Autowired
    WebSsoGroupProperties ssoProperties;

    @GetMapping
    public WhoAmIResponse getWhoAmIResponse(@RequestHeader("ge_sub") String sso,
                                            @RequestHeader("ge_firstname") String firstName,
                                            @RequestHeader("ge_lastname") String lastName,
                                            @RequestHeader("ge_mail") String email,
                                            @RequestHeader("ge_gevdsGroupIDmemberOf") String groupId,
                                            @RequestHeader("oidc_access_token") String accessToken,
                                            @RequestHeader("oidc_access_token_expires") int tokenExpiresAbs
    ) {
        WhoAmIResponse response = new WhoAmIResponse();

        response.setSso(sso);
        response.setFirstName(firstName);
        response.setLastName(lastName);
        response.setEmail(email);
        response.setToken(accessToken);
        response.setGroupId(groupId);

        Collection<String> groupidCollection = Arrays.asList(groupId.split(","));

        response.setRoles(getRolesFromWebSsoGroups(groupidCollection));

        // Get the token expiration time in seconds since 1970 (UNIX timestamp),
        // and convert it
        // to the number of seconds relative to the current time.
        int currentTimeSecs = (int) (System.currentTimeMillis() / 1000);
        response.setTokenExpiresIn(tokenExpiresAbs - currentTimeSecs);

        return response;
    }

    // Determines which RFR roles a set of Web SSO groups implies
    private Set<String> getRolesFromWebSsoGroups(Collection<String> webSsoGroups) {
        Set<String> result = new HashSet<>();

        // Defines which roles in RFR a web sso group gets
        Map<String, UserRole[]> groupIdMapping = ssoProperties.getGroups();

        for (String webSsoGroup : webSsoGroups) {
            UserRole[] roles = groupIdMapping.get(webSsoGroup);
            if (roles != null) {
                for (UserRole role : roles) {
                    for (GrantedAuthority grantedAuthority : role.getGrantedAuthorities()) {
                        result.add(grantedAuthority.getAuthority());
                    }
                }
            }
        }
        return result;
    }

}
