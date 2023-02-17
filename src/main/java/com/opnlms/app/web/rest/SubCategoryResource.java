package com.opnlms.app.web.rest;

import com.opnlms.app.domain.SubCategory;
import com.opnlms.app.repository.SubCategoryRepository;
import com.opnlms.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.opnlms.app.domain.SubCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SubCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubCategoryResource.class);

    private static final String ENTITY_NAME = "subCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryResource(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    /**
     * {@code POST  /sub-categories} : Create a new subCategory.
     *
     * @param subCategory the subCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subCategory, or with status {@code 400 (Bad Request)} if the subCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-categories")
    public Mono<ResponseEntity<SubCategory>> createSubCategory(@Valid @RequestBody SubCategory subCategory) throws URISyntaxException {
        log.debug("REST request to save SubCategory : {}", subCategory);
        if (subCategory.getId() != null) {
            throw new BadRequestAlertException("A new subCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return subCategoryRepository
            .save(subCategory)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/sub-categories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sub-categories/:id} : Updates an existing subCategory.
     *
     * @param id the id of the subCategory to save.
     * @param subCategory the subCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategory,
     * or with status {@code 400 (Bad Request)} if the subCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-categories/{id}")
    public Mono<ResponseEntity<SubCategory>> updateSubCategory(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody SubCategory subCategory
    ) throws URISyntaxException {
        log.debug("REST request to update SubCategory : {}, {}", id, subCategory);
        if (subCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return subCategoryRepository
                    .save(subCategory.setIsPersisted())
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /sub-categories/:id} : Partial updates given fields of an existing subCategory, field will ignore if it is null
     *
     * @param id the id of the subCategory to save.
     * @param subCategory the subCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategory,
     * or with status {@code 400 (Bad Request)} if the subCategory is not valid,
     * or with status {@code 404 (Not Found)} if the subCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the subCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sub-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SubCategory>> partialUpdateSubCategory(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody SubCategory subCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubCategory partially : {}, {}", id, subCategory);
        if (subCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SubCategory> result = subCategoryRepository
                    .findById(subCategory.getId())
                    .map(existingSubCategory -> {
                        if (subCategory.getName() != null) {
                            existingSubCategory.setName(subCategory.getName());
                        }
                        if (subCategory.getDescription() != null) {
                            existingSubCategory.setDescription(subCategory.getDescription());
                        }

                        return existingSubCategory;
                    })
                    .flatMap(subCategoryRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /sub-categories} : get all the subCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCategories in body.
     */
    @GetMapping("/sub-categories")
    public Mono<List<SubCategory>> getAllSubCategories() {
        log.debug("REST request to get all SubCategories");
        return subCategoryRepository.findAll().collectList();
    }

    /**
     * {@code GET  /sub-categories} : get all the subCategories as a stream.
     * @return the {@link Flux} of subCategories.
     */
    @GetMapping(value = "/sub-categories", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SubCategory> getAllSubCategoriesAsStream() {
        log.debug("REST request to get all SubCategories as a stream");
        return subCategoryRepository.findAll();
    }

    /**
     * {@code GET  /sub-categories/:id} : get the "id" subCategory.
     *
     * @param id the id of the subCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-categories/{id}")
    public Mono<ResponseEntity<SubCategory>> getSubCategory(@PathVariable String id) {
        log.debug("REST request to get SubCategory : {}", id);
        Mono<SubCategory> subCategory = subCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subCategory);
    }

    /**
     * {@code DELETE  /sub-categories/:id} : delete the "id" subCategory.
     *
     * @param id the id of the subCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-categories/{id}")
    public Mono<ResponseEntity<Void>> deleteSubCategory(@PathVariable String id) {
        log.debug("REST request to delete SubCategory : {}", id);
        return subCategoryRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
