package com.opl.api.service.mapper;

import static com.opl.api.domain.LinkItemAsserts.*;
import static com.opl.api.domain.LinkItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkItemMapperTest {

    private LinkItemMapper linkItemMapper;

    @BeforeEach
    void setUp() {
        linkItemMapper = new LinkItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLinkItemSample1();
        var actual = linkItemMapper.toEntity(linkItemMapper.toDto(expected));
        assertLinkItemAllPropertiesEquals(expected, actual);
    }
}
