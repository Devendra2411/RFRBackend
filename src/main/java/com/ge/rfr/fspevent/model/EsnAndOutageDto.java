package com.ge.rfr.fspevent.model;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;


public class EsnAndOutageDto {

    @NotNull
    private String equipSerialNumber;

    @NotBlank
    @ApiModelProperty(notes = "Outages List")
    private Set<Integer> outagesList;

    public String getEquipSerialNumber() {
        return equipSerialNumber;
    }


    public void setEquipSerialNumber(String equipSerialNumber) {
        this.equipSerialNumber = equipSerialNumber;
    }


    public Set<Integer> getOutagesList() {
        return outagesList;
    }


    public void setOutagesList(Set<Integer> outagesList) {
        this.outagesList = outagesList;
    }

}
