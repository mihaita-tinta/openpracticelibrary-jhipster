package com.opl.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlogItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlogItemDTO.class);
        BlogItemDTO blogItemDTO1 = new BlogItemDTO();
        blogItemDTO1.setId(1L);
        BlogItemDTO blogItemDTO2 = new BlogItemDTO();
        assertThat(blogItemDTO1).isNotEqualTo(blogItemDTO2);
        blogItemDTO2.setId(blogItemDTO1.getId());
        assertThat(blogItemDTO1).isEqualTo(blogItemDTO2);
        blogItemDTO2.setId(2L);
        assertThat(blogItemDTO1).isNotEqualTo(blogItemDTO2);
        blogItemDTO1.setId(null);
        assertThat(blogItemDTO1).isNotEqualTo(blogItemDTO2);
    }
}
