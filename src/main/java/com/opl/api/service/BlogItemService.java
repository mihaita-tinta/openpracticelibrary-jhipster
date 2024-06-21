package com.opl.api.service;

import com.opl.api.domain.BlogItem;
import com.opl.api.repository.BlogItemRepository;
import com.opl.api.service.dto.BlogItemDTO;
import com.opl.api.service.mapper.BlogItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.opl.api.domain.BlogItem}.
 */
@Service
@Transactional
public class BlogItemService {

    private final Logger log = LoggerFactory.getLogger(BlogItemService.class);

    private final BlogItemRepository blogItemRepository;

    private final BlogItemMapper blogItemMapper;

    public BlogItemService(BlogItemRepository blogItemRepository, BlogItemMapper blogItemMapper) {
        this.blogItemRepository = blogItemRepository;
        this.blogItemMapper = blogItemMapper;
    }

    /**
     * Save a blogItem.
     *
     * @param blogItemDTO the entity to save.
     * @return the persisted entity.
     */
    public BlogItemDTO save(BlogItemDTO blogItemDTO) {
        log.debug("Request to save BlogItem : {}", blogItemDTO);
        BlogItem blogItem = blogItemMapper.toEntity(blogItemDTO);
        blogItem = blogItemRepository.save(blogItem);
        return blogItemMapper.toDto(blogItem);
    }

    /**
     * Update a blogItem.
     *
     * @param blogItemDTO the entity to save.
     * @return the persisted entity.
     */
    public BlogItemDTO update(BlogItemDTO blogItemDTO) {
        log.debug("Request to update BlogItem : {}", blogItemDTO);
        BlogItem blogItem = blogItemMapper.toEntity(blogItemDTO);
        blogItem = blogItemRepository.save(blogItem);
        return blogItemMapper.toDto(blogItem);
    }

    /**
     * Partially update a blogItem.
     *
     * @param blogItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BlogItemDTO> partialUpdate(BlogItemDTO blogItemDTO) {
        log.debug("Request to partially update BlogItem : {}", blogItemDTO);

        return blogItemRepository
            .findById(blogItemDTO.getId())
            .map(existingBlogItem -> {
                blogItemMapper.partialUpdate(existingBlogItem, blogItemDTO);

                return existingBlogItem;
            })
            .map(blogItemRepository::save)
            .map(blogItemMapper::toDto);
    }

    /**
     * Get all the blogItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BlogItemDTO> findAll() {
        log.debug("Request to get all BlogItems");
        return blogItemRepository.findAll().stream().map(blogItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one blogItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BlogItemDTO> findOne(Long id) {
        log.debug("Request to get BlogItem : {}", id);
        return blogItemRepository.findById(id).map(blogItemMapper::toDto);
    }

    /**
     * Delete the blogItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BlogItem : {}", id);
        blogItemRepository.deleteById(id);
    }
}
