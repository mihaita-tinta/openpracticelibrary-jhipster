package com.opl.api.web.rest;

import static com.opl.api.domain.BlogItemAsserts.*;
import static com.opl.api.web.rest.TestUtil.createUpdateProxyForBean;
import static com.opl.api.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opl.api.IntegrationTest;
import com.opl.api.domain.BlogItem;
import com.opl.api.domain.enumeration.Status;
import com.opl.api.repository.BlogItemRepository;
import com.opl.api.service.dto.BlogItemDTO;
import com.opl.api.service.mapper.BlogItemMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BlogItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlogItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBTITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUBTITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PUBLISH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PUBLISHED_BY = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHED_BY = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.DRAFT;
    private static final Status UPDATED_STATUS = Status.IN_REVIEW;

    private static final String DEFAULT_APPROVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_APPROVED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_AUTHORS = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORS = "BBBBBBBBBB";

    private static final String DEFAULT_JUMBOTRON_ALT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_JUMBOTRON_ALT_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/blog-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BlogItemRepository blogItemRepository;

    @Autowired
    private BlogItemMapper blogItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlogItemMockMvc;

    private BlogItem blogItem;

    private BlogItem insertedBlogItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogItem createEntity(EntityManager em) {
        BlogItem blogItem = new BlogItem()
            .title(DEFAULT_TITLE)
            .subtitle(DEFAULT_SUBTITLE)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .publishedBy(DEFAULT_PUBLISHED_BY)
            .status(DEFAULT_STATUS)
            .approvedBy(DEFAULT_APPROVED_BY)
            .approvedDate(DEFAULT_APPROVED_DATE)
            .authors(DEFAULT_AUTHORS)
            .jumbotronAltText(DEFAULT_JUMBOTRON_ALT_TEXT)
            .body(DEFAULT_BODY);
        return blogItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogItem createUpdatedEntity(EntityManager em) {
        BlogItem blogItem = new BlogItem()
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .authors(UPDATED_AUTHORS)
            .jumbotronAltText(UPDATED_JUMBOTRON_ALT_TEXT)
            .body(UPDATED_BODY);
        return blogItem;
    }

    @BeforeEach
    public void initTest() {
        blogItem = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBlogItem != null) {
            blogItemRepository.delete(insertedBlogItem);
            insertedBlogItem = null;
        }
    }

    @Test
    @Transactional
    void createBlogItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);
        var returnedBlogItemDTO = om.readValue(
            restBlogItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blogItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BlogItemDTO.class
        );

        // Validate the BlogItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBlogItem = blogItemMapper.toEntity(returnedBlogItemDTO);
        assertBlogItemUpdatableFieldsEquals(returnedBlogItem, getPersistedBlogItem(returnedBlogItem));

        insertedBlogItem = returnedBlogItem;
    }

    @Test
    @Transactional
    void createBlogItemWithExistingId() throws Exception {
        // Create the BlogItem with an existing ID
        blogItem.setId(1L);
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blogItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BlogItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBlogItems() throws Exception {
        // Initialize the database
        insertedBlogItem = blogItemRepository.saveAndFlush(blogItem);

        // Get all the blogItemList
        restBlogItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blogItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subtitle").value(hasItem(DEFAULT_SUBTITLE)))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(sameInstant(DEFAULT_PUBLISH_DATE))))
            .andExpect(jsonPath("$.[*].publishedBy").value(hasItem(DEFAULT_PUBLISHED_BY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)))
            .andExpect(jsonPath("$.[*].approvedDate").value(hasItem(sameInstant(DEFAULT_APPROVED_DATE))))
            .andExpect(jsonPath("$.[*].authors").value(hasItem(DEFAULT_AUTHORS)))
            .andExpect(jsonPath("$.[*].jumbotronAltText").value(hasItem(DEFAULT_JUMBOTRON_ALT_TEXT)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())));
    }

    @Test
    @Transactional
    void getBlogItem() throws Exception {
        // Initialize the database
        insertedBlogItem = blogItemRepository.saveAndFlush(blogItem);

        // Get the blogItem
        restBlogItemMockMvc
            .perform(get(ENTITY_API_URL_ID, blogItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blogItem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subtitle").value(DEFAULT_SUBTITLE))
            .andExpect(jsonPath("$.publishDate").value(sameInstant(DEFAULT_PUBLISH_DATE)))
            .andExpect(jsonPath("$.publishedBy").value(DEFAULT_PUBLISHED_BY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.approvedBy").value(DEFAULT_APPROVED_BY))
            .andExpect(jsonPath("$.approvedDate").value(sameInstant(DEFAULT_APPROVED_DATE)))
            .andExpect(jsonPath("$.authors").value(DEFAULT_AUTHORS))
            .andExpect(jsonPath("$.jumbotronAltText").value(DEFAULT_JUMBOTRON_ALT_TEXT))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBlogItem() throws Exception {
        // Get the blogItem
        restBlogItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBlogItem() throws Exception {
        // Initialize the database
        insertedBlogItem = blogItemRepository.saveAndFlush(blogItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blogItem
        BlogItem updatedBlogItem = blogItemRepository.findById(blogItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBlogItem are not directly saved in db
        em.detach(updatedBlogItem);
        updatedBlogItem
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .authors(UPDATED_AUTHORS)
            .jumbotronAltText(UPDATED_JUMBOTRON_ALT_TEXT)
            .body(UPDATED_BODY);
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(updatedBlogItem);

        restBlogItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blogItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blogItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the BlogItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBlogItemToMatchAllProperties(updatedBlogItem);
    }

    @Test
    @Transactional
    void putNonExistingBlogItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogItem.setId(longCount.incrementAndGet());

        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blogItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlogItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogItem.setId(longCount.incrementAndGet());

        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlogItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogItem.setId(longCount.incrementAndGet());

        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blogItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlogItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlogItemWithPatch() throws Exception {
        // Initialize the database
        insertedBlogItem = blogItemRepository.saveAndFlush(blogItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blogItem using partial update
        BlogItem partialUpdatedBlogItem = new BlogItem();
        partialUpdatedBlogItem.setId(blogItem.getId());

        partialUpdatedBlogItem
            .subtitle(UPDATED_SUBTITLE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedDate(UPDATED_APPROVED_DATE)
            .body(UPDATED_BODY);

        restBlogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlogItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlogItem))
            )
            .andExpect(status().isOk());

        // Validate the BlogItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlogItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBlogItem, blogItem), getPersistedBlogItem(blogItem));
    }

    @Test
    @Transactional
    void fullUpdateBlogItemWithPatch() throws Exception {
        // Initialize the database
        insertedBlogItem = blogItemRepository.saveAndFlush(blogItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blogItem using partial update
        BlogItem partialUpdatedBlogItem = new BlogItem();
        partialUpdatedBlogItem.setId(blogItem.getId());

        partialUpdatedBlogItem
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .authors(UPDATED_AUTHORS)
            .jumbotronAltText(UPDATED_JUMBOTRON_ALT_TEXT)
            .body(UPDATED_BODY);

        restBlogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlogItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlogItem))
            )
            .andExpect(status().isOk());

        // Validate the BlogItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlogItemUpdatableFieldsEquals(partialUpdatedBlogItem, getPersistedBlogItem(partialUpdatedBlogItem));
    }

    @Test
    @Transactional
    void patchNonExistingBlogItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogItem.setId(longCount.incrementAndGet());

        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blogItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlogItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogItem.setId(longCount.incrementAndGet());

        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlogItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogItem.setId(longCount.incrementAndGet());

        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(blogItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlogItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlogItem() throws Exception {
        // Initialize the database
        insertedBlogItem = blogItemRepository.saveAndFlush(blogItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the blogItem
        restBlogItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, blogItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return blogItemRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected BlogItem getPersistedBlogItem(BlogItem blogItem) {
        return blogItemRepository.findById(blogItem.getId()).orElseThrow();
    }

    protected void assertPersistedBlogItemToMatchAllProperties(BlogItem expectedBlogItem) {
        assertBlogItemAllPropertiesEquals(expectedBlogItem, getPersistedBlogItem(expectedBlogItem));
    }

    protected void assertPersistedBlogItemToMatchUpdatableProperties(BlogItem expectedBlogItem) {
        assertBlogItemAllUpdatablePropertiesEquals(expectedBlogItem, getPersistedBlogItem(expectedBlogItem));
    }
}
