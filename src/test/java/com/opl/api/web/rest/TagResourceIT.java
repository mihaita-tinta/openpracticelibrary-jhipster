package com.opl.api.web.rest;

import static com.opl.api.domain.TagAsserts.*;
import static com.opl.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opl.api.IntegrationTest;
import com.opl.api.domain.Tag;
import com.opl.api.repository.TagRepository;
import com.opl.api.service.dto.TagDTO;
import com.opl.api.service.mapper.TagMapper;
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
 * Integration tests for the {@link TagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagMockMvc;

    private Tag tag;

    private Tag insertedTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity(EntityManager em) {
        Tag tag = new Tag().content(DEFAULT_CONTENT);
        return tag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createUpdatedEntity(EntityManager em) {
        Tag tag = new Tag().content(UPDATED_CONTENT);
        return tag;
    }

    @BeforeEach
    public void initTest() {
        tag = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTag != null) {
            tagRepository.delete(insertedTag);
            insertedTag = null;
        }
    }

    @Test
    @Transactional
    void createTag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);
        var returnedTagDTO = om.readValue(
            restTagMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TagDTO.class
        );

        // Validate the Tag in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTag = tagMapper.toEntity(returnedTagDTO);
        assertTagUpdatableFieldsEquals(returnedTag, getPersistedTag(returnedTag));

        insertedTag = returnedTag;
    }

    @Test
    @Transactional
    void createTagWithExistingId() throws Exception {
        // Create the Tag with an existing ID
        tag.setId(1L);
        TagDTO tagDTO = tagMapper.toDto(tag);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTags() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getTag() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get the tag
        restTagMockMvc
            .perform(get(ENTITY_API_URL_ID, tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTag() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tag
        Tag updatedTag = tagRepository.findById(tag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTag are not directly saved in db
        em.detach(updatedTag);
        updatedTag.content(UPDATED_CONTENT);
        TagDTO tagDTO = tagMapper.toDto(updatedTag);

        restTagMockMvc
            .perform(put(ENTITY_API_URL_ID, tagDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isOk());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTagToMatchAllProperties(updatedTag);
    }

    @Test
    @Transactional
    void putNonExistingTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(put(ENTITY_API_URL_ID, tagDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagWithPatch() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag.content(UPDATED_CONTENT);

        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTag, tag), getPersistedTag(tag));
    }

    @Test
    @Transactional
    void fullUpdateTagWithPatch() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag.content(UPDATED_CONTENT);

        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagUpdatableFieldsEquals(partialUpdatedTag, getPersistedTag(partialUpdatedTag));
    }

    @Test
    @Transactional
    void patchNonExistingTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTag() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tag
        restTagMockMvc.perform(delete(ENTITY_API_URL_ID, tag.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tagRepository.count();
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

    protected Tag getPersistedTag(Tag tag) {
        return tagRepository.findById(tag.getId()).orElseThrow();
    }

    protected void assertPersistedTagToMatchAllProperties(Tag expectedTag) {
        assertTagAllPropertiesEquals(expectedTag, getPersistedTag(expectedTag));
    }

    protected void assertPersistedTagToMatchUpdatableProperties(Tag expectedTag) {
        assertTagAllUpdatablePropertiesEquals(expectedTag, getPersistedTag(expectedTag));
    }
}