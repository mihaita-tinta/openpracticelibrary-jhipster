package com.opl.api.domain;

import static com.opl.api.domain.LinkItemTestSamples.*;
import static com.opl.api.domain.MediaAssetTestSamples.*;
import static com.opl.api.domain.PracticeItemTestSamples.*;
import static com.opl.api.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PracticeItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PracticeItem.class);
        PracticeItem practiceItem1 = getPracticeItemSample1();
        PracticeItem practiceItem2 = new PracticeItem();
        assertThat(practiceItem1).isNotEqualTo(practiceItem2);

        practiceItem2.setId(practiceItem1.getId());
        assertThat(practiceItem1).isEqualTo(practiceItem2);

        practiceItem2 = getPracticeItemSample2();
        assertThat(practiceItem1).isNotEqualTo(practiceItem2);
    }

    @Test
    void coverImageTest() {
        PracticeItem practiceItem = getPracticeItemRandomSampleGenerator();
        MediaAsset mediaAssetBack = getMediaAssetRandomSampleGenerator();

        practiceItem.setCoverImage(mediaAssetBack);
        assertThat(practiceItem.getCoverImage()).isEqualTo(mediaAssetBack);

        practiceItem.coverImage(null);
        assertThat(practiceItem.getCoverImage()).isNull();
    }

    @Test
    void tagsTest() {
        PracticeItem practiceItem = getPracticeItemRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        practiceItem.addTags(tagBack);
        assertThat(practiceItem.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getPracticeItem()).isEqualTo(practiceItem);

        practiceItem.removeTags(tagBack);
        assertThat(practiceItem.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getPracticeItem()).isNull();

        practiceItem.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(practiceItem.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getPracticeItem()).isEqualTo(practiceItem);

        practiceItem.setTags(new HashSet<>());
        assertThat(practiceItem.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getPracticeItem()).isNull();
    }

    @Test
    void linksTest() {
        PracticeItem practiceItem = getPracticeItemRandomSampleGenerator();
        LinkItem linkItemBack = getLinkItemRandomSampleGenerator();

        practiceItem.addLinks(linkItemBack);
        assertThat(practiceItem.getLinks()).containsOnly(linkItemBack);
        assertThat(linkItemBack.getPracticeItem()).isEqualTo(practiceItem);

        practiceItem.removeLinks(linkItemBack);
        assertThat(practiceItem.getLinks()).doesNotContain(linkItemBack);
        assertThat(linkItemBack.getPracticeItem()).isNull();

        practiceItem.links(new HashSet<>(Set.of(linkItemBack)));
        assertThat(practiceItem.getLinks()).containsOnly(linkItemBack);
        assertThat(linkItemBack.getPracticeItem()).isEqualTo(practiceItem);

        practiceItem.setLinks(new HashSet<>());
        assertThat(practiceItem.getLinks()).doesNotContain(linkItemBack);
        assertThat(linkItemBack.getPracticeItem()).isNull();
    }

    @Test
    void imagesTest() {
        PracticeItem practiceItem = getPracticeItemRandomSampleGenerator();
        MediaAsset mediaAssetBack = getMediaAssetRandomSampleGenerator();

        practiceItem.addImages(mediaAssetBack);
        assertThat(practiceItem.getImages()).containsOnly(mediaAssetBack);
        assertThat(mediaAssetBack.getPractices()).isEqualTo(practiceItem);

        practiceItem.removeImages(mediaAssetBack);
        assertThat(practiceItem.getImages()).doesNotContain(mediaAssetBack);
        assertThat(mediaAssetBack.getPractices()).isNull();

        practiceItem.images(new HashSet<>(Set.of(mediaAssetBack)));
        assertThat(practiceItem.getImages()).containsOnly(mediaAssetBack);
        assertThat(mediaAssetBack.getPractices()).isEqualTo(practiceItem);

        practiceItem.setImages(new HashSet<>());
        assertThat(practiceItem.getImages()).doesNotContain(mediaAssetBack);
        assertThat(mediaAssetBack.getPractices()).isNull();
    }
}
