package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Enrollment;
import com.opnlms.app.repository.EnrollmentRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Enrollment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EnrollmentResource {

    private final Logger log = LoggerFactory.getLogger(EnrollmentResource.class);

    private static final String ENTITY_NAME = "enrollment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentResource(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * {@code POST  /enrollments} : Create a new enrollment.
     *
     * @param enrollment the enrollment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enrollment, or with status {@code 400 (Bad Request)} if the enrollment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enrollments")
    public Mono<ResponseEntity<Enrollment>> createEnrollment(@Valid @RequestBody Enrollment enrollment) throws URISyntaxException {
        log.debug("REST request to save Enrollment : {}", enrollment);
        if (enrollment.getId() != null) {
            throw new BadRequestAlertException("A new enrollment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return enrollmentRepository
            .save(enrollment)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/enrollments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /enrollments/:id} : Updates an existing enrollment.
     *
     * @param id the id of the enrollment to save.
     * @param enrollment the enrollment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enrollment,
     * or with status {@code 400 (Bad Request)} if the enrollment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enrollment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enrollments/{id}")
    public Mono<ResponseEntity<Enrollment>> updateEnrollment(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Enrollment enrollment
    ) throws URISyntaxException {
        log.debug("REST request to update Enrollment : {}, {}", id, enrollment);
        if (enrollment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enrollment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return enrollmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return enrollmentRepository
                    .save(enrollment.setIsPersisted())
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
     * {@code PATCH  /enrollments/:id} : Partial updates given fields of an existing enrollment, field will ignore if it is null
     *
     * @param id the id of the enrollment to save.
     * @param enrollment the enrollment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enrollment,
     * or with status {@code 400 (Bad Request)} if the enrollment is not valid,
     * or with status {@code 404 (Not Found)} if the enrollment is not found,
     * or with status {@code 500 (Internal Server Error)} if the enrollment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/enrollments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Enrollment>> partialUpdateEnrollment(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Enrollment enrollment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enrollment partially : {}, {}", id, enrollment);
        if (enrollment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enrollment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return enrollmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Enrollment> result = enrollmentRepository
                    .findById(enrollment.getId())
                    .map(existingEnrollment -> {
                        if (enrollment.getCourseId() != null) {
                            existingEnrollment.setCourseId(enrollment.getCourseId());
                        }
                        if (enrollment.getUserId() != null) {
                            existingEnrollment.setUserId(enrollment.getUserId());
                        }
                        if (enrollment.getEnrolledDate() != null) {
                            existingEnrollment.setEnrolledDate(enrollment.getEnrolledDate());
                        }
                        if (enrollment.getCompletionRate() != null) {
                            existingEnrollment.setCompletionRate(enrollment.getCompletionRate());
                        }
                        if (enrollment.getCompletedDate() != null) {
                            existingEnrollment.setCompletedDate(enrollment.getCompletedDate());
                        }

                        return existingEnrollment;
                    })
                    .flatMap(enrollmentRepository::save);

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
     * {@code GET  /enrollments} : get all the enrollments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enrollments in body.
     */
    @GetMapping("/enrollments")
    public Mono<List<Enrollment>> getAllEnrollments() {
        log.debug("REST request to get all Enrollments");
        return enrollmentRepository.findAll().collectList();
    }

    /**
     * {@code GET  /enrollments} : get all the enrollments as a stream.
     * @return the {@link Flux} of enrollments.
     */
    @GetMapping(value = "/enrollments", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Enrollment> getAllEnrollmentsAsStream() {
        log.debug("REST request to get all Enrollments as a stream");
        return enrollmentRepository.findAll();
    }

    /**
     * {@code GET  /enrollments/:id} : get the "id" enrollment.
     *
     * @param id the id of the enrollment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enrollment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enrollments/{id}")
    public Mono<ResponseEntity<Enrollment>> getEnrollment(@PathVariable String id) {
        log.debug("REST request to get Enrollment : {}", id);
        Mono<Enrollment> enrollment = enrollmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(enrollment);
    }

    /**
     * {@code DELETE  /enrollments/:id} : delete the "id" enrollment.
     *
     * @param id the id of the enrollment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enrollments/{id}")
    public Mono<ResponseEntity<Void>> deleteEnrollment(@PathVariable String id) {
        log.debug("REST request to delete Enrollment : {}", id);
        return enrollmentRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
