package com.opl.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PageItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageItemDTO.class);
        PageItemDTO pageItemDTO1 = new PageItemDTO();
        pageItemDTO1.setId(1L);
        PageItemDTO pageItemDTO2 = new PageItemDTO();
        assertThat(pageItemDTO1).isNotEqualTo(pageItemDTO2);
        pageItemDTO2.setId(pageItemDTO1.getId());
        assertThat(pageItemDTO1).isEqualTo(pageItemDTO2);
        pageItemDTO2.setId(2L);
        assertThat(pageItemDTO1).isNotEqualTo(pageItemDTO2);
        pageItemDTO1.setId(null);
        assertThat(pageItemDTO1).isNotEqualTo(pageItemDTO2);
    }
}
