package com.ge.rfr.actionitem.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum Owner implements CommonOptionsEnum {
    FE("FE"),
    CONTROLS_REQ_ENGINEER("Controls Req Engr"),
    IRFR_ADMIN("IRFR Admin"),
    OPS_CENTER("Ops Center"),
    SIL_ENGINEER("SIL Engineer"),
    SIL_COMPLIANCE_LEADER("SIL Compliance Leader"),
    LEVEL_OWNER("Level Engineer"),
    CPM("CPM"),
    PSENGR("PSENGR");

    private final String text;

    Owner(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
