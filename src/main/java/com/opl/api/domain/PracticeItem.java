package com.opl.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opl.api.domain.enumeration.FacilitationDifficulty;
import com.opl.api.domain.enumeration.MobiusLoopTag;
import com.opl.api.domain.enumeration.Status;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A PracticeItem.
 */
@Entity
@Table(name = "practice_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PracticeItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "objective")
    private String objective;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "facilitation_difficulty")
    private FacilitationDifficulty facilitationDifficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "mobius_loop_tag")
    private MobiusLoopTag mobiusLoopTag;

    @Lob
    @Column(name = "what")
    private String what;

    @Lob
    @Column(name = "why")
    private String why;

    @Lob
    @Column(name = "how")
    private String how;

    @Column(name = "number_of_people_required")
    private String numberOfPeopleRequired;

    @Column(name = "time_length")
    private String timeLength;

    @Column(name = "expected_participants")
    private String expectedParticipants;

    @JsonIgnoreProperties(value = { "practices", "practiceItem", "blogItem", "pageItem" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MediaAsset coverImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "practiceItem")
    @JsonIgnoreProperties(value = { "practiceItem" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "practiceItem")
    @JsonIgnoreProperties(value = { "practiceItem" }, allowSetters = true)
    private Set<LinkItem> links = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "practices")
    @JsonIgnoreProperties(value = { "practices", "practiceItem", "blogItem", "pageItem" }, allowSetters = true)
    private Set<MediaAsset> images = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PracticeItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public PracticeItem title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObjective() {
        return this.objective;
    }

    public PracticeItem objective(String objective) {
        this.setObjective(objective);
        return this;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public ZonedDateTime getPublishDate() {
        return this.publishDate;
    }

    public PracticeItem publishDate(ZonedDateTime publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishedBy() {
        return this.publishedBy;
    }

    public PracticeItem publishedBy(String publishedBy) {
        this.setPublishedBy(publishedBy);
        return this;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public Status getStatus() {
        return this.status;
    }

    public PracticeItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return this.approvedBy;
    }

    public PracticeItem approvedBy(String approvedBy) {
        this.setApprovedBy(approvedBy);
        return this;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public ZonedDateTime getApprovedDate() {
        return this.approvedDate;
    }

    public PracticeItem approvedDate(ZonedDateTime approvedDate) {
        this.setApprovedDate(approvedDate);
        return this;
    }

    public void setApprovedDate(ZonedDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getAuthors() {
        return this.authors;
    }

    public PracticeItem authors(String authors) {
        this.setAuthors(authors);
        return this;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public FacilitationDifficulty getFacilitationDifficulty() {
        return this.facilitationDifficulty;
    }

    public PracticeItem facilitationDifficulty(FacilitationDifficulty facilitationDifficulty) {
        this.setFacilitationDifficulty(facilitationDifficulty);
        return this;
    }

    public void setFacilitationDifficulty(FacilitationDifficulty facilitationDifficulty) {
        this.facilitationDifficulty = facilitationDifficulty;
    }

    public MobiusLoopTag getMobiusLoopTag() {
        return this.mobiusLoopTag;
    }

    public PracticeItem mobiusLoopTag(MobiusLoopTag mobiusLoopTag) {
        this.setMobiusLoopTag(mobiusLoopTag);
        return this;
    }

    public void setMobiusLoopTag(MobiusLoopTag mobiusLoopTag) {
        this.mobiusLoopTag = mobiusLoopTag;
    }

    public String getWhat() {
        return this.what;
    }

    public PracticeItem what(String what) {
        this.setWhat(what);
        return this;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhy() {
        return this.why;
    }

    public PracticeItem why(String why) {
        this.setWhy(why);
        return this;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public String getHow() {
        return this.how;
    }

    public PracticeItem how(String how) {
        this.setHow(how);
        return this;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getNumberOfPeopleRequired() {
        return this.numberOfPeopleRequired;
    }

    public PracticeItem numberOfPeopleRequired(String numberOfPeopleRequired) {
        this.setNumberOfPeopleRequired(numberOfPeopleRequired);
        return this;
    }

    public void setNumberOfPeopleRequired(String numberOfPeopleRequired) {
        this.numberOfPeopleRequired = numberOfPeopleRequired;
    }

    public String getTimeLength() {
        return this.timeLength;
    }

    public PracticeItem timeLength(String timeLength) {
        this.setTimeLength(timeLength);
        return this;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getExpectedParticipants() {
        return this.expectedParticipants;
    }

    public PracticeItem expectedParticipants(String expectedParticipants) {
        this.setExpectedParticipants(expectedParticipants);
        return this;
    }

    public void setExpectedParticipants(String expectedParticipants) {
        this.expectedParticipants = expectedParticipants;
    }

    public MediaAsset getCoverImage() {
        return this.coverImage;
    }

    public void setCoverImage(MediaAsset mediaAsset) {
        this.coverImage = mediaAsset;
    }

    public PracticeItem coverImage(MediaAsset mediaAsset) {
        this.setCoverImage(mediaAsset);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setPracticeItem(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setPracticeItem(this));
        }
        this.tags = tags;
    }

    public PracticeItem tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public PracticeItem addTags(Tag tag) {
        this.tags.add(tag);
        tag.setPracticeItem(this);
        return this;
    }

    public PracticeItem removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.setPracticeItem(null);
        return this;
    }

    public Set<LinkItem> getLinks() {
        return this.links;
    }

    public void setLinks(Set<LinkItem> linkItems) {
        if (this.links != null) {
            this.links.forEach(i -> i.setPracticeItem(null));
        }
        if (linkItems != null) {
            linkItems.forEach(i -> i.setPracticeItem(this));
        }
        this.links = linkItems;
    }

    public PracticeItem links(Set<LinkItem> linkItems) {
        this.setLinks(linkItems);
        return this;
    }

    public PracticeItem addLinks(LinkItem linkItem) {
        this.links.add(linkItem);
        linkItem.setPracticeItem(this);
        return this;
    }

    public PracticeItem removeLinks(LinkItem linkItem) {
        this.links.remove(linkItem);
        linkItem.setPracticeItem(null);
        return this;
    }

    public Set<MediaAsset> getImages() {
        return this.images;
    }

    public void setImages(Set<MediaAsset> mediaAssets) {
        if (this.images != null) {
            this.images.forEach(i -> i.setPractices(null));
        }
        if (mediaAssets != null) {
            mediaAssets.forEach(i -> i.setPractices(this));
        }
        this.images = mediaAssets;
    }

    public PracticeItem images(Set<MediaAsset> mediaAssets) {
        this.setImages(mediaAssets);
        return this;
    }

    public PracticeItem addImages(MediaAsset mediaAsset) {
        this.images.add(mediaAsset);
        mediaAsset.setPractices(this);
        return this;
    }

    public PracticeItem removeImages(MediaAsset mediaAsset) {
        this.images.remove(mediaAsset);
        mediaAsset.setPractices(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PracticeItem)) {
            return false;
        }
        return getId() != null && getId().equals(((PracticeItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PracticeItem{" +
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
            "}";
    }
}
