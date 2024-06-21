package com.opl.api.service.mapper;

import static com.opl.api.domain.MediaAssetAsserts.*;
import static com.opl.api.domain.MediaAssetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaAssetMapperTest {

    private MediaAssetMapper mediaAssetMapper;

    @BeforeEach
    void setUp() {
        mediaAssetMapper = new MediaAssetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMediaAssetSample1();
        var actual = mediaAssetMapper.toEntity(mediaAssetMapper.toDto(expected));
        assertMediaAssetAllPropertiesEquals(expected, actual);
    }
}
