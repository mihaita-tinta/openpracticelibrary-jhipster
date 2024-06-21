package com.opl.api.web.rest;

import com.opl.api.repository.PageItemRepository;
import com.opl.api.service.PageItemService;
import com.opl.api.service.dto.PageItemDTO;
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
 * REST controller for managing {@link com.opl.api.domain.PageItem}.
 */
@RestController
@RequestMapping("/api/page-items")
public class PageItemResource {

    private final Logger log = LoggerFactory.getLogger(PageItemResource.class);

    private static final String ENTITY_NAME = "pageItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PageItemService pageItemService;

    private final PageItemRepository pageItemRepository;

    public PageItemResource(PageItemService pageItemService, PageItemRepository pageItemRepository) {
        this.pageItemService = pageItemService;
        this.pageItemRepository = pageItemRepository;
    }

    /**
     * {@code POST  /page-items} : Create a new pageItem.
     *
     * @param pageItemDTO the pageItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pageItemDTO, or with status {@code 400 (Bad Request)} if the pageItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PageItemDTO> createPageItem(@RequestBody PageItemDTO pageItemDTO) throws URISyntaxException {
        log.debug("REST request to save PageItem : {}", pageItemDTO);
        if (pageItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new pageItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pageItemDTO = pageItemService.save(pageItemDTO);
        return ResponseEntity.created(new URI("/api/page-items/" + pageItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pageItemDTO.getId().toString()))
            .body(pageItemDTO);
    }

    /**
     * {@code PUT  /page-items/:id} : Updates an existing pageItem.
     *
     * @param id the id of the pageItemDTO to save.
     * @param pageItemDTO the pageItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageItemDTO,
     * or with status {@code 400 (Bad Request)} if the pageItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pageItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PageItemDTO> updatePageItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PageItemDTO pageItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PageItem : {}, {}", id, pageItemDTO);
        if (pageItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pageItemDTO = pageItemService.update(pageItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pageItemDTO.getId().toString()))
            .body(pageItemDTO);
    }

    /**
     * {@code PATCH  /page-items/:id} : Partial updates given fields of an existing pageItem, field will ignore if it is null
     *
     * @param id the id of the pageItemDTO to save.
     * @param pageItemDTO the pageItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageItemDTO,
     * or with status {@code 400 (Bad Request)} if the pageItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pageItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pageItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PageItemDTO> partialUpdatePageItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PageItemDTO pageItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PageItem partially : {}, {}", id, pageItemDTO);
        if (pageItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PageItemDTO> result = pageItemService.partialUpdate(pageItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pageItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /page-items} : get all the pageItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pageItems in body.
     */
    @GetMapping("")
    public List<PageItemDTO> getAllPageItems() {
        log.debug("REST request to get all PageItems");
        return pageItemService.findAll();
    }

    /**
     * {@code GET  /page-items/:id} : get the "id" pageItem.
     *
     * @param id the id of the pageItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pageItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PageItemDTO> getPageItem(@PathVariable("id") Long id) {
        log.debug("REST request to get PageItem : {}", id);
        Optional<PageItemDTO> pageItemDTO = pageItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pageItemDTO);
    }

    /**
     * {@code DELETE  /page-items/:id} : delete the "id" pageItem.
     *
     * @param id the id of the pageItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePageItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete PageItem : {}", id);
        pageItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
