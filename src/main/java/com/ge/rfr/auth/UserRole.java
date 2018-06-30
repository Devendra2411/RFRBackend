package com.ge.rfr.auth;

import com.google.common.collect.ImmutableList;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static com.ge.rfr.auth.Privileges.*;

public enum UserRole {

    RFR_ADMIN(
            WORKFLOW_CREATE,
            WORKFLOW_VIEW,
            PHONE_CALL_CREATE,
            PHONE_CALL_VIEW,
            ACTION_ITEM_CREATE,
            ACTION_ITEM_VIEW),

    RFR_INSTALLATION_DIRECTOR(
            WORKFLOW_CREATE,
            WORKFLOW_VIEW,
            PHONE_CALL_CREATE,
            PHONE_CALL_VIEW,
            ACTION_ITEM_CREATE,
            ACTION_ITEM_VIEW),
    
    RFR_COORDINATOR_ASIA(
            WORKFLOW_CREATE,
            WORKFLOW_VIEW,
            PHONE_CALL_CREATE,
            PHONE_CALL_VIEW,
            ACTION_ITEM_CREATE,
            ACTION_ITEM_VIEW),
    
    RFR_COORDINATOR_INDIA(
            WORKFLOW_CREATE,
            WORKFLOW_VIEW,
            PHONE_CALL_CREATE,
            PHONE_CALL_VIEW,
            ACTION_ITEM_CREATE,
            ACTION_ITEM_VIEW),
    
    RFR_COORDINATOR_EUROPE(
            WORKFLOW_CREATE,
            WORKFLOW_VIEW,
            PHONE_CALL_CREATE,
            PHONE_CALL_VIEW,
            ACTION_ITEM_CREATE,
            ACTION_ITEM_VIEW),
    
    RFR_COORDINATOR_LATIN_AMERICA(
            WORKFLOW_CREATE,
            WORKFLOW_VIEW,
            PHONE_CALL_CREATE,
            PHONE_CALL_VIEW,
            ACTION_ITEM_CREATE,
            ACTION_ITEM_VIEW),
    
    RFR_COORDINATOR_MEA(
            WORKFLOW_CREATE,
            WORKFLOW_VIEW,
            PHONE_CALL_CREATE,
            PHONE_CALL_VIEW,
            ACTION_ITEM_CREATE,
            ACTION_ITEM_VIEW),
    
    RFR_COORDINATOR_NORTH_AMERICA(
            WORKFLOW_CREATE,
            WORKFLOW_VIEW,
            PHONE_CALL_CREATE,
            PHONE_CALL_VIEW,
            ACTION_ITEM_CREATE,
            ACTION_ITEM_VIEW),
    
    RFR_TIM(
            WORKFLOW_VIEW,
            PHONE_CALL_VIEW,
            ACTION_ITEM_VIEW),

    RFR_TEAM_MEMBER(
            WORKFLOW_VIEW,
            PHONE_CALL_VIEW,
            ACTION_ITEM_VIEW);

    private final List<GrantedAuthority> grantedAuthorities;

    UserRole(String... privileges) {
        // Spring Security requires the ROLE_ prefix for all roles
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Automatically add a privilege for the user role itself
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));

        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }

        this.grantedAuthorities = ImmutableList.copyOf(authorities);
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

}
