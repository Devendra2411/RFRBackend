package com.ge.rfr.phonecalldetails.model;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.workflow.model.RfrWorkflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "phone_call_details")
@ApiModel(description = "Entity for Phone Call Details")
public class PhoneCallDetails {

    // Name of the db constraint for uniqueness of the name
    public static final String UNQ_NAME_CONSTRAINT = "phone_call_details_unq_name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id")
    private int id;

    @NotBlank
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "Meeting Line")
    private String meetingLine;

    @NotNull
    @ApiModelProperty(notes = "Date of the Phone Call Meeting")
    private Instant meetingDate;

    @ApiModelProperty(notes = "Phone Call Minutes")
    private String phoneCallMinutes;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "workflowId")
    @NotNull
    @ApiModelProperty(notes = "Rfr Workflow Id")
    private RfrWorkflow rfrWorkflow;

    @Embedded
    @Valid
    @NotNull
    @ApiModelProperty(notes = "User Details")
    private ChangeTracking changeTracking;

    public PhoneCallDetails() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeetingLine() {
        return meetingLine;
    }

    public void setMeetingLine(String meetingLine) {
        this.meetingLine = meetingLine;
    }

    public Instant getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Instant meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getPhoneCallMinutes() {
        return phoneCallMinutes;
    }

    public void setPhoneCallMinutes(String phoneCallMinutes) {
        this.phoneCallMinutes = phoneCallMinutes;
    }

    public RfrWorkflow getRfrWorkflow() {
        return rfrWorkflow;
    }

    public void setRfrWorkflow(RfrWorkflow rfrWorkflow) {
        this.rfrWorkflow = rfrWorkflow;
    }

    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }

}
