package com.opl.api.service;

import com.opl.api.domain.PracticeItem;
import com.opl.api.repository.PracticeItemRepository;
import com.opl.api.service.dto.PracticeItemDTO;
import com.opl.api.service.mapper.PracticeItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.opl.api.domain.PracticeItem}.
 */
@Service
@Transactional
public class PracticeItemService {

    private final Logger log = LoggerFactory.getLogger(PracticeItemService.class);

    private final PracticeItemRepository practiceItemRepository;

    private final PracticeItemMapper practiceItemMapper;

    public PracticeItemService(PracticeItemRepository practiceItemRepository, PracticeItemMapper practiceItemMapper) {
        this.practiceItemRepository = practiceItemRepository;
        this.practiceItemMapper = practiceItemMapper;
    }

    /**
     * Save a practiceItem.
     *
     * @param practiceItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PracticeItemDTO save(PracticeItemDTO practiceItemDTO) {
        log.debug("Request to save PracticeItem : {}", practiceItemDTO);
        PracticeItem practiceItem = practiceItemMapper.toEntity(practiceItemDTO);
        practiceItem = practiceItemRepository.save(practiceItem);
        return practiceItemMapper.toDto(practiceItem);
    }

    /**
     * Update a practiceItem.
     *
     * @param practiceItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PracticeItemDTO update(PracticeItemDTO practiceItemDTO) {
        log.debug("Request to update PracticeItem : {}", practiceItemDTO);
        PracticeItem practiceItem = practiceItemMapper.toEntity(practiceItemDTO);
        practiceItem = practiceItemRepository.save(practiceItem);
        return practiceItemMapper.toDto(practiceItem);
    }

    /**
     * Partially update a practiceItem.
     *
     * @param practiceItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PracticeItemDTO> partialUpdate(PracticeItemDTO practiceItemDTO) {
        log.debug("Request to partially update PracticeItem : {}", practiceItemDTO);

        return practiceItemRepository
            .findById(practiceItemDTO.getId())
            .map(existingPracticeItem -> {
                practiceItemMapper.partialUpdate(existingPracticeItem, practiceItemDTO);

                return existingPracticeItem;
            })
            .map(practiceItemRepository::save)
            .map(practiceItemMapper::toDto);
    }

    /**
     * Get all the practiceItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PracticeItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PracticeItems");
        return practiceItemRepository.findAll(pageable).map(practiceItemMapper::toDto);
    }

    /**
     * Get one practiceItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PracticeItemDTO> findOne(Long id) {
        log.debug("Request to get PracticeItem : {}", id);
        return practiceItemRepository.findById(id).map(practiceItemMapper::toDto);
    }

    /**
     * Delete the practiceItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PracticeItem : {}", id);
        practiceItemRepository.deleteById(id);
    }
}
