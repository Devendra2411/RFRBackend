package com.ge.rfr.galsearch.dto;

public class EmployeeDetailsDto {

    private String sso;
    private String firstName;
    private String lastName;
    private String emailId;

    public String getSso() {
        return sso;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setMail(String mail) {
        emailId = mail;
    }

    public void setUid(String uid) {
        sso = uid;
    }

    public void setGeoraclehrid(String georaclehrid) {
        sso = georaclehrid;
    }

    public void setGivenName(String givenName) {
        firstName = givenName;
    }

    public void setSn(String sn) {
        lastName = sn;
    }
}
