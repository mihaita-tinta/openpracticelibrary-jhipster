package com.opl.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opl.api.domain.enumeration.MediaAssetType;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A MediaAsset.
 */
@Entity
@Table(name = "media_asset")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MediaAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private MediaAssetType type;

    @Lob
    @Column(name = "content")
    private byte[] content;

    @Column(name = "content_content_type")
    private String contentContentType;

    @Column(name = "sort_index")
    private Integer sortIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "coverImage", "tags", "links", "images" }, allowSetters = true)
    private PracticeItem practices;

    @JsonIgnoreProperties(value = { "coverImage", "tags", "links", "images" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "coverImage")
    private PracticeItem practiceItem;

    @JsonIgnoreProperties(value = { "jumbotronImage" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "jumbotronImage")
    private BlogItem blogItem;

    @JsonIgnoreProperties(value = { "jumbotronImage" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "jumbotronImage")
    private PageItem pageItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MediaAsset id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaAssetType getType() {
        return this.type;
    }

    public MediaAsset type(MediaAssetType type) {
        this.setType(type);
        return this;
    }

    public void setType(MediaAssetType type) {
        this.type = type;
    }

    public byte[] getContent() {
        return this.content;
    }

    public MediaAsset content(byte[] content) {
        this.setContent(content);
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return this.contentContentType;
    }

    public MediaAsset contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Integer getSortIndex() {
        return this.sortIndex;
    }

    public MediaAsset sortIndex(Integer sortIndex) {
        this.setSortIndex(sortIndex);
        return this;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public PracticeItem getPractices() {
        return this.practices;
    }

    public void setPractices(PracticeItem practiceItem) {
        this.practices = practiceItem;
    }

    public MediaAsset practices(PracticeItem practiceItem) {
        this.setPractices(practiceItem);
        return this;
    }

    public PracticeItem getPracticeItem() {
        return this.practiceItem;
    }

    public void setPracticeItem(PracticeItem practiceItem) {
        if (this.practiceItem != null) {
            this.practiceItem.setCoverImage(null);
        }
        if (practiceItem != null) {
            practiceItem.setCoverImage(this);
        }
        this.practiceItem = practiceItem;
    }

    public MediaAsset practiceItem(PracticeItem practiceItem) {
        this.setPracticeItem(practiceItem);
        return this;
    }

    public BlogItem getBlogItem() {
        return this.blogItem;
    }

    public void setBlogItem(BlogItem blogItem) {
        if (this.blogItem != null) {
            this.blogItem.setJumbotronImage(null);
        }
        if (blogItem != null) {
            blogItem.setJumbotronImage(this);
        }
        this.blogItem = blogItem;
    }

    public MediaAsset blogItem(BlogItem blogItem) {
        this.setBlogItem(blogItem);
        return this;
    }

    public PageItem getPageItem() {
        return this.pageItem;
    }

    public void setPageItem(PageItem pageItem) {
        if (this.pageItem != null) {
            this.pageItem.setJumbotronImage(null);
        }
        if (pageItem != null) {
            pageItem.setJumbotronImage(this);
        }
        this.pageItem = pageItem;
    }

    public MediaAsset pageItem(PageItem pageItem) {
        this.setPageItem(pageItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaAsset)) {
            return false;
        }
        return getId() != null && getId().equals(((MediaAsset) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaAsset{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            ", sortIndex=" + getSortIndex() +
            "}";
    }
}
