package com.opl.api.web.rest;

import com.opl.api.repository.LinkItemRepository;
import com.opl.api.service.LinkItemService;
import com.opl.api.service.dto.LinkItemDTO;
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
 * REST controller for managing {@link com.opl.api.domain.LinkItem}.
 */
@RestController
@RequestMapping("/api/link-items")
public class LinkItemResource {

    private final Logger log = LoggerFactory.getLogger(LinkItemResource.class);

    private static final String ENTITY_NAME = "linkItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LinkItemService linkItemService;

    private final LinkItemRepository linkItemRepository;

    public LinkItemResource(LinkItemService linkItemService, LinkItemRepository linkItemRepository) {
        this.linkItemService = linkItemService;
        this.linkItemRepository = linkItemRepository;
    }

    /**
     * {@code POST  /link-items} : Create a new linkItem.
     *
     * @param linkItemDTO the linkItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new linkItemDTO, or with status {@code 400 (Bad Request)} if the linkItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LinkItemDTO> createLinkItem(@RequestBody LinkItemDTO linkItemDTO) throws URISyntaxException {
        log.debug("REST request to save LinkItem : {}", linkItemDTO);
        if (linkItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new linkItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        linkItemDTO = linkItemService.save(linkItemDTO);
        return ResponseEntity.created(new URI("/api/link-items/" + linkItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, linkItemDTO.getId().toString()))
            .body(linkItemDTO);
    }

    /**
     * {@code PUT  /link-items/:id} : Updates an existing linkItem.
     *
     * @param id the id of the linkItemDTO to save.
     * @param linkItemDTO the linkItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated linkItemDTO,
     * or with status {@code 400 (Bad Request)} if the linkItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the linkItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LinkItemDTO> updateLinkItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LinkItemDTO linkItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LinkItem : {}, {}", id, linkItemDTO);
        if (linkItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, linkItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!linkItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        linkItemDTO = linkItemService.update(linkItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, linkItemDTO.getId().toString()))
            .body(linkItemDTO);
    }

    /**
     * {@code PATCH  /link-items/:id} : Partial updates given fields of an existing linkItem, field will ignore if it is null
     *
     * @param id the id of the linkItemDTO to save.
     * @param linkItemDTO the linkItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated linkItemDTO,
     * or with status {@code 400 (Bad Request)} if the linkItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the linkItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the linkItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LinkItemDTO> partialUpdateLinkItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LinkItemDTO linkItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LinkItem partially : {}, {}", id, linkItemDTO);
        if (linkItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, linkItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!linkItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LinkItemDTO> result = linkItemService.partialUpdate(linkItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, linkItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /link-items} : get all the linkItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of linkItems in body.
     */
    @GetMapping("")
    public List<LinkItemDTO> getAllLinkItems() {
        log.debug("REST request to get all LinkItems");
        return linkItemService.findAll();
    }

    /**
     * {@code GET  /link-items/:id} : get the "id" linkItem.
     *
     * @param id the id of the linkItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the linkItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LinkItemDTO> getLinkItem(@PathVariable("id") Long id) {
        log.debug("REST request to get LinkItem : {}", id);
        Optional<LinkItemDTO> linkItemDTO = linkItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(linkItemDTO);
    }

    /**
     * {@code DELETE  /link-items/:id} : delete the "id" linkItem.
     *
     * @param id the id of the linkItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLinkItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete LinkItem : {}", id);
        linkItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
