package com.ge.rfr.workflow.model.dto;

import java.time.Instant;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.RfrRegion;
import com.ge.rfr.workflow.model.ContractType;
import com.ge.rfr.workflow.model.OutageProbability;
import com.ge.rfr.workflow.model.OutageType;
import com.ge.rfr.workflow.model.Status;
import com.ge.rfr.workflow.model.TechnicalCode;
import com.ge.rfr.workflow.model.dto.ValidatorGroups.SiteNameValidator;

import io.swagger.annotations.ApiModelProperty;

public class RfrWorkflowDetailsDto {

	@NotNull
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "WorkflowId ",hidden=true)
	private int workflowId;
	
	@NotBlank 
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Equipment Serial Number ",hidden=true)
	private String equipSerialNumber;

	@NotNull
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "OutageId ",hidden=true)
	private int outageId;

	@NotBlank(groups=SiteNameValidator.class)
	@Size(min = 1, max = 255)
	@JsonView({Views.SiteNamesListView.class,Views.CompleteObjectsView.class})
	@ApiModelProperty(notes = "Name of the Site ")
	private String siteName;

	@NotBlank
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Outage Probability ",hidden=true)
	private OutageProbability outageProbability;

	@NotNull
	@JsonView(Views.CompleteObjectsView.class)
	@Enumerated(EnumType.STRING)
	@ApiModelProperty(notes = "Outage Type",hidden=true)
	private OutageType outageType;

	@NotBlank
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Technical Code ",hidden=true)
	private TechnicalCode technicalCode;
	
	@NotBlank
	@Enumerated(EnumType.STRING)
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Contract Type ",hidden=true)
	private ContractType contractType;

	@NotBlank
	@Enumerated(EnumType.STRING)
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Project Status ",hidden=true)
	private Status projectStatus;
	
	@NotBlank
	@Enumerated(EnumType.STRING)
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Event Status ",hidden=true)
	private Status eventStatus;

	
	@NotNull
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Estimated Start Date ",hidden=true)
	private Instant eStartDate;
	
	@NotNull
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Estimated End Date",hidden=true)
	private Instant eEndDate;
	
	@NotNull
	@JsonView(Views.CompleteObjectsView.class)
	@ApiModelProperty(notes = "Estimated UnitStartup Date",hidden=true)
	private Instant eUnitStartupDate;

	@NotNull
	@JsonView(Views.CompleteObjectsView.class)
	@Enumerated(EnumType.STRING)
	@ApiModelProperty(notes = "RFR Region",hidden=true)
	private RfrRegion region;
	
	@Valid
	@NotNull
	@ApiModelProperty(notes = "User Details",hidden=true)
	@JsonView(Views.CompleteObjectsView.class)
	private ChangeTracking changeTracking;
	

	public String getEquipSerialNumber() {
		return equipSerialNumber;
	}

	public void setEquipSerialNumber(String equipSerialNumber) {
		this.equipSerialNumber = equipSerialNumber;
	}

	public int getOutageId() {
		return outageId;
	}

	public void setOutageId(int outageId) {
		this.outageId = outageId;
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

	public OutageProbability getOutageProbability() {
		return outageProbability;
	}

	public void setOutageProbability(OutageProbability outageProbability) {
		this.outageProbability = outageProbability;
	}

	public OutageType getOutageType() {
		return outageType;
	}

	public void setOutageType(OutageType outageType) {
		this.outageType = outageType;
	}

	public TechnicalCode getTechnicalCode() {
		return technicalCode;
	}

	public void setTechnicalCode(TechnicalCode technicalCode) {
		this.technicalCode = technicalCode;
	}

	

	public RfrRegion getRegion() {
		return region;
	}

	public void setRegion(RfrRegion region) {
		this.region = region;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Status getProjectStatus() {
		return projectStatus;
	}

	public Status getEventStatus() {
		return eventStatus;
	}

	public Instant geteStartDate() {
		return eStartDate;
	}

	public Instant geteEndDate() {
		return eEndDate;
	}

	public Instant geteUnitStartupDate() {
		return eUnitStartupDate;
	}

	public ChangeTracking getChangeTracking() {
		return changeTracking;
	}

	public void setProjectStatus(Status projectStatus) {
		this.projectStatus = projectStatus;
	}

	public void setEventStatus(Status eventStatus) {
		this.eventStatus = eventStatus;
	}

	public void seteStartDate(Instant eStartDate) {
		this.eStartDate = eStartDate;
	}

	public void seteEndDate(Instant eEndDate) {
		this.eEndDate = eEndDate;
	}

	public void seteUnitStartupDate(Instant eUnitStartupDate) {
		this.eUnitStartupDate = eUnitStartupDate;
	}

	public void setChangeTracking(ChangeTracking changeTracking) {
		this.changeTracking = changeTracking;
	}

	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

}
