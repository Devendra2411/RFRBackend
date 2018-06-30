package com.ge.rfr.actionitem.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum Category implements CommonOptionsEnum {
    START_UP_CHECKLIST("Start-Up Checklist"),
    TUNING("Tuning"),
    GENERATOR("Generator"),
    CLEANLINESS("Cleanliness"),
    COMPLIANCE_CHECKLIST("Compliance Checklist"),
    MD_CENTER("MD Center"),
    AUDITS_COMM_PROCEDURES("Audits and Comissioning Procedures"),
    SITE_DEMOBILIZATION("Site Demobilization"),
    TUNING_KIT("Tuning Kit"),
    CONTROLS("Controls");

    private final String text;

    Category(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
