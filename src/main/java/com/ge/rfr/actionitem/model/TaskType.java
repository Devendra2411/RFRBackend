package com.ge.rfr.actionitem.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum TaskType implements CommonOptionsEnum {

    FIRST_FIRE("First Fire"),
    DEMOBILIZATION("Demobilization");

    private final String text;

    TaskType(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
