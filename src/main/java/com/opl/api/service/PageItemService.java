package com.opl.api.service;

import com.opl.api.domain.PageItem;
import com.opl.api.repository.PageItemRepository;
import com.opl.api.service.dto.PageItemDTO;
import com.opl.api.service.mapper.PageItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.opl.api.domain.PageItem}.
 */
@Service
@Transactional
public class PageItemService {

    private final Logger log = LoggerFactory.getLogger(PageItemService.class);

    private final PageItemRepository pageItemRepository;

    private final PageItemMapper pageItemMapper;

    public PageItemService(PageItemRepository pageItemRepository, PageItemMapper pageItemMapper) {
        this.pageItemRepository = pageItemRepository;
        this.pageItemMapper = pageItemMapper;
    }

    /**
     * Save a pageItem.
     *
     * @param pageItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PageItemDTO save(PageItemDTO pageItemDTO) {
        log.debug("Request to save PageItem : {}", pageItemDTO);
        PageItem pageItem = pageItemMapper.toEntity(pageItemDTO);
        pageItem = pageItemRepository.save(pageItem);
        return pageItemMapper.toDto(pageItem);
    }

    /**
     * Update a pageItem.
     *
     * @param pageItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PageItemDTO update(PageItemDTO pageItemDTO) {
        log.debug("Request to update PageItem : {}", pageItemDTO);
        PageItem pageItem = pageItemMapper.toEntity(pageItemDTO);
        pageItem = pageItemRepository.save(pageItem);
        return pageItemMapper.toDto(pageItem);
    }

    /**
     * Partially update a pageItem.
     *
     * @param pageItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PageItemDTO> partialUpdate(PageItemDTO pageItemDTO) {
        log.debug("Request to partially update PageItem : {}", pageItemDTO);

        return pageItemRepository
            .findById(pageItemDTO.getId())
            .map(existingPageItem -> {
                pageItemMapper.partialUpdate(existingPageItem, pageItemDTO);

                return existingPageItem;
            })
            .map(pageItemRepository::save)
            .map(pageItemMapper::toDto);
    }

    /**
     * Get all the pageItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PageItemDTO> findAll() {
        log.debug("Request to get all PageItems");
        return pageItemRepository.findAll().stream().map(pageItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one pageItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PageItemDTO> findOne(Long id) {
        log.debug("Request to get PageItem : {}", id);
        return pageItemRepository.findById(id).map(pageItemMapper::toDto);
    }

    /**
     * Delete the pageItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PageItem : {}", id);
        pageItemRepository.deleteById(id);
    }
}
