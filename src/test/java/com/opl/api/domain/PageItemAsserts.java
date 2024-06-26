package com.opl.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PageItemAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPageItemAllPropertiesEquals(PageItem expected, PageItem actual) {
        assertPageItemAutoGeneratedPropertiesEquals(expected, actual);
        assertPageItemAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPageItemAllUpdatablePropertiesEquals(PageItem expected, PageItem actual) {
        assertPageItemUpdatableFieldsEquals(expected, actual);
        assertPageItemUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPageItemAutoGeneratedPropertiesEquals(PageItem expected, PageItem actual) {
        assertThat(expected)
            .as("Verify PageItem auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPageItemUpdatableFieldsEquals(PageItem expected, PageItem actual) {
        assertThat(expected)
            .as("Verify PageItem relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getAuthors()).as("check authors").isEqualTo(actual.getAuthors()))
            .satisfies(e -> assertThat(e.getMenu()).as("check menu").isEqualTo(actual.getMenu()))
            .satisfies(e -> assertThat(e.getMenuWeight()).as("check menuWeight").isEqualTo(actual.getMenuWeight()))
            .satisfies(e -> assertThat(e.getPublishDate()).as("check publishDate").isEqualTo(actual.getPublishDate()))
            .satisfies(e -> assertThat(e.getPublishedBy()).as("check publishedBy").isEqualTo(actual.getPublishedBy()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getApprovedBy()).as("check approvedBy").isEqualTo(actual.getApprovedBy()))
            .satisfies(e -> assertThat(e.getApprovedDate()).as("check approvedDate").isEqualTo(actual.getApprovedDate()))
            .satisfies(e -> assertThat(e.getJumbotronAltText()).as("check jumbotronAltText").isEqualTo(actual.getJumbotronAltText()))
            .satisfies(e -> assertThat(e.getBody()).as("check body").isEqualTo(actual.getBody()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPageItemUpdatableRelationshipsEquals(PageItem expected, PageItem actual) {
        assertThat(expected)
            .as("Verify PageItem relationships")
            .satisfies(e -> assertThat(e.getJumbotronImage()).as("check jumbotronImage").isEqualTo(actual.getJumbotronImage()));
    }
}
