package com.opl.api.web.rest;

import static com.opl.api.domain.LinkItemAsserts.*;
import static com.opl.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opl.api.IntegrationTest;
import com.opl.api.domain.LinkItem;
import com.opl.api.repository.LinkItemRepository;
import com.opl.api.service.dto.LinkItemDTO;
import com.opl.api.service.mapper.LinkItemMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link LinkItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LinkItemResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_INDEX = 1;
    private static final Integer UPDATED_SORT_INDEX = 2;

    private static final String ENTITY_API_URL = "/api/link-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LinkItemRepository linkItemRepository;

    @Autowired
    private LinkItemMapper linkItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLinkItemMockMvc;

    private LinkItem linkItem;

    private LinkItem insertedLinkItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LinkItem createEntity(EntityManager em) {
        LinkItem linkItem = new LinkItem().url(DEFAULT_URL).sortIndex(DEFAULT_SORT_INDEX);
        return linkItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LinkItem createUpdatedEntity(EntityManager em) {
        LinkItem linkItem = new LinkItem().url(UPDATED_URL).sortIndex(UPDATED_SORT_INDEX);
        return linkItem;
    }

    @BeforeEach
    public void initTest() {
        linkItem = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLinkItem != null) {
            linkItemRepository.delete(insertedLinkItem);
            insertedLinkItem = null;
        }
    }

    @Test
    @Transactional
    void createLinkItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LinkItem
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(linkItem);
        var returnedLinkItemDTO = om.readValue(
            restLinkItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LinkItemDTO.class
        );

        // Validate the LinkItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLinkItem = linkItemMapper.toEntity(returnedLinkItemDTO);
        assertLinkItemUpdatableFieldsEquals(returnedLinkItem, getPersistedLinkItem(returnedLinkItem));

        insertedLinkItem = returnedLinkItem;
    }

    @Test
    @Transactional
    void createLinkItemWithExistingId() throws Exception {
        // Create the LinkItem with an existing ID
        linkItem.setId(1L);
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(linkItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinkItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LinkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLinkItems() throws Exception {
        // Initialize the database
        insertedLinkItem = linkItemRepository.saveAndFlush(linkItem);

        // Get all the linkItemList
        restLinkItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(linkItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].sortIndex").value(hasItem(DEFAULT_SORT_INDEX)));
    }

    @Test
    @Transactional
    void getLinkItem() throws Exception {
        // Initialize the database
        insertedLinkItem = linkItemRepository.saveAndFlush(linkItem);

        // Get the linkItem
        restLinkItemMockMvc
            .perform(get(ENTITY_API_URL_ID, linkItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(linkItem.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.sortIndex").value(DEFAULT_SORT_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingLinkItem() throws Exception {
        // Get the linkItem
        restLinkItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLinkItem() throws Exception {
        // Initialize the database
        insertedLinkItem = linkItemRepository.saveAndFlush(linkItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkItem
        LinkItem updatedLinkItem = linkItemRepository.findById(linkItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLinkItem are not directly saved in db
        em.detach(updatedLinkItem);
        updatedLinkItem.url(UPDATED_URL).sortIndex(UPDATED_SORT_INDEX);
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(updatedLinkItem);

        restLinkItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, linkItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the LinkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLinkItemToMatchAllProperties(updatedLinkItem);
    }

    @Test
    @Transactional
    void putNonExistingLinkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkItem.setId(longCount.incrementAndGet());

        // Create the LinkItem
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(linkItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLinkItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, linkItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLinkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkItem.setId(longCount.incrementAndGet());

        // Create the LinkItem
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(linkItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLinkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkItem.setId(longCount.incrementAndGet());

        // Create the LinkItem
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(linkItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LinkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLinkItemWithPatch() throws Exception {
        // Initialize the database
        insertedLinkItem = linkItemRepository.saveAndFlush(linkItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkItem using partial update
        LinkItem partialUpdatedLinkItem = new LinkItem();
        partialUpdatedLinkItem.setId(linkItem.getId());

        partialUpdatedLinkItem.url(UPDATED_URL).sortIndex(UPDATED_SORT_INDEX);

        restLinkItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLinkItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLinkItem))
            )
            .andExpect(status().isOk());

        // Validate the LinkItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLinkItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLinkItem, linkItem), getPersistedLinkItem(linkItem));
    }

    @Test
    @Transactional
    void fullUpdateLinkItemWithPatch() throws Exception {
        // Initialize the database
        insertedLinkItem = linkItemRepository.saveAndFlush(linkItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkItem using partial update
        LinkItem partialUpdatedLinkItem = new LinkItem();
        partialUpdatedLinkItem.setId(linkItem.getId());

        partialUpdatedLinkItem.url(UPDATED_URL).sortIndex(UPDATED_SORT_INDEX);

        restLinkItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLinkItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLinkItem))
            )
            .andExpect(status().isOk());

        // Validate the LinkItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLinkItemUpdatableFieldsEquals(partialUpdatedLinkItem, getPersistedLinkItem(partialUpdatedLinkItem));
    }

    @Test
    @Transactional
    void patchNonExistingLinkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkItem.setId(longCount.incrementAndGet());

        // Create the LinkItem
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(linkItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLinkItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, linkItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(linkItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLinkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkItem.setId(longCount.incrementAndGet());

        // Create the LinkItem
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(linkItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(linkItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLinkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkItem.setId(longCount.incrementAndGet());

        // Create the LinkItem
        LinkItemDTO linkItemDTO = linkItemMapper.toDto(linkItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(linkItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LinkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLinkItem() throws Exception {
        // Initialize the database
        insertedLinkItem = linkItemRepository.saveAndFlush(linkItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the linkItem
        restLinkItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, linkItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return linkItemRepository.count();
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

    protected LinkItem getPersistedLinkItem(LinkItem linkItem) {
        return linkItemRepository.findById(linkItem.getId()).orElseThrow();
    }

    protected void assertPersistedLinkItemToMatchAllProperties(LinkItem expectedLinkItem) {
        assertLinkItemAllPropertiesEquals(expectedLinkItem, getPersistedLinkItem(expectedLinkItem));
    }

    protected void assertPersistedLinkItemToMatchUpdatableProperties(LinkItem expectedLinkItem) {
        assertLinkItemAllUpdatablePropertiesEquals(expectedLinkItem, getPersistedLinkItem(expectedLinkItem));
    }
}
