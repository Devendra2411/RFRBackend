package com.ge.rfr.actionitem.upload.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ge.rfr.actionitem.model.RfrActionItem;
import com.ge.rfr.common.model.ChangeTracking;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "rfr_action_item_files")
public class ActionItemUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id for File")
    private int id;

    @NotNull
    @Size(min = 1, max = 255)
    @ApiModelProperty(notes = "Name of the File")
    private String fileName;

    @NotNull
    private String filePath;

    @NotNull
    private String fileId;

    private long fileSize;

    private ChangeTracking changeTracking;

    @ManyToOne
    @JoinColumn(name = "action_item_id")
    @NotNull
    @ApiModelProperty(notes = "Rfr Action Item")
    private RfrActionItem actionItem;

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

    public ChangeTracking getChangeTracking() {
        return changeTracking;
    }

    public void setChangeTracking(ChangeTracking changeTracking) {
        this.changeTracking = changeTracking;
    }

    public RfrActionItem getActionItem() {
        return actionItem;
    }

    public void setActionItem(RfrActionItem actionItem) {
        this.actionItem = actionItem;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
