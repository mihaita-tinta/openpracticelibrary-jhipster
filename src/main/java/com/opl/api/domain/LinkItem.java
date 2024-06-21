package com.opl.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A LinkItem.
 */
@Entity
@Table(name = "link_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LinkItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "sort_index")
    private Integer sortIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "coverImage", "tags", "links", "images" }, allowSetters = true)
    private PracticeItem practiceItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LinkItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public LinkItem url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSortIndex() {
        return this.sortIndex;
    }

    public LinkItem sortIndex(Integer sortIndex) {
        this.setSortIndex(sortIndex);
        return this;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public PracticeItem getPracticeItem() {
        return this.practiceItem;
    }

    public void setPracticeItem(PracticeItem practiceItem) {
        this.practiceItem = practiceItem;
    }

    public LinkItem practiceItem(PracticeItem practiceItem) {
        this.setPracticeItem(practiceItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LinkItem)) {
            return false;
        }
        return getId() != null && getId().equals(((LinkItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LinkItem{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", sortIndex=" + getSortIndex() +
            "}";
    }
}
