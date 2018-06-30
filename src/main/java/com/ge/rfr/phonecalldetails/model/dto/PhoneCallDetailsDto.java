package com.ge.rfr.phonecalldetails.model.dto;

import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.phonecalldetails.model.PhoneCallDetails;
import com.ge.rfr.phonecalldetails.model.dto.ValidatorGroups.PhoneCallMinutesValidator;

import io.swagger.annotations.ApiModelProperty;

public class PhoneCallDetailsDto {

    @ApiModelProperty(notes = "Id")
    @NotNull
    private int meetingId;

    @NotBlank
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "Meeting Line")
    private String meetingLine;

    @NotNull
    @ApiModelProperty(notes = "Date of the Phone Call Meeting")
    private Instant meetingDate;

    @JsonView(Views.PhoneCallDetailsView.class)
    @NotBlank(groups = PhoneCallMinutesValidator.class)
    @ApiModelProperty(notes = "Phone Call Minutes")
    private String phoneCallMinutes;

    @ApiModelProperty(notes = "User Details")
    private ChangeTracking changeTracking;

    public static PhoneCallDetailsDto valueOf(PhoneCallDetails phoneCallDetails) {
        PhoneCallDetailsDto phoneCallDetailsDto = new PhoneCallDetailsDto();

        phoneCallDetailsDto.setMeetingId(phoneCallDetails.getId());
        phoneCallDetailsDto.setMeetingLine(phoneCallDetails.getMeetingLine());
        phoneCallDetailsDto.setMeetingDate(phoneCallDetails.getMeetingDate());
        phoneCallDetailsDto.setPhoneCallMinutes(phoneCallDetails.getPhoneCallMinutes());
        phoneCallDetailsDto.setChangeTracking(phoneCallDetails.getChangeTracking());

        return phoneCallDetailsDto;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
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

    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }

}

