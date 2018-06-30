package com.ge.rfr.login;

import java.util.HashSet;
import java.util.Set;

public class WhoAmIResponse {

    private String sso;

    private String firstName;

    private String lastName;

    private String email;

    private String token;

    private int tokenExpiresIn;

    private Set<String> roles = new HashSet<>();

    private String groupId;

    public String getSso() {
        return sso;
    }

    public void setSso(String sso) {
        this.sso = sso;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public void setTokenExpiresIn(int tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
