package com.ge.rfr.actionitem.upload.model.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ge.rfr.actionitem.upload.model.ActionItemUpload;
import com.ge.rfr.common.model.ChangeTracking;

import io.swagger.annotations.ApiModelProperty;

public class ActionItemUploadDto {

    @Id
    @ApiModelProperty(notes = "Id for Action Items")
    private int id;

    @NotNull
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "Name of the File")
    private String fileName;

    private String filePath;

    private long fileSize;

    private String fileId;

    private ChangeTracking changeTracking;

    private int actionItemId;

    public static ActionItemUploadDto valueOf(ActionItemUpload upload) {
        ActionItemUploadDto uploadDto = new ActionItemUploadDto();
        uploadDto.setActionItemId(upload.getActionItem().getId());
        uploadDto.setId(upload.getId());
        uploadDto.setFileName(upload.getFileName());
        uploadDto.setFilePath(upload.getFilePath());
        uploadDto.setFileSize(upload.getFileSize());
        uploadDto.setFileId(upload.getFileId());
        uploadDto.setChangeTracking(upload.getChangeTracking());
        return uploadDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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
