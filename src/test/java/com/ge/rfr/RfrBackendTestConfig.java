package com.ge.rfr;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.ge.rfr.helper.TestUser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RfrBackendTestConfig {

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }

    @Bean
    @Primary
    public ResourceServerTokenServices resourceServerTokenServices() {

        return new ResourceServerTokenServices() {
            @Override
            public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
                // Determine the test user we're using based on the bearer token
                TestUser testUser;
                try {
                    testUser = TestUser.valueOf(accessToken);
                } catch (IllegalArgumentException e) {
                    throw new InvalidTokenException("Unknown token.");
                }

                // Create our fake OAuth2 authentication based on the test user that was selected via
                // the bearer token name
                OAuth2Request req = new OAuth2Request(Collections.emptyMap(),
                        "",
                        Collections.emptyList(),
                        true, // Mark as approved (important)
                        Collections.emptySet(),
                        null,
                        "",
                        null,
                        null);

                // Get all authorities (-> roles) that the test user has
                List<GrantedAuthority> authorities = testUser.getSsoUser().getRoles().stream()
                        .flatMap(r -> r.getGrantedAuthorities().stream())
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        testUser.getSsoUser(),
                        "N/A",
                        authorities
                );

                return new OAuth2Authentication(
                        req,
                        auth
                );
            }

            @Override
            public OAuth2AccessToken readAccessToken(String accessToken) {
                throw new UnsupportedOperationException();
            }
        };

    }

}
