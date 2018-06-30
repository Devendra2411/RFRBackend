package com.ge.rfr.actionitem.model.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.ge.rfr.actionitem.model.Category;
import com.ge.rfr.actionitem.model.Owner;
import com.ge.rfr.actionitem.model.RfrActionItem;
import com.ge.rfr.actionitem.model.Status;
import com.ge.rfr.actionitem.model.TaskType;
import com.ge.rfr.actionitem.upload.model.dto.ActionItemUploadDto;
import com.ge.rfr.common.model.ChangeTracking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Dto for Action Items")
public class RfrActionItemDto {

    @ApiModelProperty(notes = "Id for Action Items")
    private int actionItemId;

    @NotNull
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
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Task Type")
    private TaskType taskType;

    @ApiModelProperty(notes = "User Details", hidden = true)
    private ChangeTracking changeTracking;

    @ApiModelProperty(notes = "Workflow Id")
    private int workflowId;

    private List<ActionItemUploadDto> uploadedFiles = new ArrayList<>();

    public static RfrActionItemDto valueOf(RfrActionItem rfrActionItem) {
        RfrActionItemDto result = new RfrActionItemDto();
        result.setItemTitle(rfrActionItem.getItemTitle());
        result.setCategory(rfrActionItem.getCategory());
        result.setDueDate(rfrActionItem.getDueDate());
        result.setActionItemId(rfrActionItem.getId());
        result.setLevelValue(rfrActionItem.getLevelValue());
        result.setOwner(rfrActionItem.getOwner());
        result.setStatus(rfrActionItem.getStatus());
        result.setTaskType(rfrActionItem.getTaskType());
        result.setChangeTracking(rfrActionItem.getChangeTracking());
        result.setWorkflowId(rfrActionItem.getWorkflow().getId());
        result.setUploadedFiles(rfrActionItem.getUploadedFiles().stream().map(ActionItemUploadDto::valueOf)
                .collect(Collectors.toList()));
        return result;
    }

    public int getActionItemId() {
        return actionItemId;
    }

    public void setActionItemId(int actionItemId) {
        this.actionItemId = actionItemId;
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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public List<ActionItemUploadDto> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<ActionItemUploadDto> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

}
