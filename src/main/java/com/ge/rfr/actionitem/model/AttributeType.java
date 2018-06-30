package com.ge.rfr.actionitem.model;

import com.ge.rfr.common.options.CommonOptionsEnum;

public enum AttributeType implements CommonOptionsEnum {
    TECHNOLOGY("Technology"),
    OUTAGE_STATUS("OutageStatus"),
    OUTAGE_TYPE("OutageType"),
    EQUIPMENT_TYPE("EquipmentType"),
    EQUIP_SERIAL_NUMBER("Equipment Serial Number"),
    CONTRACT_TYPE("ContractType"),
    REGION("PGSRegion"),
    OUTAGE_ID("OutageID");

    private final String text;

    AttributeType(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
