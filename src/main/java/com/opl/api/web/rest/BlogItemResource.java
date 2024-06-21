package com.opl.api.web.rest;

import com.opl.api.repository.BlogItemRepository;
import com.opl.api.service.BlogItemService;
import com.opl.api.service.dto.BlogItemDTO;
import com.opl.api.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.opl.api.domain.BlogItem}.
 */
@RestController
@RequestMapping("/api/blog-items")
public class BlogItemResource {

    private final Logger log = LoggerFactory.getLogger(BlogItemResource.class);

    private static final String ENTITY_NAME = "blogItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlogItemService blogItemService;

    private final BlogItemRepository blogItemRepository;

    public BlogItemResource(BlogItemService blogItemService, BlogItemRepository blogItemRepository) {
        this.blogItemService = blogItemService;
        this.blogItemRepository = blogItemRepository;
    }

    /**
     * {@code POST  /blog-items} : Create a new blogItem.
     *
     * @param blogItemDTO the blogItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blogItemDTO, or with status {@code 400 (Bad Request)} if the blogItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BlogItemDTO> createBlogItem(@RequestBody BlogItemDTO blogItemDTO) throws URISyntaxException {
        log.debug("REST request to save BlogItem : {}", blogItemDTO);
        if (blogItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new blogItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        blogItemDTO = blogItemService.save(blogItemDTO);
        return ResponseEntity.created(new URI("/api/blog-items/" + blogItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, blogItemDTO.getId().toString()))
            .body(blogItemDTO);
    }

    /**
     * {@code PUT  /blog-items/:id} : Updates an existing blogItem.
     *
     * @param id the id of the blogItemDTO to save.
     * @param blogItemDTO the blogItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blogItemDTO,
     * or with status {@code 400 (Bad Request)} if the blogItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blogItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BlogItemDTO> updateBlogItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BlogItemDTO blogItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BlogItem : {}, {}", id, blogItemDTO);
        if (blogItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blogItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        blogItemDTO = blogItemService.update(blogItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogItemDTO.getId().toString()))
            .body(blogItemDTO);
    }

    /**
     * {@code PATCH  /blog-items/:id} : Partial updates given fields of an existing blogItem, field will ignore if it is null
     *
     * @param id the id of the blogItemDTO to save.
     * @param blogItemDTO the blogItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blogItemDTO,
     * or with status {@code 400 (Bad Request)} if the blogItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the blogItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the blogItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BlogItemDTO> partialUpdateBlogItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BlogItemDTO blogItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BlogItem partially : {}, {}", id, blogItemDTO);
        if (blogItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blogItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlogItemDTO> result = blogItemService.partialUpdate(blogItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /blog-items} : get all the blogItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blogItems in body.
     */
    @GetMapping("")
    public List<BlogItemDTO> getAllBlogItems() {
        log.debug("REST request to get all BlogItems");
        return blogItemService.findAll();
    }

    /**
     * {@code GET  /blog-items/:id} : get the "id" blogItem.
     *
     * @param id the id of the blogItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blogItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BlogItemDTO> getBlogItem(@PathVariable("id") Long id) {
        log.debug("REST request to get BlogItem : {}", id);
        Optional<BlogItemDTO> blogItemDTO = blogItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blogItemDTO);
    }

    /**
     * {@code DELETE  /blog-items/:id} : delete the "id" blogItem.
     *
     * @param id the id of the blogItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete BlogItem : {}", id);
        blogItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
