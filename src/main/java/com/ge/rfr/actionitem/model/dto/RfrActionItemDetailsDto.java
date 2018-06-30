package com.ge.rfr.actionitem.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Action Item Details Dto to get ESN and OutageId")
public class RfrActionItemDetailsDto {

    @ApiModelProperty(notes = "Equipment Serial Number")
    private String equipSerialNumber;

    @ApiModelProperty(notes = "Site Name")
    private String siteName;

    @ApiModelProperty(notes = "List of Action Items")
    private List<RfrActionItemDto> actionItemsList;

    public String getEquipSerialNumber() {
        return equipSerialNumber;
    }

    public void setEquipSerialNumber(String equipSerialNumber) {
        this.equipSerialNumber = equipSerialNumber;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public List<RfrActionItemDto> getActionItemsList() {
        return actionItemsList;
    }

    public void setActionItemsList(List<RfrActionItemDto> actionItemsList) {
        this.actionItemsList = actionItemsList;
    }

}
