package com.ge.rfr.actionitem.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum Status implements CommonOptionsEnum {

    COMPLETE("Complete"),
    INCOMPLETE("Incomplete"),
    NA("NA");

    private final String text;

    Status(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
