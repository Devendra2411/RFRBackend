package com.ge.rfr.workflow.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum OutageType implements CommonOptionsEnum {
    CALL_OUT("Call Out"),
    HGP("HGP"),
    MI("MI"),
    NA("NA"),
    ONSITE_TRAINING("On Site Training"),
    OTHER("Other"),
    TDI("TDI"),
    VALUE_PACK("Value Pack"),
    WARRANTY("Warranty");

    private final String text;

    OutageType(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}