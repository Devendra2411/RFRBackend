package com.ge.rfr.fspevent.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;


public class TrainDataDto {

    @NotNull
    private String trainId;

    @NotBlank
    @ApiModelProperty(notes = "ESN and Outages List")
    private List<EsnAndOutageDto> esnAndOutagesList;

    @JsonIgnore
    public int getSize() {
		return esnAndOutagesList.size();
	}
    
    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public List<EsnAndOutageDto> getEsnAndOutagesList() {
        return esnAndOutagesList;
    }

	public void setEsnAndOutagesList(List<EsnAndOutageDto> esnAndOutagesList) {
		this.esnAndOutagesList = esnAndOutagesList;
	}

}
