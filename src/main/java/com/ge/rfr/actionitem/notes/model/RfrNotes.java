package com.ge.rfr.actionitem.notes.model;

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

import org.hibernate.validator.constraints.NotBlank;

import com.ge.rfr.actionitem.model.RfrActionItem;
import com.ge.rfr.common.model.ChangeTracking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "rfr_action_item_notes")
@ApiModel(description = "Entity for Action Item Notes")
public class RfrNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Action Item Notes Id")
    private int id;

    @NotBlank
    @ApiModelProperty(notes = "Action Item Notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "action_items_id")
    @ApiModelProperty(notes = "Rfr Action Item")
    private RfrActionItem actionItem;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public RfrActionItem getActionItem() {
        return actionItem;
    }

    public void setActionItem(RfrActionItem actionItem) {
        this.actionItem = actionItem;
    }

    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }
}
