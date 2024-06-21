package com.opl.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opl.api.domain.enumeration.Status;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A PageItem.
 */
@Entity
@Table(name = "page_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "authors")
    private String authors;

    @Column(name = "menu")
    private String menu;

    @Column(name = "menu_weight")
    private String menuWeight;

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

    public PageItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public PageItem title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return this.authors;
    }

    public PageItem authors(String authors) {
        this.setAuthors(authors);
        return this;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getMenu() {
        return this.menu;
    }

    public PageItem menu(String menu) {
        this.setMenu(menu);
        return this;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMenuWeight() {
        return this.menuWeight;
    }

    public PageItem menuWeight(String menuWeight) {
        this.setMenuWeight(menuWeight);
        return this;
    }

    public void setMenuWeight(String menuWeight) {
        this.menuWeight = menuWeight;
    }

    public Instant getPublishDate() {
        return this.publishDate;
    }

    public PageItem publishDate(Instant publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishedBy() {
        return this.publishedBy;
    }

    public PageItem publishedBy(String publishedBy) {
        this.setPublishedBy(publishedBy);
        return this;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public Status getStatus() {
        return this.status;
    }

    public PageItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return this.approvedBy;
    }

    public PageItem approvedBy(String approvedBy) {
        this.setApprovedBy(approvedBy);
        return this;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getApprovedDate() {
        return this.approvedDate;
    }

    public PageItem approvedDate(Instant approvedDate) {
        this.setApprovedDate(approvedDate);
        return this;
    }

    public void setApprovedDate(Instant approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getJumbotronAltText() {
        return this.jumbotronAltText;
    }

    public PageItem jumbotronAltText(String jumbotronAltText) {
        this.setJumbotronAltText(jumbotronAltText);
        return this;
    }

    public void setJumbotronAltText(String jumbotronAltText) {
        this.jumbotronAltText = jumbotronAltText;
    }

    public String getBody() {
        return this.body;
    }

    public PageItem body(String body) {
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

    public PageItem jumbotronImage(MediaAsset mediaAsset) {
        this.setJumbotronImage(mediaAsset);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageItem)) {
            return false;
        }
        return getId() != null && getId().equals(((PageItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageItem{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", authors='" + getAuthors() + "'" +
            ", menu='" + getMenu() + "'" +
            ", menuWeight='" + getMenuWeight() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", publishedBy='" + getPublishedBy() + "'" +
            ", status='" + getStatus() + "'" +
            ", approvedBy='" + getApprovedBy() + "'" +
            ", approvedDate='" + getApprovedDate() + "'" +
            ", jumbotronAltText='" + getJumbotronAltText() + "'" +
            ", body='" + getBody() + "'" +
            "}";
    }
}
