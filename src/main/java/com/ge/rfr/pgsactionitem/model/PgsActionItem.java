package com.ge.rfr.pgsactionitem.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ge.rfr.actionitem.model.AttributeType;
import com.ge.rfr.actionitem.model.Category;
import com.ge.rfr.actionitem.model.Owner;
import com.ge.rfr.actionitem.model.Status;
import com.ge.rfr.actionitem.model.TaskType;

@Entity
@Table(name = "pgs_action_items")
public class PgsActionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String itemTitle;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Owner owner;

    @NotNull
    private String typeOfItem;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AttributeType attributeType;

    @NotNull
    private String attributeValue;

    private Instant calDate;

    private int refDateId;

    private int spanFromRef;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Instant getCalDate() {
        return calDate;
    }

    public void setCalDate(Instant calDate) {
        this.calDate = calDate;
    }

    public int getRefDateId() {
        return refDateId;
    }

    public void setRefDateId(int refDateId) {
        this.refDateId = refDateId;
    }

    public int getSpanFromRef() {
        return spanFromRef;
    }

    public void setSpanFromRef(int spanFromRef) {
        this.spanFromRef = spanFromRef;
    }
}
