package com.ge.rfr.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.ge.rfr.fspevent.model.RfrPgsFspEvent;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class SerialNumberDetailsDto {

    @JsonView(Views.EsnListView.class)
    @NotNull
    @ApiModelProperty(dataType = "String", notes = "Equipment Serial Number ")
    private String equipSerialNumber;

    @JsonView(Views.EsnListView.class)
    @NotBlank
    @ApiModelProperty(notes = "Name of the site")
    private String siteName;

    @JsonView(Views.OutageListView.class)
    @ApiModelProperty(notes = "OutageId ")
    private int outageId;

    public static SerialNumberDetailsDto valueOf(RfrPgsFspEvent event) {
        SerialNumberDetailsDto result = new SerialNumberDetailsDto();
        result.setEquipSerialNumber(event.getEquipSerialNumber());
        result.setSiteName(event.getSiteName());
        result.setOutageId(event.getOutageId());
        return result;
    }

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

    public int getOutageId() {
        return outageId;
    }

    public void setOutageId(int outageId) {
        this.outageId = outageId;
    }

}
