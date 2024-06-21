package com.opl.api.service.dto;

import com.opl.api.domain.enumeration.Status;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.opl.api.domain.PageItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageItemDTO implements Serializable {

    private Long id;

    private String title;

    private String authors;

    private String menu;

    private String menuWeight;

    private Instant publishDate;

    private String publishedBy;

    private Status status;

    private String approvedBy;

    private Instant approvedDate;

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

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMenuWeight() {
        return menuWeight;
    }

    public void setMenuWeight(String menuWeight) {
        this.menuWeight = menuWeight;
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
        if (!(o instanceof PageItemDTO)) {
            return false;
        }

        PageItemDTO pageItemDTO = (PageItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pageItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageItemDTO{" +
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
            ", jumbotronImage=" + getJumbotronImage() +
            "}";
    }
}
