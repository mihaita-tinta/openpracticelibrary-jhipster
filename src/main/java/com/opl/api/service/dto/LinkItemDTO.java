package com.opl.api.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.opl.api.domain.LinkItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LinkItemDTO implements Serializable {

    private Long id;

    private String url;

    private Integer sortIndex;

    private PracticeItemDTO practiceItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public PracticeItemDTO getPracticeItem() {
        return practiceItem;
    }

    public void setPracticeItem(PracticeItemDTO practiceItem) {
        this.practiceItem = practiceItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LinkItemDTO)) {
            return false;
        }

        LinkItemDTO linkItemDTO = (LinkItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, linkItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LinkItemDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", sortIndex=" + getSortIndex() +
            ", practiceItem=" + getPracticeItem() +
            "}";
    }
}
