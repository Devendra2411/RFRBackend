package com.ge.rfr.actionitem.notes.model.dto;

import org.hibernate.validator.constraints.NotBlank;

import com.ge.rfr.actionitem.notes.model.RfrNotes;
import com.ge.rfr.common.model.ChangeTracking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Dto for Action Item Notes")
public class RfrNotesDto {

    @ApiModelProperty(notes = "Notes Id")
    private int notesId;

    @NotBlank
    @ApiModelProperty(notes = "Notes")
    private String notes;

    @ApiModelProperty(notes = "Action Item Id ")
    private int actionItemId;

    @ApiModelProperty(notes = "Change Tracking of Users")
    private ChangeTracking changeTracking;

    public static RfrNotesDto valueOf(RfrNotes rfrNotes) {
        RfrNotesDto rfrNotesDto = new RfrNotesDto();
        rfrNotesDto.setNotesId(rfrNotes.getId());
        rfrNotesDto.setNotes(rfrNotes.getNotes());
        rfrNotesDto.setActionItemId(rfrNotes.getActionItem().getId());
        rfrNotesDto.setChangeTracking(rfrNotes.getChangeTracking());
        return rfrNotesDto;
    }

    public int getNotesId() {
        return notesId;
    }

    public void setNotesId(int notesId) {
        this.notesId = notesId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getActionItemId() {
        return actionItemId;
    }

    public void setActionItemId(int actionItemId) {
        this.actionItemId = actionItemId;
    }

    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }
}
