package com.opl.api.web.rest;

import static com.opl.api.domain.PageItemAsserts.*;
import static com.opl.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opl.api.IntegrationTest;
import com.opl.api.domain.PageItem;
import com.opl.api.domain.enumeration.Status;
import com.opl.api.repository.PageItemRepository;
import com.opl.api.service.dto.PageItemDTO;
import com.opl.api.service.mapper.PageItemMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PageItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PageItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHORS = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORS = "BBBBBBBBBB";

    private static final String DEFAULT_MENU = "AAAAAAAAAA";
    private static final String UPDATED_MENU = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_WEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_MENU_WEIGHT = "BBBBBBBBBB";

    private static final Instant DEFAULT_PUBLISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PUBLISHED_BY = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHED_BY = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.DRAFT;
    private static final Status UPDATED_STATUS = Status.IN_REVIEW;

    private static final String DEFAULT_APPROVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_APPROVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPROVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_JUMBOTRON_ALT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_JUMBOTRON_ALT_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/page-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PageItemRepository pageItemRepository;

    @Autowired
    private PageItemMapper pageItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPageItemMockMvc;

    private PageItem pageItem;

    private PageItem insertedPageItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageItem createEntity(EntityManager em) {
        PageItem pageItem = new PageItem()
            .title(DEFAULT_TITLE)
            .authors(DEFAULT_AUTHORS)
            .menu(DEFAULT_MENU)
            .menuWeight(DEFAULT_MENU_WEIGHT)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .publishedBy(DEFAULT_PUBLISHED_BY)
            .status(DEFAULT_STATUS)
            .approvedBy(DEFAULT_APPROVED_BY)
            .approvedDate(DEFAULT_APPROVED_DATE)
            .jumbotronAltText(DEFAULT_JUMBOTRON_ALT_TEXT)
            .body(DEFAULT_BODY);
        return pageItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageItem createUpdatedEntity(EntityManager em) {
        PageItem pageItem = new PageItem()
            .title(UPDATED_TITLE)
            .authors(UPDATED_AUTHORS)
            .menu(UPDATED_MENU)
            .menuWeight(UPDATED_MENU_WEIGHT)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .jumbotronAltText(UPDATED_JUMBOTRON_ALT_TEXT)
            .body(UPDATED_BODY);
        return pageItem;
    }

    @BeforeEach
    public void initTest() {
        pageItem = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPageItem != null) {
            pageItemRepository.delete(insertedPageItem);
            insertedPageItem = null;
        }
    }

    @Test
    @Transactional
    void createPageItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PageItem
        PageItemDTO pageItemDTO = pageItemMapper.toDto(pageItem);
        var returnedPageItemDTO = om.readValue(
            restPageItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pageItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PageItemDTO.class
        );

        // Validate the PageItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPageItem = pageItemMapper.toEntity(returnedPageItemDTO);
        assertPageItemUpdatableFieldsEquals(returnedPageItem, getPersistedPageItem(returnedPageItem));

        insertedPageItem = returnedPageItem;
    }

    @Test
    @Transactional
    void createPageItemWithExistingId() throws Exception {
        // Create the PageItem with an existing ID
        pageItem.setId(1L);
        PageItemDTO pageItemDTO = pageItemMapper.toDto(pageItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pageItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PageItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPageItems() throws Exception {
        // Initialize the database
        insertedPageItem = pageItemRepository.saveAndFlush(pageItem);

        // Get all the pageItemList
        restPageItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].authors").value(hasItem(DEFAULT_AUTHORS)))
            .andExpect(jsonPath("$.[*].menu").value(hasItem(DEFAULT_MENU)))
            .andExpect(jsonPath("$.[*].menuWeight").value(hasItem(DEFAULT_MENU_WEIGHT)))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].publishedBy").value(hasItem(DEFAULT_PUBLISHED_BY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)))
            .andExpect(jsonPath("$.[*].approvedDate").value(hasItem(DEFAULT_APPROVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].jumbotronAltText").value(hasItem(DEFAULT_JUMBOTRON_ALT_TEXT)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())));
    }

    @Test
    @Transactional
    void getPageItem() throws Exception {
        // Initialize the database
        insertedPageItem = pageItemRepository.saveAndFlush(pageItem);

        // Get the pageItem
        restPageItemMockMvc
            .perform(get(ENTITY_API_URL_ID, pageItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pageItem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.authors").value(DEFAULT_AUTHORS))
            .andExpect(jsonPath("$.menu").value(DEFAULT_MENU))
            .andExpect(jsonPath("$.menuWeight").value(DEFAULT_MENU_WEIGHT))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()))
            .andExpect(jsonPath("$.publishedBy").value(DEFAULT_PUBLISHED_BY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.approvedBy").value(DEFAULT_APPROVED_BY))
            .andExpect(jsonPath("$.approvedDate").value(DEFAULT_APPROVED_DATE.toString()))
            .andExpect(jsonPath("$.jumbotronAltText").value(DEFAULT_JUMBOTRON_ALT_TEXT))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPageItem() throws Exception {
        // Get the pageItem
        restPageItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPageItem() throws Exception {
        // Initialize the database
        insertedPageItem = pageItemRepository.saveAndFlush(pageItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pageItem
        PageItem updatedPageItem = pageItemRepository.findById(pageItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPageItem are not directly saved in db
        em.detach(updatedPageItem);
        updatedPageItem
            .title(UPDATED_TITLE)
            .authors(UPDATED_AUTHORS)
            .menu(UPDATED_MENU)
            .menuWeight(UPDATED_MENU_WEIGHT)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .jumbotronAltText(UPDATED_JUMBOTRON_ALT_TEXT)
            .body(UPDATED_BODY);
        PageItemDTO pageItemDTO = pageItemMapper.toDto(updatedPageItem);

        restPageItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pageItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the PageItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPageItemToMatchAllProperties(updatedPageItem);
    }

    @Test
    @Transactional
    void putNonExistingPageItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pageItem.setId(longCount.incrementAndGet());

        // Create the PageItem
        PageItemDTO pageItemDTO = pageItemMapper.toDto(pageItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pageItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPageItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pageItem.setId(longCount.incrementAndGet());

        // Create the PageItem
        PageItemDTO pageItemDTO = pageItemMapper.toDto(pageItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pageItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPageItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pageItem.setId(longCount.incrementAndGet());

        // Create the PageItem
        PageItemDTO pageItemDTO = pageItemMapper.toDto(pageItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pageItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePageItemWithPatch() throws Exception {
        // Initialize the database
        insertedPageItem = pageItemRepository.saveAndFlush(pageItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pageItem using partial update
        PageItem partialUpdatedPageItem = new PageItem();
        partialUpdatedPageItem.setId(pageItem.getId());

        partialUpdatedPageItem
            .menu(UPDATED_MENU)
            .menuWeight(UPDATED_MENU_WEIGHT)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .jumbotronAltText(UPDATED_JUMBOTRON_ALT_TEXT);

        restPageItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPageItem))
            )
            .andExpect(status().isOk());

        // Validate the PageItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPageItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPageItem, pageItem), getPersistedPageItem(pageItem));
    }

    @Test
    @Transactional
    void fullUpdatePageItemWithPatch() throws Exception {
        // Initialize the database
        insertedPageItem = pageItemRepository.saveAndFlush(pageItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pageItem using partial update
        PageItem partialUpdatedPageItem = new PageItem();
        partialUpdatedPageItem.setId(pageItem.getId());

        partialUpdatedPageItem
            .title(UPDATED_TITLE)
            .authors(UPDATED_AUTHORS)
            .menu(UPDATED_MENU)
            .menuWeight(UPDATED_MENU_WEIGHT)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .jumbotronAltText(UPDATED_JUMBOTRON_ALT_TEXT)
            .body(UPDATED_BODY);

        restPageItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPageItem))
            )
            .andExpect(status().isOk());

        // Validate the PageItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPageItemUpdatableFieldsEquals(partialUpdatedPageItem, getPersistedPageItem(partialUpdatedPageItem));
    }

    @Test
    @Transactional
    void patchNonExistingPageItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pageItem.setId(longCount.incrementAndGet());

        // Create the PageItem
        PageItemDTO pageItemDTO = pageItemMapper.toDto(pageItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pageItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pageItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPageItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pageItem.setId(longCount.incrementAndGet());

        // Create the PageItem
        PageItemDTO pageItemDTO = pageItemMapper.toDto(pageItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pageItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPageItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pageItem.setId(longCount.incrementAndGet());

        // Create the PageItem
        PageItemDTO pageItemDTO = pageItemMapper.toDto(pageItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pageItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePageItem() throws Exception {
        // Initialize the database
        insertedPageItem = pageItemRepository.saveAndFlush(pageItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pageItem
        restPageItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, pageItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pageItemRepository.count();
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

    protected PageItem getPersistedPageItem(PageItem pageItem) {
        return pageItemRepository.findById(pageItem.getId()).orElseThrow();
    }

    protected void assertPersistedPageItemToMatchAllProperties(PageItem expectedPageItem) {
        assertPageItemAllPropertiesEquals(expectedPageItem, getPersistedPageItem(expectedPageItem));
    }

    protected void assertPersistedPageItemToMatchUpdatableProperties(PageItem expectedPageItem) {
        assertPageItemAllUpdatablePropertiesEquals(expectedPageItem, getPersistedPageItem(expectedPageItem));
    }
}
