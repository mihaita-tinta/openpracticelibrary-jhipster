package com.opl.api.service.mapper;

import static com.opl.api.domain.PracticeItemAsserts.*;
import static com.opl.api.domain.PracticeItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PracticeItemMapperTest {

    private PracticeItemMapper practiceItemMapper;

    @BeforeEach
    void setUp() {
        practiceItemMapper = new PracticeItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPracticeItemSample1();
        var actual = practiceItemMapper.toEntity(practiceItemMapper.toDto(expected));
        assertPracticeItemAllPropertiesEquals(expected, actual);
    }
}
