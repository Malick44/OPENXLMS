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
 * A Section.
 */
@Table("section")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Section implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private String id;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("course_id")
    private String courseId;

    @Column("text")
    private String text;

    @Column("video_url")
    private String videoUrl;

    @Column("video_id")
    private String videoId;

    @Column("video_duration")
    private String videoDuration;

    @Column("video_title")
    private String videoTitle;

    @Column("video_description")
    private String videoDescription;

    @Column("video_channel_language")
    private String videoChannelLanguage;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Section id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Section title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Section description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Section courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getText() {
        return this.text;
    }

    public Section text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public Section videoUrl(String videoUrl) {
        this.setVideoUrl(videoUrl);
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public Section videoId(String videoId) {
        this.setVideoId(videoId);
        return this;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoDuration() {
        return this.videoDuration;
    }

    public Section videoDuration(String videoDuration) {
        this.setVideoDuration(videoDuration);
        return this;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public Section videoTitle(String videoTitle) {
        this.setVideoTitle(videoTitle);
        return this;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return this.videoDescription;
    }

    public Section videoDescription(String videoDescription) {
        this.setVideoDescription(videoDescription);
        return this;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoChannelLanguage() {
        return this.videoChannelLanguage;
    }

    public Section videoChannelLanguage(String videoChannelLanguage) {
        this.setVideoChannelLanguage(videoChannelLanguage);
        return this;
    }

    public void setVideoChannelLanguage(String videoChannelLanguage) {
        this.videoChannelLanguage = videoChannelLanguage;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Section setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        return id != null && id.equals(((Section) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Section{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", text='" + getText() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", videoId='" + getVideoId() + "'" +
            ", videoDuration='" + getVideoDuration() + "'" +
            ", videoTitle='" + getVideoTitle() + "'" +
            ", videoDescription='" + getVideoDescription() + "'" +
            ", videoChannelLanguage='" + getVideoChannelLanguage() + "'" +
            "}";
    }
}
