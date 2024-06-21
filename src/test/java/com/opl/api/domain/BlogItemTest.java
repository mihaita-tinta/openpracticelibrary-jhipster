package com.opl.api.domain;

import static com.opl.api.domain.BlogItemTestSamples.*;
import static com.opl.api.domain.MediaAssetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlogItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlogItem.class);
        BlogItem blogItem1 = getBlogItemSample1();
        BlogItem blogItem2 = new BlogItem();
        assertThat(blogItem1).isNotEqualTo(blogItem2);

        blogItem2.setId(blogItem1.getId());
        assertThat(blogItem1).isEqualTo(blogItem2);

        blogItem2 = getBlogItemSample2();
        assertThat(blogItem1).isNotEqualTo(blogItem2);
    }

    @Test
    void jumbotronImageTest() {
        BlogItem blogItem = getBlogItemRandomSampleGenerator();
        MediaAsset mediaAssetBack = getMediaAssetRandomSampleGenerator();

        blogItem.setJumbotronImage(mediaAssetBack);
        assertThat(blogItem.getJumbotronImage()).isEqualTo(mediaAssetBack);

        blogItem.jumbotronImage(null);
        assertThat(blogItem.getJumbotronImage()).isNull();
    }
}
