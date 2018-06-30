package com.ge.rfr.common.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ge.rfr.auth.SsoUser;

/**
 * This embeddable class is used for user that includes user related information.
 *
 * @author 503055886
 */
@Embeddable
public class User {

    @NotBlank
    private String sso = "";

    @NotBlank
    private String firstName = "";

    @NotBlank
    private String lastName = "";

    public User(String sso, String firstName, String lastName) {
        this.sso = sso;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
    }

    public static User fromSsoUser(SsoUser user) {
        return new User(user.getSso(), user.getFirstName(), user.getLastName());
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return sso.equals(user.sso) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName);

    }

    @Override
    public int hashCode() {
        int result = sso.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + sso + ")";
    }
}