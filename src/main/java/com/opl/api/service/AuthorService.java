package com.opl.api.service;

import com.opl.api.domain.Author;
import com.opl.api.repository.AuthorRepository;
import com.opl.api.service.dto.AuthorDTO;
import com.opl.api.service.mapper.AuthorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.opl.api.domain.Author}.
 */
@Service
@Transactional
public class AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    /**
     * Save a author.
     *
     * @param authorDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorDTO save(AuthorDTO authorDTO) {
        log.debug("Request to save Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    /**
     * Update a author.
     *
     * @param authorDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorDTO update(AuthorDTO authorDTO) {
        log.debug("Request to update Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    /**
     * Partially update a author.
     *
     * @param authorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AuthorDTO> partialUpdate(AuthorDTO authorDTO) {
        log.debug("Request to partially update Author : {}", authorDTO);

        return authorRepository
            .findById(authorDTO.getId())
            .map(existingAuthor -> {
                authorMapper.partialUpdate(existingAuthor, authorDTO);

                return existingAuthor;
            })
            .map(authorRepository::save)
            .map(authorMapper::toDto);
    }

    /**
     * Get all the authors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> findAll() {
        log.debug("Request to get all Authors");
        return authorRepository.findAll().stream().map(authorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one author by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    /**
     * Delete the author by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.deleteById(id);
    }
}
