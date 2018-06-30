package com.ge.rfr.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final WebSsoGroupProperties ssoProperties;

    public SecurityConfig(WebSsoGroupProperties ssoProperties) {
        this.ssoProperties = ssoProperties;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer rfrpropertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "validator")
    public Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    @Bean
    public ResourceServerTokenServices userInfoTokenService(UserInfoRestTemplateFactory restTemplateFactory,
                                                            ResourceServerProperties resourceServerProperties) {

        UserInfoTokenServices service = new UserInfoTokenServices(
                resourceServerProperties.getUserInfoUri(),
                resourceServerProperties.getClientId()
        );

        service.setPrincipalExtractor(this::extractPrincipal);
        service.setAuthoritiesExtractor(this::extractAuthorities);
        service.setRestTemplate(restTemplateFactory.getUserInfoRestTemplate());
        service.setTokenType(resourceServerProperties.getTokenType());

        // Create a proxy for the user info token service that caches the results
        Cache<String, OAuth2Authentication> cache = Caffeine.from(ssoProperties.getTokenCacheSpec()).build();
        return new CachingUserInfoTokenService(service, cache);

    }

    private List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        Set<UserRole> roles = getRolesFromWebSsoGroups(getWebSsoGroups(map));
        roles.forEach(role -> authorities.addAll(role.getGrantedAuthorities()));

        return new ArrayList<>(authorities);
    }

    private Object extractPrincipal(Map<String, Object> map) {
        String sso = (String) map.getOrDefault("sub", "<unknown>");
        String mail = (String) map.get("mail");
        String firstName = (String) map.get("firstname");
        String lastName = (String) map.get("lastname");

        List<String> webSsoGroups = getWebSsoGroups(map);
        Set<UserRole> roles = getRolesFromWebSsoGroups(webSsoGroups);

        return new SsoUser(sso, firstName, lastName, mail, roles);
    }

    @Bean
    public SsoUserArgumentConfigurer ssoUserArgumentConfigurer() {
        return new SsoUserArgumentConfigurer();
    }


    private List<String> getWebSsoGroups(Map<String, Object> map) {
        Object groupListObj = map.get("gevdsGroupIDmemberOf");

        Collection groupList = new ArrayList<>();
        if (!(groupListObj instanceof Collection)) {
            if (groupListObj == null) {
                return Collections.emptyList();
            } else {
                groupList.add(groupListObj.toString());
            }
        } else {
            groupList = (Collection) groupListObj;
        }

        if (!(groupListObj instanceof Collection)) {
            groupList.add(groupListObj.toString());
        }
        List<String> result = new ArrayList<>(groupList.size());

        for (Object entryObj : groupList) {
            if (entryObj instanceof String) {
                result.add((String) entryObj);
            }
        }

        return result;
    }

    // Determines which RFR roles a set of Web SSO groups implies
    private Set<UserRole> getRolesFromWebSsoGroups(Collection<String> webSsoGroups) {
        EnumSet<UserRole> result = EnumSet.noneOf(UserRole.class);

        // Defines which roles in RFR a web sso group gets
        Map<String, UserRole[]> groupIdMapping = ssoProperties.getGroups();

        for (String webSsoGroup : webSsoGroups) {
            UserRole[] roles = groupIdMapping.get(webSsoGroup);
            if (roles != null) {
                Collections.addAll(result, roles);
            }
        }

        logger.debug("Mapped WebSSO groups {} to roles {}", webSsoGroups, result);

        return result;
    }

}
