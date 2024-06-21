package com.opl.api.domain;

import static com.opl.api.domain.MediaAssetTestSamples.*;
import static com.opl.api.domain.PageItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PageItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageItem.class);
        PageItem pageItem1 = getPageItemSample1();
        PageItem pageItem2 = new PageItem();
        assertThat(pageItem1).isNotEqualTo(pageItem2);

        pageItem2.setId(pageItem1.getId());
        assertThat(pageItem1).isEqualTo(pageItem2);

        pageItem2 = getPageItemSample2();
        assertThat(pageItem1).isNotEqualTo(pageItem2);
    }

    @Test
    void jumbotronImageTest() {
        PageItem pageItem = getPageItemRandomSampleGenerator();
        MediaAsset mediaAssetBack = getMediaAssetRandomSampleGenerator();

        pageItem.setJumbotronImage(mediaAssetBack);
        assertThat(pageItem.getJumbotronImage()).isEqualTo(mediaAssetBack);

        pageItem.jumbotronImage(null);
        assertThat(pageItem.getJumbotronImage()).isNull();
    }
}
