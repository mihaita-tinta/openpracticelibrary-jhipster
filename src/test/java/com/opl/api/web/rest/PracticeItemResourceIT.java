package com.opl.api.web.rest;

import static com.opl.api.domain.PracticeItemAsserts.*;
import static com.opl.api.web.rest.TestUtil.createUpdateProxyForBean;
import static com.opl.api.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opl.api.IntegrationTest;
import com.opl.api.domain.PracticeItem;
import com.opl.api.domain.enumeration.FacilitationDifficulty;
import com.opl.api.domain.enumeration.MobiusLoopTag;
import com.opl.api.domain.enumeration.Status;
import com.opl.api.repository.PracticeItemRepository;
import com.opl.api.service.dto.PracticeItemDTO;
import com.opl.api.service.mapper.PracticeItemMapper;
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
 * Integration tests for the {@link PracticeItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PracticeItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIVE = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIVE = "BBBBBBBBBB";

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

    private static final FacilitationDifficulty DEFAULT_FACILITATION_DIFFICULTY = FacilitationDifficulty.EASY;
    private static final FacilitationDifficulty UPDATED_FACILITATION_DIFFICULTY = FacilitationDifficulty.MODERATE;

    private static final MobiusLoopTag DEFAULT_MOBIUS_LOOP_TAG = MobiusLoopTag.DISCOVERY;
    private static final MobiusLoopTag UPDATED_MOBIUS_LOOP_TAG = MobiusLoopTag.DELIVERY;

    private static final String DEFAULT_WHAT = "AAAAAAAAAA";
    private static final String UPDATED_WHAT = "BBBBBBBBBB";

    private static final String DEFAULT_WHY = "AAAAAAAAAA";
    private static final String UPDATED_WHY = "BBBBBBBBBB";

    private static final String DEFAULT_HOW = "AAAAAAAAAA";
    private static final String UPDATED_HOW = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER_OF_PEOPLE_REQUIRED = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER_OF_PEOPLE_REQUIRED = "BBBBBBBBBB";

    private static final String DEFAULT_TIME_LENGTH = "AAAAAAAAAA";
    private static final String UPDATED_TIME_LENGTH = "BBBBBBBBBB";

    private static final String DEFAULT_EXPECTED_PARTICIPANTS = "AAAAAAAAAA";
    private static final String UPDATED_EXPECTED_PARTICIPANTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/practice-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PracticeItemRepository practiceItemRepository;

    @Autowired
    private PracticeItemMapper practiceItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPracticeItemMockMvc;

    private PracticeItem practiceItem;

    private PracticeItem insertedPracticeItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PracticeItem createEntity(EntityManager em) {
        PracticeItem practiceItem = new PracticeItem()
            .title(DEFAULT_TITLE)
            .objective(DEFAULT_OBJECTIVE)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .publishedBy(DEFAULT_PUBLISHED_BY)
            .status(DEFAULT_STATUS)
            .approvedBy(DEFAULT_APPROVED_BY)
            .approvedDate(DEFAULT_APPROVED_DATE)
            .authors(DEFAULT_AUTHORS)
            .facilitationDifficulty(DEFAULT_FACILITATION_DIFFICULTY)
            .mobiusLoopTag(DEFAULT_MOBIUS_LOOP_TAG)
            .what(DEFAULT_WHAT)
            .why(DEFAULT_WHY)
            .how(DEFAULT_HOW)
            .numberOfPeopleRequired(DEFAULT_NUMBER_OF_PEOPLE_REQUIRED)
            .timeLength(DEFAULT_TIME_LENGTH)
            .expectedParticipants(DEFAULT_EXPECTED_PARTICIPANTS);
        return practiceItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PracticeItem createUpdatedEntity(EntityManager em) {
        PracticeItem practiceItem = new PracticeItem()
            .title(UPDATED_TITLE)
            .objective(UPDATED_OBJECTIVE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .authors(UPDATED_AUTHORS)
            .facilitationDifficulty(UPDATED_FACILITATION_DIFFICULTY)
            .mobiusLoopTag(UPDATED_MOBIUS_LOOP_TAG)
            .what(UPDATED_WHAT)
            .why(UPDATED_WHY)
            .how(UPDATED_HOW)
            .numberOfPeopleRequired(UPDATED_NUMBER_OF_PEOPLE_REQUIRED)
            .timeLength(UPDATED_TIME_LENGTH)
            .expectedParticipants(UPDATED_EXPECTED_PARTICIPANTS);
        return practiceItem;
    }

    @BeforeEach
    public void initTest() {
        practiceItem = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPracticeItem != null) {
            practiceItemRepository.delete(insertedPracticeItem);
            insertedPracticeItem = null;
        }
    }

    @Test
    @Transactional
    void createPracticeItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PracticeItem
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(practiceItem);
        var returnedPracticeItemDTO = om.readValue(
            restPracticeItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(practiceItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PracticeItemDTO.class
        );

        // Validate the PracticeItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPracticeItem = practiceItemMapper.toEntity(returnedPracticeItemDTO);
        assertPracticeItemUpdatableFieldsEquals(returnedPracticeItem, getPersistedPracticeItem(returnedPracticeItem));

        insertedPracticeItem = returnedPracticeItem;
    }

    @Test
    @Transactional
    void createPracticeItemWithExistingId() throws Exception {
        // Create the PracticeItem with an existing ID
        practiceItem.setId(1L);
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(practiceItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPracticeItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(practiceItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PracticeItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPracticeItems() throws Exception {
        // Initialize the database
        insertedPracticeItem = practiceItemRepository.saveAndFlush(practiceItem);

        // Get all the practiceItemList
        restPracticeItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(practiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].objective").value(hasItem(DEFAULT_OBJECTIVE)))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(sameInstant(DEFAULT_PUBLISH_DATE))))
            .andExpect(jsonPath("$.[*].publishedBy").value(hasItem(DEFAULT_PUBLISHED_BY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)))
            .andExpect(jsonPath("$.[*].approvedDate").value(hasItem(sameInstant(DEFAULT_APPROVED_DATE))))
            .andExpect(jsonPath("$.[*].authors").value(hasItem(DEFAULT_AUTHORS)))
            .andExpect(jsonPath("$.[*].facilitationDifficulty").value(hasItem(DEFAULT_FACILITATION_DIFFICULTY.toString())))
            .andExpect(jsonPath("$.[*].mobiusLoopTag").value(hasItem(DEFAULT_MOBIUS_LOOP_TAG.toString())))
            .andExpect(jsonPath("$.[*].what").value(hasItem(DEFAULT_WHAT.toString())))
            .andExpect(jsonPath("$.[*].why").value(hasItem(DEFAULT_WHY.toString())))
            .andExpect(jsonPath("$.[*].how").value(hasItem(DEFAULT_HOW.toString())))
            .andExpect(jsonPath("$.[*].numberOfPeopleRequired").value(hasItem(DEFAULT_NUMBER_OF_PEOPLE_REQUIRED)))
            .andExpect(jsonPath("$.[*].timeLength").value(hasItem(DEFAULT_TIME_LENGTH)))
            .andExpect(jsonPath("$.[*].expectedParticipants").value(hasItem(DEFAULT_EXPECTED_PARTICIPANTS)));
    }

    @Test
    @Transactional
    void getPracticeItem() throws Exception {
        // Initialize the database
        insertedPracticeItem = practiceItemRepository.saveAndFlush(practiceItem);

        // Get the practiceItem
        restPracticeItemMockMvc
            .perform(get(ENTITY_API_URL_ID, practiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(practiceItem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.objective").value(DEFAULT_OBJECTIVE))
            .andExpect(jsonPath("$.publishDate").value(sameInstant(DEFAULT_PUBLISH_DATE)))
            .andExpect(jsonPath("$.publishedBy").value(DEFAULT_PUBLISHED_BY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.approvedBy").value(DEFAULT_APPROVED_BY))
            .andExpect(jsonPath("$.approvedDate").value(sameInstant(DEFAULT_APPROVED_DATE)))
            .andExpect(jsonPath("$.authors").value(DEFAULT_AUTHORS))
            .andExpect(jsonPath("$.facilitationDifficulty").value(DEFAULT_FACILITATION_DIFFICULTY.toString()))
            .andExpect(jsonPath("$.mobiusLoopTag").value(DEFAULT_MOBIUS_LOOP_TAG.toString()))
            .andExpect(jsonPath("$.what").value(DEFAULT_WHAT.toString()))
            .andExpect(jsonPath("$.why").value(DEFAULT_WHY.toString()))
            .andExpect(jsonPath("$.how").value(DEFAULT_HOW.toString()))
            .andExpect(jsonPath("$.numberOfPeopleRequired").value(DEFAULT_NUMBER_OF_PEOPLE_REQUIRED))
            .andExpect(jsonPath("$.timeLength").value(DEFAULT_TIME_LENGTH))
            .andExpect(jsonPath("$.expectedParticipants").value(DEFAULT_EXPECTED_PARTICIPANTS));
    }

    @Test
    @Transactional
    void getNonExistingPracticeItem() throws Exception {
        // Get the practiceItem
        restPracticeItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPracticeItem() throws Exception {
        // Initialize the database
        insertedPracticeItem = practiceItemRepository.saveAndFlush(practiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the practiceItem
        PracticeItem updatedPracticeItem = practiceItemRepository.findById(practiceItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPracticeItem are not directly saved in db
        em.detach(updatedPracticeItem);
        updatedPracticeItem
            .title(UPDATED_TITLE)
            .objective(UPDATED_OBJECTIVE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .authors(UPDATED_AUTHORS)
            .facilitationDifficulty(UPDATED_FACILITATION_DIFFICULTY)
            .mobiusLoopTag(UPDATED_MOBIUS_LOOP_TAG)
            .what(UPDATED_WHAT)
            .why(UPDATED_WHY)
            .how(UPDATED_HOW)
            .numberOfPeopleRequired(UPDATED_NUMBER_OF_PEOPLE_REQUIRED)
            .timeLength(UPDATED_TIME_LENGTH)
            .expectedParticipants(UPDATED_EXPECTED_PARTICIPANTS);
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(updatedPracticeItem);

        restPracticeItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, practiceItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(practiceItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the PracticeItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPracticeItemToMatchAllProperties(updatedPracticeItem);
    }

    @Test
    @Transactional
    void putNonExistingPracticeItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practiceItem.setId(longCount.incrementAndGet());

        // Create the PracticeItem
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(practiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPracticeItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, practiceItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(practiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PracticeItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPracticeItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practiceItem.setId(longCount.incrementAndGet());

        // Create the PracticeItem
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(practiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPracticeItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(practiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PracticeItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPracticeItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practiceItem.setId(longCount.incrementAndGet());

        // Create the PracticeItem
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(practiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPracticeItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(practiceItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PracticeItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePracticeItemWithPatch() throws Exception {
        // Initialize the database
        insertedPracticeItem = practiceItemRepository.saveAndFlush(practiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the practiceItem using partial update
        PracticeItem partialUpdatedPracticeItem = new PracticeItem();
        partialUpdatedPracticeItem.setId(practiceItem.getId());

        partialUpdatedPracticeItem
            .objective(UPDATED_OBJECTIVE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .approvedDate(UPDATED_APPROVED_DATE)
            .facilitationDifficulty(UPDATED_FACILITATION_DIFFICULTY)
            .mobiusLoopTag(UPDATED_MOBIUS_LOOP_TAG)
            .how(UPDATED_HOW)
            .numberOfPeopleRequired(UPDATED_NUMBER_OF_PEOPLE_REQUIRED);

        restPracticeItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPracticeItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPracticeItem))
            )
            .andExpect(status().isOk());

        // Validate the PracticeItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPracticeItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPracticeItem, practiceItem),
            getPersistedPracticeItem(practiceItem)
        );
    }

    @Test
    @Transactional
    void fullUpdatePracticeItemWithPatch() throws Exception {
        // Initialize the database
        insertedPracticeItem = practiceItemRepository.saveAndFlush(practiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the practiceItem using partial update
        PracticeItem partialUpdatedPracticeItem = new PracticeItem();
        partialUpdatedPracticeItem.setId(practiceItem.getId());

        partialUpdatedPracticeItem
            .title(UPDATED_TITLE)
            .objective(UPDATED_OBJECTIVE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .publishedBy(UPDATED_PUBLISHED_BY)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedDate(UPDATED_APPROVED_DATE)
            .authors(UPDATED_AUTHORS)
            .facilitationDifficulty(UPDATED_FACILITATION_DIFFICULTY)
            .mobiusLoopTag(UPDATED_MOBIUS_LOOP_TAG)
            .what(UPDATED_WHAT)
            .why(UPDATED_WHY)
            .how(UPDATED_HOW)
            .numberOfPeopleRequired(UPDATED_NUMBER_OF_PEOPLE_REQUIRED)
            .timeLength(UPDATED_TIME_LENGTH)
            .expectedParticipants(UPDATED_EXPECTED_PARTICIPANTS);

        restPracticeItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPracticeItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPracticeItem))
            )
            .andExpect(status().isOk());

        // Validate the PracticeItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPracticeItemUpdatableFieldsEquals(partialUpdatedPracticeItem, getPersistedPracticeItem(partialUpdatedPracticeItem));
    }

    @Test
    @Transactional
    void patchNonExistingPracticeItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practiceItem.setId(longCount.incrementAndGet());

        // Create the PracticeItem
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(practiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPracticeItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, practiceItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(practiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PracticeItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPracticeItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practiceItem.setId(longCount.incrementAndGet());

        // Create the PracticeItem
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(practiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPracticeItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(practiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PracticeItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPracticeItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practiceItem.setId(longCount.incrementAndGet());

        // Create the PracticeItem
        PracticeItemDTO practiceItemDTO = practiceItemMapper.toDto(practiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPracticeItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(practiceItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PracticeItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePracticeItem() throws Exception {
        // Initialize the database
        insertedPracticeItem = practiceItemRepository.saveAndFlush(practiceItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the practiceItem
        restPracticeItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, practiceItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return practiceItemRepository.count();
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

    protected PracticeItem getPersistedPracticeItem(PracticeItem practiceItem) {
        return practiceItemRepository.findById(practiceItem.getId()).orElseThrow();
    }

    protected void assertPersistedPracticeItemToMatchAllProperties(PracticeItem expectedPracticeItem) {
        assertPracticeItemAllPropertiesEquals(expectedPracticeItem, getPersistedPracticeItem(expectedPracticeItem));
    }

    protected void assertPersistedPracticeItemToMatchUpdatableProperties(PracticeItem expectedPracticeItem) {
        assertPracticeItemAllUpdatablePropertiesEquals(expectedPracticeItem, getPersistedPracticeItem(expectedPracticeItem));
    }
}
