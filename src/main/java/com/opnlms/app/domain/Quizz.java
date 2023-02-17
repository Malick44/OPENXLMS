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
 * A Quizz.
 */
@Table("quizz")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quizz implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("user_id")
    private String userId;

    @NotNull(message = "must not be null")
    @Column("course_id")
    private String courseId;

    @Column("title")
    private String title;

    @Column("section_id")
    private String sectionId;

    @NotNull(message = "must not be null")
    @Column("exam_date")
    private LocalDate examDate;

    @Column("number_of_questions")
    private Integer numberOfQuestions;

    @Column("time_limit")
    private Integer timeLimit;

    @Column("score")
    private Integer score;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Quizz id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public Quizz userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Quizz courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return this.title;
    }

    public Quizz title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSectionId() {
        return this.sectionId;
    }

    public Quizz sectionId(String sectionId) {
        this.setSectionId(sectionId);
        return this;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public LocalDate getExamDate() {
        return this.examDate;
    }

    public Quizz examDate(LocalDate examDate) {
        this.setExamDate(examDate);
        return this;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public Integer getNumberOfQuestions() {
        return this.numberOfQuestions;
    }

    public Quizz numberOfQuestions(Integer numberOfQuestions) {
        this.setNumberOfQuestions(numberOfQuestions);
        return this;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public Integer getTimeLimit() {
        return this.timeLimit;
    }

    public Quizz timeLimit(Integer timeLimit) {
        this.setTimeLimit(timeLimit);
        return this;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getScore() {
        return this.score;
    }

    public Quizz score(Integer score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Quizz setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quizz)) {
            return false;
        }
        return id != null && id.equals(((Quizz) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quizz{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", title='" + getTitle() + "'" +
            ", sectionId='" + getSectionId() + "'" +
            ", examDate='" + getExamDate() + "'" +
            ", numberOfQuestions=" + getNumberOfQuestions() +
            ", timeLimit=" + getTimeLimit() +
            ", score=" + getScore() +
            "}";
    }
}
