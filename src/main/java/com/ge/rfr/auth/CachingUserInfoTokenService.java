package com.ge.rfr.auth;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

class CachingUserInfoTokenService implements ResourceServerTokenServices {

    private final ResourceServerTokenServices delegate;

    private final Cache<String, OAuth2Authentication> authenticationCache;

    CachingUserInfoTokenService(UserInfoTokenServices service, Cache<String, OAuth2Authentication> cache) {
        this.delegate = service;
        this.authenticationCache = cache;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        return authenticationCache.get(accessToken, delegate::loadAuthentication);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        // Uncached because this is unsupported
        return delegate.readAccessToken(accessToken);
    }

}
