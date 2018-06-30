package com.ge.rfr.common.model;

import com.ge.rfr.auth.SsoUser;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Embeddable
public class ChangeTracking {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "sso", column = @Column(name = "created_by_sso")),
            @AttributeOverride(name = "firstName", column = @Column(name = "created_by_firstname")),
            @AttributeOverride(name = "lastName", column = @Column(name = "created_by_lastname"))
    })
    @NotNull
    @Valid
    private User createdBy;

    @NotNull
    private Instant createdDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "sso", column = @Column(name = "modified_by_sso")),
            @AttributeOverride(name = "firstName", column = @Column(name = "modified_by_firstname")),
            @AttributeOverride(name = "lastName", column = @Column(name = "modified_by_lastname"))
    })
    @NotNull
    @Valid
    private User modifiedBy;

    @NotNull
    private Instant modifiedDate;

    public ChangeTracking() {
    }

    public ChangeTracking(User createdBy) {
        this.createdBy = createdBy;
        this.createdDate = Instant.now();
        this.modifiedBy = createdBy;
        this.modifiedDate = this.createdDate;
    }

    public static ChangeTracking fromSsoUser(SsoUser user) {
        return new ChangeTracking(User.fromSsoUser(user));
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeTracking that = (ChangeTracking) o;

        return createdBy.equals(that.createdBy) &&
                createdDate.equals(that.createdDate) &&
                modifiedBy.equals(that.modifiedBy) &&
                modifiedDate.equals(that.modifiedDate);

    }

    @Override
    public int hashCode() {
        int result = createdBy.hashCode();
        result = 31 * result + createdDate.hashCode();
        result = 31 * result + modifiedBy.hashCode();
        result = 31 * result + modifiedDate.hashCode();
        return result;
    }

    public void update(SsoUser user) {
        modifiedBy = User.fromSsoUser(user);
        modifiedDate = Instant.now();
    }

}