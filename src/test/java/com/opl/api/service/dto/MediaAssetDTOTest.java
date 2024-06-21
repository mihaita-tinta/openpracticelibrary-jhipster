package com.opl.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.opl.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MediaAssetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MediaAssetDTO.class);
        MediaAssetDTO mediaAssetDTO1 = new MediaAssetDTO();
        mediaAssetDTO1.setId(1L);
        MediaAssetDTO mediaAssetDTO2 = new MediaAssetDTO();
        assertThat(mediaAssetDTO1).isNotEqualTo(mediaAssetDTO2);
        mediaAssetDTO2.setId(mediaAssetDTO1.getId());
        assertThat(mediaAssetDTO1).isEqualTo(mediaAssetDTO2);
        mediaAssetDTO2.setId(2L);
        assertThat(mediaAssetDTO1).isNotEqualTo(mediaAssetDTO2);
        mediaAssetDTO1.setId(null);
        assertThat(mediaAssetDTO1).isNotEqualTo(mediaAssetDTO2);
    }
}
