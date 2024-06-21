package com.opl.api.service.dto;

import com.opl.api.domain.enumeration.FacilitationDifficulty;
import com.opl.api.domain.enumeration.MobiusLoopTag;
import com.opl.api.domain.enumeration.Status;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.opl.api.domain.PracticeItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PracticeItemDTO implements Serializable {

    private Long id;

    private String title;

    private String objective;

    private ZonedDateTime publishDate;

    private String publishedBy;

    private Status status;

    private String approvedBy;

    private ZonedDateTime approvedDate;

    private String authors;

    private FacilitationDifficulty facilitationDifficulty;

    private MobiusLoopTag mobiusLoopTag;

    @Lob
    private String what;

    @Lob
    private String why;

    @Lob
    private String how;

    private String numberOfPeopleRequired;

    private String timeLength;

    private String expectedParticipants;

    private MediaAssetDTO coverImage;

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

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public ZonedDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(ZonedDateTime publishDate) {
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

    public ZonedDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(ZonedDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public FacilitationDifficulty getFacilitationDifficulty() {
        return facilitationDifficulty;
    }

    public void setFacilitationDifficulty(FacilitationDifficulty facilitationDifficulty) {
        this.facilitationDifficulty = facilitationDifficulty;
    }

    public MobiusLoopTag getMobiusLoopTag() {
        return mobiusLoopTag;
    }

    public void setMobiusLoopTag(MobiusLoopTag mobiusLoopTag) {
        this.mobiusLoopTag = mobiusLoopTag;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getNumberOfPeopleRequired() {
        return numberOfPeopleRequired;
    }

    public void setNumberOfPeopleRequired(String numberOfPeopleRequired) {
        this.numberOfPeopleRequired = numberOfPeopleRequired;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getExpectedParticipants() {
        return expectedParticipants;
    }

    public void setExpectedParticipants(String expectedParticipants) {
        this.expectedParticipants = expectedParticipants;
    }

    public MediaAssetDTO getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(MediaAssetDTO coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PracticeItemDTO)) {
            return false;
        }

        PracticeItemDTO practiceItemDTO = (PracticeItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, practiceItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PracticeItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", objective='" + getObjective() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", publishedBy='" + getPublishedBy() + "'" +
            ", status='" + getStatus() + "'" +
            ", approvedBy='" + getApprovedBy() + "'" +
            ", approvedDate='" + getApprovedDate() + "'" +
            ", authors='" + getAuthors() + "'" +
            ", facilitationDifficulty='" + getFacilitationDifficulty() + "'" +
            ", mobiusLoopTag='" + getMobiusLoopTag() + "'" +
            ", what='" + getWhat() + "'" +
            ", why='" + getWhy() + "'" +
            ", how='" + getHow() + "'" +
            ", numberOfPeopleRequired='" + getNumberOfPeopleRequired() + "'" +
            ", timeLength='" + getTimeLength() + "'" +
            ", expectedParticipants='" + getExpectedParticipants() + "'" +
            ", coverImage=" + getCoverImage() +
            "}";
    }
}
