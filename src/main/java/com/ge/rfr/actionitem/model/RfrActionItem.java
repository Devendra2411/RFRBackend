package com.ge.rfr.actionitem.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ge.rfr.actionitem.upload.model.ActionItemUpload;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.workflow.model.RfrWorkflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "rfr_action_items")
@ApiModel(description = "Entity for Action Items")
public class RfrActionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id for Action Items")
    private int id;

    @NotNull
    @Size(min = 1, max = 2500)
    @ApiModelProperty(notes = "Title for the Item")
    private String itemTitle;

    @NotNull
    @ApiModelProperty(notes = "Due Date")
    private Instant dueDate;

    @NotNull
    @ApiModelProperty(notes = "Level Value")
    private int levelValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Status of the Action Item")
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Action Item Category")
    private Category category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Owner")
    private Owner owner;

    @NotNull
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "Type of the Item")
    private String typeOfItem;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Task Type")
    private TaskType taskType;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "workflow_id")
    @NotNull
    @ApiModelProperty(notes = "Rfr Workflow")
    private RfrWorkflow workflow;

    @OneToMany(mappedBy = "actionItem", cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "List of File Uploaded for Action Item")
    private List<ActionItemUpload> uploadedFiles = new ArrayList<>();

    @Embedded
    @Valid
    @NotNull
    @ApiModelProperty(notes = "User Details", hidden = true)
    private ChangeTracking changeTracking;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public int getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(int levelValue) {
        this.levelValue = levelValue;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getTypeOfItem() {
        return typeOfItem;
    }

    public void setTypeOfItem(String typeOfItem) {
        this.typeOfItem = typeOfItem;
    }

    public RfrWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(RfrWorkflow workflow) {
        this.workflow = workflow;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public List<ActionItemUpload> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<ActionItemUpload> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }
}
