package com.opl.api.web.rest;

import static com.opl.api.domain.MediaAssetAsserts.*;
import static com.opl.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opl.api.IntegrationTest;
import com.opl.api.domain.MediaAsset;
import com.opl.api.domain.enumeration.MediaAssetType;
import com.opl.api.repository.MediaAssetRepository;
import com.opl.api.service.dto.MediaAssetDTO;
import com.opl.api.service.mapper.MediaAssetMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link MediaAssetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MediaAssetResourceIT {

    private static final MediaAssetType DEFAULT_TYPE = MediaAssetType.IMAGE;
    private static final MediaAssetType UPDATED_TYPE = MediaAssetType.URL;

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_SORT_INDEX = 1;
    private static final Integer UPDATED_SORT_INDEX = 2;

    private static final String ENTITY_API_URL = "/api/media-assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MediaAssetRepository mediaAssetRepository;

    @Autowired
    private MediaAssetMapper mediaAssetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMediaAssetMockMvc;

    private MediaAsset mediaAsset;

    private MediaAsset insertedMediaAsset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MediaAsset createEntity(EntityManager em) {
        MediaAsset mediaAsset = new MediaAsset()
            .type(DEFAULT_TYPE)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .sortIndex(DEFAULT_SORT_INDEX);
        return mediaAsset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MediaAsset createUpdatedEntity(EntityManager em) {
        MediaAsset mediaAsset = new MediaAsset()
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .sortIndex(UPDATED_SORT_INDEX);
        return mediaAsset;
    }

    @BeforeEach
    public void initTest() {
        mediaAsset = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMediaAsset != null) {
            mediaAssetRepository.delete(insertedMediaAsset);
            insertedMediaAsset = null;
        }
    }

    @Test
    @Transactional
    void createMediaAsset() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MediaAsset
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(mediaAsset);
        var returnedMediaAssetDTO = om.readValue(
            restMediaAssetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaAssetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MediaAssetDTO.class
        );

        // Validate the MediaAsset in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMediaAsset = mediaAssetMapper.toEntity(returnedMediaAssetDTO);
        assertMediaAssetUpdatableFieldsEquals(returnedMediaAsset, getPersistedMediaAsset(returnedMediaAsset));

        insertedMediaAsset = returnedMediaAsset;
    }

    @Test
    @Transactional
    void createMediaAssetWithExistingId() throws Exception {
        // Create the MediaAsset with an existing ID
        mediaAsset.setId(1L);
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(mediaAsset);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediaAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaAssetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MediaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMediaAssets() throws Exception {
        // Initialize the database
        insertedMediaAsset = mediaAssetRepository.saveAndFlush(mediaAsset);

        // Get all the mediaAssetList
        restMediaAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mediaAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].sortIndex").value(hasItem(DEFAULT_SORT_INDEX)));
    }

    @Test
    @Transactional
    void getMediaAsset() throws Exception {
        // Initialize the database
        insertedMediaAsset = mediaAssetRepository.saveAndFlush(mediaAsset);

        // Get the mediaAsset
        restMediaAssetMockMvc
            .perform(get(ENTITY_API_URL_ID, mediaAsset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mediaAsset.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.sortIndex").value(DEFAULT_SORT_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingMediaAsset() throws Exception {
        // Get the mediaAsset
        restMediaAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMediaAsset() throws Exception {
        // Initialize the database
        insertedMediaAsset = mediaAssetRepository.saveAndFlush(mediaAsset);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mediaAsset
        MediaAsset updatedMediaAsset = mediaAssetRepository.findById(mediaAsset.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMediaAsset are not directly saved in db
        em.detach(updatedMediaAsset);
        updatedMediaAsset
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .sortIndex(UPDATED_SORT_INDEX);
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(updatedMediaAsset);

        restMediaAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mediaAssetDTO))
            )
            .andExpect(status().isOk());

        // Validate the MediaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMediaAssetToMatchAllProperties(updatedMediaAsset);
    }

    @Test
    @Transactional
    void putNonExistingMediaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaAsset.setId(longCount.incrementAndGet());

        // Create the MediaAsset
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(mediaAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mediaAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MediaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMediaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaAsset.setId(longCount.incrementAndGet());

        // Create the MediaAsset
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(mediaAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mediaAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MediaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMediaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaAsset.setId(longCount.incrementAndGet());

        // Create the MediaAsset
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(mediaAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaAssetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaAssetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MediaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMediaAssetWithPatch() throws Exception {
        // Initialize the database
        insertedMediaAsset = mediaAssetRepository.saveAndFlush(mediaAsset);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mediaAsset using partial update
        MediaAsset partialUpdatedMediaAsset = new MediaAsset();
        partialUpdatedMediaAsset.setId(mediaAsset.getId());

        partialUpdatedMediaAsset.sortIndex(UPDATED_SORT_INDEX);

        restMediaAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMediaAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMediaAsset))
            )
            .andExpect(status().isOk());

        // Validate the MediaAsset in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMediaAssetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMediaAsset, mediaAsset),
            getPersistedMediaAsset(mediaAsset)
        );
    }

    @Test
    @Transactional
    void fullUpdateMediaAssetWithPatch() throws Exception {
        // Initialize the database
        insertedMediaAsset = mediaAssetRepository.saveAndFlush(mediaAsset);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mediaAsset using partial update
        MediaAsset partialUpdatedMediaAsset = new MediaAsset();
        partialUpdatedMediaAsset.setId(mediaAsset.getId());

        partialUpdatedMediaAsset
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .sortIndex(UPDATED_SORT_INDEX);

        restMediaAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMediaAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMediaAsset))
            )
            .andExpect(status().isOk());

        // Validate the MediaAsset in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMediaAssetUpdatableFieldsEquals(partialUpdatedMediaAsset, getPersistedMediaAsset(partialUpdatedMediaAsset));
    }

    @Test
    @Transactional
    void patchNonExistingMediaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaAsset.setId(longCount.incrementAndGet());

        // Create the MediaAsset
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(mediaAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mediaAssetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mediaAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MediaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMediaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaAsset.setId(longCount.incrementAndGet());

        // Create the MediaAsset
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(mediaAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mediaAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MediaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMediaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mediaAsset.setId(longCount.incrementAndGet());

        // Create the MediaAsset
        MediaAssetDTO mediaAssetDTO = mediaAssetMapper.toDto(mediaAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaAssetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mediaAssetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MediaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMediaAsset() throws Exception {
        // Initialize the database
        insertedMediaAsset = mediaAssetRepository.saveAndFlush(mediaAsset);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mediaAsset
        restMediaAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, mediaAsset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mediaAssetRepository.count();
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

    protected MediaAsset getPersistedMediaAsset(MediaAsset mediaAsset) {
        return mediaAssetRepository.findById(mediaAsset.getId()).orElseThrow();
    }

    protected void assertPersistedMediaAssetToMatchAllProperties(MediaAsset expectedMediaAsset) {
        assertMediaAssetAllPropertiesEquals(expectedMediaAsset, getPersistedMediaAsset(expectedMediaAsset));
    }

    protected void assertPersistedMediaAssetToMatchUpdatableProperties(MediaAsset expectedMediaAsset) {
        assertMediaAssetAllUpdatablePropertiesEquals(expectedMediaAsset, getPersistedMediaAsset(expectedMediaAsset));
    }
}
