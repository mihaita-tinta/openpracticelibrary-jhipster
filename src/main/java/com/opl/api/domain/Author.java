package com.opl.api.domain;

import com.opl.api.domain.enumeration.Status;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Author.
 */
@Entity
@Table(name = "author")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "github_username")
    private String githubUsername;

    @Lob
    @Column(name = "about_you")
    private String aboutYou;

    @Column(name = "location")
    private String location;

    @Column(name = "website")
    private String website;

    @Column(name = "publish_date")
    private Instant publishDate;

    @Column(name = "published_by")
    private String publishedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    private Instant approvedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Author id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Author name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubUsername() {
        return this.githubUsername;
    }

    public Author githubUsername(String githubUsername) {
        this.setGithubUsername(githubUsername);
        return this;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public String getAboutYou() {
        return this.aboutYou;
    }

    public Author aboutYou(String aboutYou) {
        this.setAboutYou(aboutYou);
        return this;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public String getLocation() {
        return this.location;
    }

    public Author location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return this.website;
    }

    public Author website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Instant getPublishDate() {
        return this.publishDate;
    }

    public Author publishDate(Instant publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishedBy() {
        return this.publishedBy;
    }

    public Author publishedBy(String publishedBy) {
        this.setPublishedBy(publishedBy);
        return this;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public Status getStatus() {
        return this.status;
    }

    public Author status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return this.approvedBy;
    }

    public Author approvedBy(String approvedBy) {
        this.setApprovedBy(approvedBy);
        return this;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getApprovedDate() {
        return this.approvedDate;
    }

    public Author approvedDate(Instant approvedDate) {
        this.setApprovedDate(approvedDate);
        return this;
    }

    public void setApprovedDate(Instant approvedDate) {
        this.approvedDate = approvedDate;
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
        return getId() != null && getId().equals(((Author) o).getId());
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
