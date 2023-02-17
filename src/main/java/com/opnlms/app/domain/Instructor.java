package com.opnlms.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Instructor.
 */
@Table("instructor")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Instructor implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private String id;

    @Column("course_id")
    private String courseId;

    @Column("name")
    private String name;

    @Column("email")
    private String email;

    @Column("instructor_url")
    private String instructorUrl;

    @Column("instructor_thumbnail")
    private String instructorThumbnail;

    @Column("instructor_rating")
    private String instructorRating;

    @Column("instructor_rating_count")
    private String instructorRatingCount;

    @Column("instructor_total_students")
    private String instructorTotalStudents;

    @Column("instructor_total_reviews")
    private String instructorTotalReviews;

    @Column("rating_count")
    private String ratingCount;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Instructor id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Instructor courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return this.name;
    }

    public Instructor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Instructor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstructorUrl() {
        return this.instructorUrl;
    }

    public Instructor instructorUrl(String instructorUrl) {
        this.setInstructorUrl(instructorUrl);
        return this;
    }

    public void setInstructorUrl(String instructorUrl) {
        this.instructorUrl = instructorUrl;
    }

    public String getInstructorThumbnail() {
        return this.instructorThumbnail;
    }

    public Instructor instructorThumbnail(String instructorThumbnail) {
        this.setInstructorThumbnail(instructorThumbnail);
        return this;
    }

    public void setInstructorThumbnail(String instructorThumbnail) {
        this.instructorThumbnail = instructorThumbnail;
    }

    public String getInstructorRating() {
        return this.instructorRating;
    }

    public Instructor instructorRating(String instructorRating) {
        this.setInstructorRating(instructorRating);
        return this;
    }

    public void setInstructorRating(String instructorRating) {
        this.instructorRating = instructorRating;
    }

    public String getInstructorRatingCount() {
        return this.instructorRatingCount;
    }

    public Instructor instructorRatingCount(String instructorRatingCount) {
        this.setInstructorRatingCount(instructorRatingCount);
        return this;
    }

    public void setInstructorRatingCount(String instructorRatingCount) {
        this.instructorRatingCount = instructorRatingCount;
    }

    public String getInstructorTotalStudents() {
        return this.instructorTotalStudents;
    }

    public Instructor instructorTotalStudents(String instructorTotalStudents) {
        this.setInstructorTotalStudents(instructorTotalStudents);
        return this;
    }

    public void setInstructorTotalStudents(String instructorTotalStudents) {
        this.instructorTotalStudents = instructorTotalStudents;
    }

    public String getInstructorTotalReviews() {
        return this.instructorTotalReviews;
    }

    public Instructor instructorTotalReviews(String instructorTotalReviews) {
        this.setInstructorTotalReviews(instructorTotalReviews);
        return this;
    }

    public void setInstructorTotalReviews(String instructorTotalReviews) {
        this.instructorTotalReviews = instructorTotalReviews;
    }

    public String getRatingCount() {
        return this.ratingCount;
    }

    public Instructor ratingCount(String ratingCount) {
        this.setRatingCount(ratingCount);
        return this;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Instructor setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instructor)) {
            return false;
        }
        return id != null && id.equals(((Instructor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Instructor{" +
            "id=" + getId() +
            ", courseId='" + getCourseId() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", instructorUrl='" + getInstructorUrl() + "'" +
            ", instructorThumbnail='" + getInstructorThumbnail() + "'" +
            ", instructorRating='" + getInstructorRating() + "'" +
            ", instructorRatingCount='" + getInstructorRatingCount() + "'" +
            ", instructorTotalStudents='" + getInstructorTotalStudents() + "'" +
            ", instructorTotalReviews='" + getInstructorTotalReviews() + "'" +
            ", ratingCount='" + getRatingCount() + "'" +
            "}";
    }
}
