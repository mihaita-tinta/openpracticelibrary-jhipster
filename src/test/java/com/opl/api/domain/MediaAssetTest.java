package com.opl.api.domain;

import static com.opl.api.domain.BlogItemTestSamples.*;
import static com.opl.api.domain.MediaAssetTestSamples.*;
import static com.opl.api.domain.PageItemTestSamples.*;
import static com.opl.api.domain.PracticeItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MediaAssetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MediaAsset.class);
        MediaAsset mediaAsset1 = getMediaAssetSample1();
        MediaAsset mediaAsset2 = new MediaAsset();
        assertThat(mediaAsset1).isNotEqualTo(mediaAsset2);

        mediaAsset2.setId(mediaAsset1.getId());
        assertThat(mediaAsset1).isEqualTo(mediaAsset2);

        mediaAsset2 = getMediaAssetSample2();
        assertThat(mediaAsset1).isNotEqualTo(mediaAsset2);
    }

    @Test
    void practicesTest() {
        MediaAsset mediaAsset = getMediaAssetRandomSampleGenerator();
        PracticeItem practiceItemBack = getPracticeItemRandomSampleGenerator();

        mediaAsset.setPractices(practiceItemBack);
        assertThat(mediaAsset.getPractices()).isEqualTo(practiceItemBack);

        mediaAsset.practices(null);
        assertThat(mediaAsset.getPractices()).isNull();
    }

    @Test
    void practiceItemTest() {
        MediaAsset mediaAsset = getMediaAssetRandomSampleGenerator();
        PracticeItem practiceItemBack = getPracticeItemRandomSampleGenerator();

        mediaAsset.setPracticeItem(practiceItemBack);
        assertThat(mediaAsset.getPracticeItem()).isEqualTo(practiceItemBack);
        assertThat(practiceItemBack.getCoverImage()).isEqualTo(mediaAsset);

        mediaAsset.practiceItem(null);
        assertThat(mediaAsset.getPracticeItem()).isNull();
        assertThat(practiceItemBack.getCoverImage()).isNull();
    }

    @Test
    void blogItemTest() {
        MediaAsset mediaAsset = getMediaAssetRandomSampleGenerator();
        BlogItem blogItemBack = getBlogItemRandomSampleGenerator();

        mediaAsset.setBlogItem(blogItemBack);
        assertThat(mediaAsset.getBlogItem()).isEqualTo(blogItemBack);
        assertThat(blogItemBack.getJumbotronImage()).isEqualTo(mediaAsset);

        mediaAsset.blogItem(null);
        assertThat(mediaAsset.getBlogItem()).isNull();
        assertThat(blogItemBack.getJumbotronImage()).isNull();
    }

    @Test
    void pageItemTest() {
        MediaAsset mediaAsset = getMediaAssetRandomSampleGenerator();
        PageItem pageItemBack = getPageItemRandomSampleGenerator();

        mediaAsset.setPageItem(pageItemBack);
        assertThat(mediaAsset.getPageItem()).isEqualTo(pageItemBack);
        assertThat(pageItemBack.getJumbotronImage()).isEqualTo(mediaAsset);

        mediaAsset.pageItem(null);
        assertThat(mediaAsset.getPageItem()).isNull();
        assertThat(pageItemBack.getJumbotronImage()).isNull();
    }
}
