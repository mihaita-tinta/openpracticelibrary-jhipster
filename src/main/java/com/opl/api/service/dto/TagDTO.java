package com.opl.api.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.opl.api.domain.Tag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagDTO implements Serializable {

    private Long id;

    private String content;

    private PracticeItemDTO practiceItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        if (!(o instanceof TagDTO)) {
            return false;
        }

        TagDTO tagDTO = (TagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", practiceItem=" + getPracticeItem() +
            "}";
    }
}
