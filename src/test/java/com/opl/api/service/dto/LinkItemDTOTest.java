package com.opl.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinkItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkItemDTO.class);
        LinkItemDTO linkItemDTO1 = new LinkItemDTO();
        linkItemDTO1.setId(1L);
        LinkItemDTO linkItemDTO2 = new LinkItemDTO();
        assertThat(linkItemDTO1).isNotEqualTo(linkItemDTO2);
        linkItemDTO2.setId(linkItemDTO1.getId());
        assertThat(linkItemDTO1).isEqualTo(linkItemDTO2);
        linkItemDTO2.setId(2L);
        assertThat(linkItemDTO1).isNotEqualTo(linkItemDTO2);
        linkItemDTO1.setId(null);
        assertThat(linkItemDTO1).isNotEqualTo(linkItemDTO2);
    }
}
