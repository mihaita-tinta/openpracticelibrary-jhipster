package com.opl.api.domain;

import static com.opl.api.domain.PracticeItemTestSamples.*;
import static com.opl.api.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void practiceItemTest() {
        Tag tag = getTagRandomSampleGenerator();
        PracticeItem practiceItemBack = getPracticeItemRandomSampleGenerator();

        tag.setPracticeItem(practiceItemBack);
        assertThat(tag.getPracticeItem()).isEqualTo(practiceItemBack);

        tag.practiceItem(null);
        assertThat(tag.getPracticeItem()).isNull();
    }
}
