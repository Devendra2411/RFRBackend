package com.ge.rfr.phonecalldetails.model.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Phone CallDetails Dto to get ESN and OutageId")
public class PhoneCallDto {

    @ApiModelProperty(notes = "Equipment Serial Number")
    @NotBlank
    private String equipSerialNumber;

    @ApiModelProperty(notes = "Site Name")
    @NotNull
    private String siteName;

    @ApiModelProperty(notes = "List of PhoneCallDetailsDto")
    @NotNull
    private List<PhoneCallDetailsDto> phoneCallDetailsDtoList;

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

    public List<PhoneCallDetailsDto> getPhoneCallDetailsDtoList() {
        return phoneCallDetailsDtoList;
    }

    public void setPhoneCallDetailsDtoList(List<PhoneCallDetailsDto> phoneCallDetailsDtoList) {
        this.phoneCallDetailsDtoList = phoneCallDetailsDtoList;
    }

}
