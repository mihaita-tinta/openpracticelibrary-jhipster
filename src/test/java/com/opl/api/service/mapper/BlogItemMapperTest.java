package com.opl.api.service.mapper;

import static com.opl.api.domain.BlogItemAsserts.*;
import static com.opl.api.domain.BlogItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlogItemMapperTest {

    private BlogItemMapper blogItemMapper;

    @BeforeEach
    void setUp() {
        blogItemMapper = new BlogItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBlogItemSample1();
        var actual = blogItemMapper.toEntity(blogItemMapper.toDto(expected));
        assertBlogItemAllPropertiesEquals(expected, actual);
    }
}
