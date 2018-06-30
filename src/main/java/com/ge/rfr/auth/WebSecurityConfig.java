package com.ge.rfr.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Override
    public void init(WebSecurity web) {
        web.ignoring().mvcMatchers("/who-am-i");
        web.ignoring().mvcMatchers("/galSearch/**");
        web.ignoring().mvcMatchers(AUTH_WHITELIST);
    }

}
