package com.ge.rfr.auth;

import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.Principal;
import java.util.List;

/**
 * This class handles providing SsoUser arguments to @RequestMapping annotated methods, based on which user is
 * currently logged in.
 */
class SsoUserArgumentConfigurer extends WebMvcConfigurerAdapter implements HandlerMethodArgumentResolver {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == SsoUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Principal principal = webRequest.getUserPrincipal();
        // Extract our SSOUser from the OAuth2 authentication
        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication auth = (OAuth2Authentication) principal;
            return auth.getPrincipal();
        }

        return null;
    }
}
