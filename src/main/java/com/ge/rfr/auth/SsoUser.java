package com.ge.rfr.auth;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

public class SsoUser {

    private final String sso;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final Set<UserRole> roles;

    public SsoUser(String sso, String firstName, String lastName, String email, Collection<UserRole> roles) {
        this.sso = sso;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = EnumSet.copyOf(roles);

    }

    public String getSso() {
        return sso;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SsoUser ssoUser = (SsoUser) o;

        return sso.equals(ssoUser.sso);

    }

    @Override
    public int hashCode() {
        return sso.hashCode();
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName + " (" + sso + ")";
    }

}
