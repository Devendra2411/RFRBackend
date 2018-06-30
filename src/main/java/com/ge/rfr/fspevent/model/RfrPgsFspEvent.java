package com.ge.rfr.fspevent.model;

import java.time.Instant;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.RfrRegion;
import com.ge.rfr.workflow.model.ContractType;
import com.ge.rfr.workflow.model.OutageProbability;
import com.ge.rfr.workflow.model.Status;
import com.ge.rfr.workflow.model.OutageType;
import com.ge.rfr.workflow.model.TechnicalCode;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "rfr_pgs_fspevent")
public class RfrPgsFspEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id")
    private int id;

    @NotNull
    @ApiModelProperty(notes = "Plant ID")
    private int plantId;

    @ApiModelProperty(notes = "Block ID")
    private String blockId;

    @NotNull
    @ApiModelProperty(notes = "Train ID")
    private String trainId;

    @NotBlank
    @ApiModelProperty(notes = "Equipment Serial Number ")
    private String equipSerialNumber;

    @NotNull
    @ApiModelProperty(notes = "Outage Id ")
    private int outageId;

    @NotBlank
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "Name of the site")
    private String siteName;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "RFR Region")
    private RfrRegion region;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "TechnicalCode")
    private TechnicalCode technicalCode;

    @NotBlank
    @ApiModelProperty(notes = "Equipment Type")
    private String equipmentType;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Contract Type")
    private ContractType contractType;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Type of the Outage ")
    private OutageType outageType;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Probability of the Outage")
    private OutageProbability outageProbability;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Project Status")
    private Status projectStatus;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Event Status")
    private Status eventStatus;


    @NotNull
    @ApiModelProperty(notes = "Estimated Start Date ")
    private Instant eStartDate;

    @NotNull
    @ApiModelProperty(notes = "Estimated End Date")
    private Instant eEndDate;

    @ApiModelProperty(notes = "Estimated UnitStartup Date")
    private Instant eUnitStartupDate;

    @Embedded
    @Valid
    @NotNull
    @ApiModelProperty(notes = "User Details")
    private ChangeTracking changeTracking;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public RfrRegion getRegion() {
        return region;
    }

    public void setRegion(RfrRegion region) {
        this.region = region;
    }

    public TechnicalCode getTechnicalCode() {
        return technicalCode;
    }

    public void setTechnicalCode(TechnicalCode technicalCode) {
        this.technicalCode = technicalCode;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public OutageType getOutageType() {
        return outageType;
    }

    public void setOutageType(OutageType outageType) {
        this.outageType = outageType;
    }

    public OutageProbability getOutageProbability() {
        return outageProbability;
    }

    public void setOutageProbability(OutageProbability outageProbability) {
        this.outageProbability = outageProbability;
    }

    public Status getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Status projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Status getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Status eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Instant geteStartDate() {
        return eStartDate;
    }

    public void seteStartDate(Instant eStartDate) {
        this.eStartDate = eStartDate;
    }

    public Instant geteEndDate() {
        return eEndDate;
    }

    public void seteEndDate(Instant eEndDate) {
        this.eEndDate = eEndDate;
    }

    public Instant geteUnitStartupDate() {
        return eUnitStartupDate;
    }

    public void seteUnitStartupDate(Instant eUnitStartupDate) {
        this.eUnitStartupDate = eUnitStartupDate;
    }

    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }

}
