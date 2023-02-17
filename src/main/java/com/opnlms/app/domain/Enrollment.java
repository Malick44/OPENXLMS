package com.opnlms.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Enrollment.
 */
@Table("enrollment")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enrollment implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("course_id")
    private String courseId;

    @NotNull(message = "must not be null")
    @Column("user_id")
    private String userId;

    @NotNull(message = "must not be null")
    @Column("enrolled_date")
    private LocalDate enrolledDate;

    @Column("completion_rate")
    private Integer completionRate;

    @Column("completed_date")
    private LocalDate completedDate;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Enrollment id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Enrollment courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return this.userId;
    }

    public Enrollment userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getEnrolledDate() {
        return this.enrolledDate;
    }

    public Enrollment enrolledDate(LocalDate enrolledDate) {
        this.setEnrolledDate(enrolledDate);
        return this;
    }

    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public Integer getCompletionRate() {
        return this.completionRate;
    }

    public Enrollment completionRate(Integer completionRate) {
        this.setCompletionRate(completionRate);
        return this;
    }

    public void setCompletionRate(Integer completionRate) {
        this.completionRate = completionRate;
    }

    public LocalDate getCompletedDate() {
        return this.completedDate;
    }

    public Enrollment completedDate(LocalDate completedDate) {
        this.setCompletedDate(completedDate);
        return this;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Enrollment setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enrollment)) {
            return false;
        }
        return id != null && id.equals(((Enrollment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enrollment{" +
            "id=" + getId() +
            ", courseId='" + getCourseId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", enrolledDate='" + getEnrolledDate() + "'" +
            ", completionRate=" + getCompletionRate() +
            ", completedDate='" + getCompletedDate() + "'" +
            "}";
    }
}
