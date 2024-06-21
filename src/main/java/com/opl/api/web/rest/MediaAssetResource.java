package com.opl.api.web.rest;

import com.opl.api.repository.MediaAssetRepository;
import com.opl.api.service.MediaAssetService;
import com.opl.api.service.dto.MediaAssetDTO;
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
 * REST controller for managing {@link com.opl.api.domain.MediaAsset}.
 */
@RestController
@RequestMapping("/api/media-assets")
public class MediaAssetResource {

    private final Logger log = LoggerFactory.getLogger(MediaAssetResource.class);

    private static final String ENTITY_NAME = "mediaAsset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MediaAssetService mediaAssetService;

    private final MediaAssetRepository mediaAssetRepository;

    public MediaAssetResource(MediaAssetService mediaAssetService, MediaAssetRepository mediaAssetRepository) {
        this.mediaAssetService = mediaAssetService;
        this.mediaAssetRepository = mediaAssetRepository;
    }

    /**
     * {@code POST  /media-assets} : Create a new mediaAsset.
     *
     * @param mediaAssetDTO the mediaAssetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mediaAssetDTO, or with status {@code 400 (Bad Request)} if the mediaAsset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MediaAssetDTO> createMediaAsset(@RequestBody MediaAssetDTO mediaAssetDTO) throws URISyntaxException {
        log.debug("REST request to save MediaAsset : {}", mediaAssetDTO);
        if (mediaAssetDTO.getId() != null) {
            throw new BadRequestAlertException("A new mediaAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mediaAssetDTO = mediaAssetService.save(mediaAssetDTO);
        return ResponseEntity.created(new URI("/api/media-assets/" + mediaAssetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mediaAssetDTO.getId().toString()))
            .body(mediaAssetDTO);
    }

    /**
     * {@code PUT  /media-assets/:id} : Updates an existing mediaAsset.
     *
     * @param id the id of the mediaAssetDTO to save.
     * @param mediaAssetDTO the mediaAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mediaAssetDTO,
     * or with status {@code 400 (Bad Request)} if the mediaAssetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mediaAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MediaAssetDTO> updateMediaAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MediaAssetDTO mediaAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MediaAsset : {}, {}", id, mediaAssetDTO);
        if (mediaAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mediaAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mediaAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mediaAssetDTO = mediaAssetService.update(mediaAssetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mediaAssetDTO.getId().toString()))
            .body(mediaAssetDTO);
    }

    /**
     * {@code PATCH  /media-assets/:id} : Partial updates given fields of an existing mediaAsset, field will ignore if it is null
     *
     * @param id the id of the mediaAssetDTO to save.
     * @param mediaAssetDTO the mediaAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mediaAssetDTO,
     * or with status {@code 400 (Bad Request)} if the mediaAssetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mediaAssetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mediaAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MediaAssetDTO> partialUpdateMediaAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MediaAssetDTO mediaAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MediaAsset partially : {}, {}", id, mediaAssetDTO);
        if (mediaAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mediaAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mediaAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MediaAssetDTO> result = mediaAssetService.partialUpdate(mediaAssetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mediaAssetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /media-assets} : get all the mediaAssets.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mediaAssets in body.
     */
    @GetMapping("")
    public List<MediaAssetDTO> getAllMediaAssets(@RequestParam(name = "filter", required = false) String filter) {
        if ("practiceitem-is-null".equals(filter)) {
            log.debug("REST request to get all MediaAssets where practiceItem is null");
            return mediaAssetService.findAllWherePracticeItemIsNull();
        }

        if ("blogitem-is-null".equals(filter)) {
            log.debug("REST request to get all MediaAssets where blogItem is null");
            return mediaAssetService.findAllWhereBlogItemIsNull();
        }

        if ("pageitem-is-null".equals(filter)) {
            log.debug("REST request to get all MediaAssets where pageItem is null");
            return mediaAssetService.findAllWherePageItemIsNull();
        }
        log.debug("REST request to get all MediaAssets");
        return mediaAssetService.findAll();
    }

    /**
     * {@code GET  /media-assets/:id} : get the "id" mediaAsset.
     *
     * @param id the id of the mediaAssetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mediaAssetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MediaAssetDTO> getMediaAsset(@PathVariable("id") Long id) {
        log.debug("REST request to get MediaAsset : {}", id);
        Optional<MediaAssetDTO> mediaAssetDTO = mediaAssetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mediaAssetDTO);
    }

    /**
     * {@code DELETE  /media-assets/:id} : delete the "id" mediaAsset.
     *
     * @param id the id of the mediaAssetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaAsset(@PathVariable("id") Long id) {
        log.debug("REST request to delete MediaAsset : {}", id);
        mediaAssetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
