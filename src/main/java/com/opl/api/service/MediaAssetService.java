package com.opl.api.service;

import com.opl.api.domain.MediaAsset;
import com.opl.api.repository.MediaAssetRepository;
import com.opl.api.service.dto.MediaAssetDTO;
import com.opl.api.service.mapper.MediaAssetMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.opl.api.domain.MediaAsset}.
 */
@Service
@Transactional
public class MediaAssetService {

    private final Logger log = LoggerFactory.getLogger(MediaAssetService.class);

    private final MediaAssetRepository mediaAssetRepository;

    private final MediaAssetMapper mediaAssetMapper;

    public MediaAssetService(MediaAssetRepository mediaAssetRepository, MediaAssetMapper mediaAssetMapper) {
        this.mediaAssetRepository = mediaAssetRepository;
        this.mediaAssetMapper = mediaAssetMapper;
    }

    /**
     * Save a mediaAsset.
     *
     * @param mediaAssetDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaAssetDTO save(MediaAssetDTO mediaAssetDTO) {
        log.debug("Request to save MediaAsset : {}", mediaAssetDTO);
        MediaAsset mediaAsset = mediaAssetMapper.toEntity(mediaAssetDTO);
        mediaAsset = mediaAssetRepository.save(mediaAsset);
        return mediaAssetMapper.toDto(mediaAsset);
    }

    /**
     * Update a mediaAsset.
     *
     * @param mediaAssetDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaAssetDTO update(MediaAssetDTO mediaAssetDTO) {
        log.debug("Request to update MediaAsset : {}", mediaAssetDTO);
        MediaAsset mediaAsset = mediaAssetMapper.toEntity(mediaAssetDTO);
        mediaAsset = mediaAssetRepository.save(mediaAsset);
        return mediaAssetMapper.toDto(mediaAsset);
    }

    /**
     * Partially update a mediaAsset.
     *
     * @param mediaAssetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MediaAssetDTO> partialUpdate(MediaAssetDTO mediaAssetDTO) {
        log.debug("Request to partially update MediaAsset : {}", mediaAssetDTO);

        return mediaAssetRepository
            .findById(mediaAssetDTO.getId())
            .map(existingMediaAsset -> {
                mediaAssetMapper.partialUpdate(existingMediaAsset, mediaAssetDTO);

                return existingMediaAsset;
            })
            .map(mediaAssetRepository::save)
            .map(mediaAssetMapper::toDto);
    }

    /**
     * Get all the mediaAssets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MediaAssetDTO> findAll() {
        log.debug("Request to get all MediaAssets");
        return mediaAssetRepository.findAll().stream().map(mediaAssetMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the mediaAssets where PracticeItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MediaAssetDTO> findAllWherePracticeItemIsNull() {
        log.debug("Request to get all mediaAssets where PracticeItem is null");
        return StreamSupport.stream(mediaAssetRepository.findAll().spliterator(), false)
            .filter(mediaAsset -> mediaAsset.getPracticeItem() == null)
            .map(mediaAssetMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the mediaAssets where BlogItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MediaAssetDTO> findAllWhereBlogItemIsNull() {
        log.debug("Request to get all mediaAssets where BlogItem is null");
        return StreamSupport.stream(mediaAssetRepository.findAll().spliterator(), false)
            .filter(mediaAsset -> mediaAsset.getBlogItem() == null)
            .map(mediaAssetMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the mediaAssets where PageItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MediaAssetDTO> findAllWherePageItemIsNull() {
        log.debug("Request to get all mediaAssets where PageItem is null");
        return StreamSupport.stream(mediaAssetRepository.findAll().spliterator(), false)
            .filter(mediaAsset -> mediaAsset.getPageItem() == null)
            .map(mediaAssetMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one mediaAsset by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MediaAssetDTO> findOne(Long id) {
        log.debug("Request to get MediaAsset : {}", id);
        return mediaAssetRepository.findById(id).map(mediaAssetMapper::toDto);
    }

    /**
     * Delete the mediaAsset by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MediaAsset : {}", id);
        mediaAssetRepository.deleteById(id);
    }
}
