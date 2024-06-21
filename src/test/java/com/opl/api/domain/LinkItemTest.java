package com.opl.api.domain;

import static com.opl.api.domain.LinkItemTestSamples.*;
import static com.opl.api.domain.PracticeItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinkItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkItem.class);
        LinkItem linkItem1 = getLinkItemSample1();
        LinkItem linkItem2 = new LinkItem();
        assertThat(linkItem1).isNotEqualTo(linkItem2);

        linkItem2.setId(linkItem1.getId());
        assertThat(linkItem1).isEqualTo(linkItem2);

        linkItem2 = getLinkItemSample2();
        assertThat(linkItem1).isNotEqualTo(linkItem2);
    }

    @Test
    void practiceItemTest() {
        LinkItem linkItem = getLinkItemRandomSampleGenerator();
        PracticeItem practiceItemBack = getPracticeItemRandomSampleGenerator();

        linkItem.setPracticeItem(practiceItemBack);
        assertThat(linkItem.getPracticeItem()).isEqualTo(practiceItemBack);

        linkItem.practiceItem(null);
        assertThat(linkItem.getPracticeItem()).isNull();
    }
}
