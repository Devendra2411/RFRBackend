package com.ge.rfr.helper;

import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.auth.UserRole;
import com.ge.rfr.common.model.User;
import com.google.common.collect.Lists;

/**
 * Users that can be used to authenticate against the RFR Backend service when it is run in
 * testing mode. See {@link RfrBackendTestConfig#resourceServerTokenServices()}.
 * <p>
 * The name of the enumeration literal can be used as a bearer token.
 */
public enum TestUser {
	
	RFR_TIM(new SsoUser(
            "10000001",
            "tim",
            "Engineer",
            "rfr.engineer@ge.com",
            Lists.newArrayList(UserRole.RFR_TIM)
    )),
    
    RFR_TEAM_MEMBER(new SsoUser(
            "10000002",
            "team",
            "member",
            "team.member@ge.com",
            Lists.newArrayList(UserRole.RFR_TEAM_MEMBER)
    )),
    
    RFR_ADMIN(new SsoUser(
    		"10000003",
            "rfr",
            "admin",
            "rfr.admin@ge.com",
            Lists.newArrayList(UserRole.RFR_ADMIN)
     )),
    
    RFR_COORDINATOR_EUROPE(new SsoUser(
    		"10000004",
            "rfr",
            "europe",
            "rfr.coordinator.europe@ge.com",
            Lists.newArrayList(UserRole.RFR_COORDINATOR_EUROPE)
     )),
    
    RFR_COORDINATOR_ASIA(new SsoUser(
    		"10000005",
            "rfr",
            "asia",
            "rfr.coordinator.asia@ge.com",
            Lists.newArrayList(UserRole.RFR_COORDINATOR_ASIA)
     )),
    RFR_COORDINATOR_INDIA(new SsoUser(
    		"10000006",
            "rfr",
            "india",
            "rfr.coordinator.india@ge.com",
            Lists.newArrayList(UserRole.RFR_COORDINATOR_INDIA)
     )),
    
    RFR_COORDINATOR_LATIN_AMERICA(new SsoUser(
    		"10000008",
            "rfr",
            "asia",
            "rfr.coordinator.la@ge.com",
            Lists.newArrayList(UserRole.RFR_COORDINATOR_LATIN_AMERICA)
     )),

    // Should only be used for update tests and other special cases
    ALL_PRIVILEGES(new SsoUser(
            "10000007",
            "All",
            "Privileges",
            "all.privileges@ge.com",
            Lists.newArrayList(UserRole.values())
    ));

    private final SsoUser ssoUser;

    TestUser(SsoUser ssoUser) {
        this.ssoUser = ssoUser;
    }

    public SsoUser getSsoUser() {
        return ssoUser;
    }

    public User toUser() {
        return new User(this.ssoUser.getSso(), this.ssoUser.getFirstName(), this.ssoUser.getLastName());
    }

}
