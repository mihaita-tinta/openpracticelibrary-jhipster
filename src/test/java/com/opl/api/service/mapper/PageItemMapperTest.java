package com.opl.api.service.mapper;

import static com.opl.api.domain.PageItemAsserts.*;
import static com.opl.api.domain.PageItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageItemMapperTest {

    private PageItemMapper pageItemMapper;

    @BeforeEach
    void setUp() {
        pageItemMapper = new PageItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPageItemSample1();
        var actual = pageItemMapper.toEntity(pageItemMapper.toDto(expected));
        assertPageItemAllPropertiesEquals(expected, actual);
    }
}
