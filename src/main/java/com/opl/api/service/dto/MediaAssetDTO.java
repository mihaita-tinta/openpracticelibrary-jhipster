package com.opl.api.service.dto;

import com.opl.api.domain.enumeration.MediaAssetType;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.opl.api.domain.MediaAsset} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MediaAssetDTO implements Serializable {

    private Long id;

    private MediaAssetType type;

    @Lob
    private byte[] content;

    private String contentContentType;

    private Integer sortIndex;

    private PracticeItemDTO practices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaAssetType getType() {
        return type;
    }

    public void setType(MediaAssetType type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public PracticeItemDTO getPractices() {
        return practices;
    }

    public void setPractices(PracticeItemDTO practices) {
        this.practices = practices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaAssetDTO)) {
            return false;
        }

        MediaAssetDTO mediaAssetDTO = (MediaAssetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mediaAssetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaAssetDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", sortIndex=" + getSortIndex() +
            ", practices=" + getPractices() +
            "}";
    }
}
