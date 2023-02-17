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
 * A Author.
 */
@Table("author")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Author implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("email")
    private String email;

    @NotNull(message = "must not be null")
    @Column("password")
    private String password;

    @NotNull(message = "must not be null")
    @Column("create_date")
    private Instant createDate;

    @Column("avatar_url")
    private String avatarUrl;

    @NotNull(message = "must not be null")
    @Column("activated")
    private Boolean activated;

    @Column("lang_key")
    private String langKey;

    @Column("reset_date")
    private Instant resetDate;

    @Column("reset_key")
    private String resetKey;

    @NotNull(message = "must not be null")
    @Column("authorities")
    private String authorities;

    @Column("created_by")
    private String createdBy;

    @Column("last_modified_by")
    private String lastModifiedBy;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @Column("course_id")
    private String courseId;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Author id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Author firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Author lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Author email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public Author password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Author createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public Author avatarUrl(String avatarUrl) {
        this.setAvatarUrl(avatarUrl);
        return this;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Author activated(Boolean activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return this.langKey;
    }

    public Author langKey(String langKey) {
        this.setLangKey(langKey);
        return this;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Instant getResetDate() {
        return this.resetDate;
    }

    public Author resetDate(Instant resetDate) {
        this.setResetDate(resetDate);
        return this;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public String getResetKey() {
        return this.resetKey;
    }

    public Author resetKey(String resetKey) {
        this.setResetKey(resetKey);
        return this;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public String getAuthorities() {
        return this.authorities;
    }

    public Author authorities(String authorities) {
        this.setAuthorities(authorities);
        return this;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Author createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Author lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Author lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Author courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Author setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author)) {
            return false;
        }
        return id != null && id.equals(((Author) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Author{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", avatarUrl='" + getAvatarUrl() + "'" +
            ", activated='" + getActivated() + "'" +
            ", langKey='" + getLangKey() + "'" +
            ", resetDate='" + getResetDate() + "'" +
            ", resetKey='" + getResetKey() + "'" +
            ", authorities='" + getAuthorities() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", courseId='" + getCourseId() + "'" +
            "}";
    }
}
