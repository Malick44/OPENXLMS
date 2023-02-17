package com.opnlms.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Rating.
 */
@Table("rating")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rating implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private String id;

    @Column("user_id")
    private String userId;

    @Column("course_id")
    private String courseId;

    @Column("instractor_id")
    private String instractorId;

    @Column("value")
    private Integer value;

    @Column("comment")
    private String comment;

    @Column("timestamp")
    private Instant timestamp;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Rating id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public Rating userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Rating courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getInstractorId() {
        return this.instractorId;
    }

    public Rating instractorId(String instractorId) {
        this.setInstractorId(instractorId);
        return this;
    }

    public void setInstractorId(String instractorId) {
        this.instractorId = instractorId;
    }

    public Integer getValue() {
        return this.value;
    }

    public Rating value(Integer value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getComment() {
        return this.comment;
    }

    public Rating comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Rating timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Rating setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rating)) {
            return false;
        }
        return id != null && id.equals(((Rating) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", instractorId='" + getInstractorId() + "'" +
            ", value=" + getValue() +
            ", comment='" + getComment() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
