package com.opl.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkItemAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLinkItemAllPropertiesEquals(LinkItem expected, LinkItem actual) {
        assertLinkItemAutoGeneratedPropertiesEquals(expected, actual);
        assertLinkItemAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLinkItemAllUpdatablePropertiesEquals(LinkItem expected, LinkItem actual) {
        assertLinkItemUpdatableFieldsEquals(expected, actual);
        assertLinkItemUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLinkItemAutoGeneratedPropertiesEquals(LinkItem expected, LinkItem actual) {
        assertThat(expected)
            .as("Verify LinkItem auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLinkItemUpdatableFieldsEquals(LinkItem expected, LinkItem actual) {
        assertThat(expected)
            .as("Verify LinkItem relevant properties")
            .satisfies(e -> assertThat(e.getUrl()).as("check url").isEqualTo(actual.getUrl()))
            .satisfies(e -> assertThat(e.getSortIndex()).as("check sortIndex").isEqualTo(actual.getSortIndex()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLinkItemUpdatableRelationshipsEquals(LinkItem expected, LinkItem actual) {
        assertThat(expected)
            .as("Verify LinkItem relationships")
            .satisfies(e -> assertThat(e.getPracticeItem()).as("check practiceItem").isEqualTo(actual.getPracticeItem()));
    }
}