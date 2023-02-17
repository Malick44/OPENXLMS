package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Organisation;
import com.opnlms.app.repository.OrganisationRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Organisation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrganisationResource {

    private final Logger log = LoggerFactory.getLogger(OrganisationResource.class);

    private static final String ENTITY_NAME = "organisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganisationRepository organisationRepository;

    public OrganisationResource(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    /**
     * {@code POST  /organisations} : Create a new organisation.
     *
     * @param organisation the organisation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organisation, or with status {@code 400 (Bad Request)} if the organisation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organisations")
    public Mono<ResponseEntity<Organisation>> createOrganisation(@Valid @RequestBody Organisation organisation) throws URISyntaxException {
        log.debug("REST request to save Organisation : {}", organisation);
        if (organisation.getId() != null) {
            throw new BadRequestAlertException("A new organisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return organisationRepository
            .save(organisation)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/organisations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /organisations/:id} : Updates an existing organisation.
     *
     * @param id the id of the organisation to save.
     * @param organisation the organisation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organisation,
     * or with status {@code 400 (Bad Request)} if the organisation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organisation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organisations/{id}")
    public Mono<ResponseEntity<Organisation>> updateOrganisation(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Organisation organisation
    ) throws URISyntaxException {
        log.debug("REST request to update Organisation : {}, {}", id, organisation);
        if (organisation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organisation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return organisationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return organisationRepository
                    .save(organisation.setIsPersisted())
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
     * {@code PATCH  /organisations/:id} : Partial updates given fields of an existing organisation, field will ignore if it is null
     *
     * @param id the id of the organisation to save.
     * @param organisation the organisation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organisation,
     * or with status {@code 400 (Bad Request)} if the organisation is not valid,
     * or with status {@code 404 (Not Found)} if the organisation is not found,
     * or with status {@code 500 (Internal Server Error)} if the organisation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organisations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Organisation>> partialUpdateOrganisation(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Organisation organisation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Organisation partially : {}, {}", id, organisation);
        if (organisation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organisation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return organisationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Organisation> result = organisationRepository
                    .findById(organisation.getId())
                    .map(existingOrganisation -> {
                        if (organisation.getName() != null) {
                            existingOrganisation.setName(organisation.getName());
                        }
                        if (organisation.getDescription() != null) {
                            existingOrganisation.setDescription(organisation.getDescription());
                        }

                        return existingOrganisation;
                    })
                    .flatMap(organisationRepository::save);

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
     * {@code GET  /organisations} : get all the organisations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organisations in body.
     */
    @GetMapping("/organisations")
    public Mono<List<Organisation>> getAllOrganisations() {
        log.debug("REST request to get all Organisations");
        return organisationRepository.findAll().collectList();
    }

    /**
     * {@code GET  /organisations} : get all the organisations as a stream.
     * @return the {@link Flux} of organisations.
     */
    @GetMapping(value = "/organisations", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Organisation> getAllOrganisationsAsStream() {
        log.debug("REST request to get all Organisations as a stream");
        return organisationRepository.findAll();
    }

    /**
     * {@code GET  /organisations/:id} : get the "id" organisation.
     *
     * @param id the id of the organisation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organisation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organisations/{id}")
    public Mono<ResponseEntity<Organisation>> getOrganisation(@PathVariable String id) {
        log.debug("REST request to get Organisation : {}", id);
        Mono<Organisation> organisation = organisationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(organisation);
    }

    /**
     * {@code DELETE  /organisations/:id} : delete the "id" organisation.
     *
     * @param id the id of the organisation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organisations/{id}")
    public Mono<ResponseEntity<Void>> deleteOrganisation(@PathVariable String id) {
        log.debug("REST request to delete Organisation : {}", id);
        return organisationRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
