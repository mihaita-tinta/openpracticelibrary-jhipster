package com.opl.api.service;

import com.opl.api.domain.LinkItem;
import com.opl.api.repository.LinkItemRepository;
import com.opl.api.service.dto.LinkItemDTO;
import com.opl.api.service.mapper.LinkItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.opl.api.domain.LinkItem}.
 */
@Service
@Transactional
public class LinkItemService {

    private final Logger log = LoggerFactory.getLogger(LinkItemService.class);

    private final LinkItemRepository linkItemRepository;

    private final LinkItemMapper linkItemMapper;

    public LinkItemService(LinkItemRepository linkItemRepository, LinkItemMapper linkItemMapper) {
        this.linkItemRepository = linkItemRepository;
        this.linkItemMapper = linkItemMapper;
    }

    /**
     * Save a linkItem.
     *
     * @param linkItemDTO the entity to save.
     * @return the persisted entity.
     */
    public LinkItemDTO save(LinkItemDTO linkItemDTO) {
        log.debug("Request to save LinkItem : {}", linkItemDTO);
        LinkItem linkItem = linkItemMapper.toEntity(linkItemDTO);
        linkItem = linkItemRepository.save(linkItem);
        return linkItemMapper.toDto(linkItem);
    }

    /**
     * Update a linkItem.
     *
     * @param linkItemDTO the entity to save.
     * @return the persisted entity.
     */
    public LinkItemDTO update(LinkItemDTO linkItemDTO) {
        log.debug("Request to update LinkItem : {}", linkItemDTO);
        LinkItem linkItem = linkItemMapper.toEntity(linkItemDTO);
        linkItem = linkItemRepository.save(linkItem);
        return linkItemMapper.toDto(linkItem);
    }

    /**
     * Partially update a linkItem.
     *
     * @param linkItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LinkItemDTO> partialUpdate(LinkItemDTO linkItemDTO) {
        log.debug("Request to partially update LinkItem : {}", linkItemDTO);

        return linkItemRepository
            .findById(linkItemDTO.getId())
            .map(existingLinkItem -> {
                linkItemMapper.partialUpdate(existingLinkItem, linkItemDTO);

                return existingLinkItem;
            })
            .map(linkItemRepository::save)
            .map(linkItemMapper::toDto);
    }

    /**
     * Get all the linkItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LinkItemDTO> findAll() {
        log.debug("Request to get all LinkItems");
        return linkItemRepository.findAll().stream().map(linkItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one linkItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LinkItemDTO> findOne(Long id) {
        log.debug("Request to get LinkItem : {}", id);
        return linkItemRepository.findById(id).map(linkItemMapper::toDto);
    }

    /**
     * Delete the linkItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LinkItem : {}", id);
        linkItemRepository.deleteById(id);
    }
}
