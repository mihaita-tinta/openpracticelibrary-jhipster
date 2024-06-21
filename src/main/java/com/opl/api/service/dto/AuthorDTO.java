package com.opl.api.service.dto;

import com.opl.api.domain.enumeration.Status;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.opl.api.domain.Author} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorDTO implements Serializable {

    private Long id;

    private String name;

    private String githubUsername;

    @Lob
    private String aboutYou;

    private String location;

    private String website;

    private Instant publishDate;

    private String publishedBy;

    private Status status;

    private String approvedBy;

    private Instant approvedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public String getAboutYou() {
        return aboutYou;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Instant getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Instant publishDate) {
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

    public Instant getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Instant approvedDate) {
        this.approvedDate = approvedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorDTO)) {
            return false;
        }

        AuthorDTO authorDTO = (AuthorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", githubUsername='" + getGithubUsername() + "'" +
            ", aboutYou='" + getAboutYou() + "'" +
            ", location='" + getLocation() + "'" +
            ", website='" + getWebsite() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", publishedBy='" + getPublishedBy() + "'" +
            ", status='" + getStatus() + "'" +
            ", approvedBy='" + getApprovedBy() + "'" +
            ", approvedDate='" + getApprovedDate() + "'" +
            "}";
    }
}
