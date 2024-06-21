package com.opl.api.service.dto;

import com.opl.api.domain.enumeration.Status;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.opl.api.domain.BlogItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlogItemDTO implements Serializable {

    private Long id;

    private String title;

    private String subtitle;

    private ZonedDateTime publishDate;

    private String publishedBy;

    private Status status;

    private String approvedBy;

    private ZonedDateTime approvedDate;

    private String authors;

    private String jumbotronAltText;

    @Lob
    private String body;

    private MediaAssetDTO jumbotronImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public ZonedDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public ZonedDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(ZonedDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getJumbotronAltText() {
        return jumbotronAltText;
    }

    public void setJumbotronAltText(String jumbotronAltText) {
        this.jumbotronAltText = jumbotronAltText;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MediaAssetDTO getJumbotronImage() {
        return jumbotronImage;
    }

    public void setJumbotronImage(MediaAssetDTO jumbotronImage) {
        this.jumbotronImage = jumbotronImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlogItemDTO)) {
            return false;
        }

        BlogItemDTO blogItemDTO = (BlogItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, blogItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlogItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", subtitle='" + getSubtitle() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", publishedBy='" + getPublishedBy() + "'" +
            ", status='" + getStatus() + "'" +
            ", approvedBy='" + getApprovedBy() + "'" +
            ", approvedDate='" + getApprovedDate() + "'" +
            ", authors='" + getAuthors() + "'" +
            ", jumbotronAltText='" + getJumbotronAltText() + "'" +
            ", body='" + getBody() + "'" +
            ", jumbotronImage=" + getJumbotronImage() +
            "}";
    }
}
