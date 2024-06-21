package com.opl.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opl.api.domain.enumeration.Status;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A BlogItem.
 */
@Entity
@Table(name = "blog_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlogItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "publish_date")
    private ZonedDateTime publishDate;

    @Column(name = "published_by")
    private String publishedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    private ZonedDateTime approvedDate;

    @Column(name = "authors")
    private String authors;

    @Column(name = "jumbotron_alt_text")
    private String jumbotronAltText;

    @Lob
    @Column(name = "jhi_body")
    private String body;

    @JsonIgnoreProperties(value = { "practices", "practiceItem", "blogItem", "pageItem" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MediaAsset jumbotronImage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BlogItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public BlogItem title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public BlogItem subtitle(String subtitle) {
        this.setSubtitle(subtitle);
        return this;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public ZonedDateTime getPublishDate() {
        return this.publishDate;
    }

    public BlogItem publishDate(ZonedDateTime publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishedBy() {
        return this.publishedBy;
    }

    public BlogItem publishedBy(String publishedBy) {
        this.setPublishedBy(publishedBy);
        return this;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public Status getStatus() {
        return this.status;
    }

    public BlogItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return this.approvedBy;
    }

    public BlogItem approvedBy(String approvedBy) {
        this.setApprovedBy(approvedBy);
        return this;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public ZonedDateTime getApprovedDate() {
        return this.approvedDate;
    }

    public BlogItem approvedDate(ZonedDateTime approvedDate) {
        this.setApprovedDate(approvedDate);
        return this;
    }

    public void setApprovedDate(ZonedDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getAuthors() {
        return this.authors;
    }

    public BlogItem authors(String authors) {
        this.setAuthors(authors);
        return this;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getJumbotronAltText() {
        return this.jumbotronAltText;
    }

    public BlogItem jumbotronAltText(String jumbotronAltText) {
        this.setJumbotronAltText(jumbotronAltText);
        return this;
    }

    public void setJumbotronAltText(String jumbotronAltText) {
        this.jumbotronAltText = jumbotronAltText;
    }

    public String getBody() {
        return this.body;
    }

    public BlogItem body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MediaAsset getJumbotronImage() {
        return this.jumbotronImage;
    }

    public void setJumbotronImage(MediaAsset mediaAsset) {
        this.jumbotronImage = mediaAsset;
    }

    public BlogItem jumbotronImage(MediaAsset mediaAsset) {
        this.setJumbotronImage(mediaAsset);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlogItem)) {
            return false;
        }
        return getId() != null && getId().equals(((BlogItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlogItem{" +
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
            "}";
    }
}
