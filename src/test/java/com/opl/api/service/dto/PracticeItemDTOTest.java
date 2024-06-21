package com.opl.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PracticeItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PracticeItemDTO.class);
        PracticeItemDTO practiceItemDTO1 = new PracticeItemDTO();
        practiceItemDTO1.setId(1L);
        PracticeItemDTO practiceItemDTO2 = new PracticeItemDTO();
        assertThat(practiceItemDTO1).isNotEqualTo(practiceItemDTO2);
        practiceItemDTO2.setId(practiceItemDTO1.getId());
        assertThat(practiceItemDTO1).isEqualTo(practiceItemDTO2);
        practiceItemDTO2.setId(2L);
        assertThat(practiceItemDTO1).isNotEqualTo(practiceItemDTO2);
        practiceItemDTO1.setId(null);
        assertThat(practiceItemDTO1).isNotEqualTo(practiceItemDTO2);
    }
}
