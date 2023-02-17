package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Quizz;
import com.opnlms.app.repository.QuizzRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Quizz}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuizzResource {

    private final Logger log = LoggerFactory.getLogger(QuizzResource.class);

    private static final String ENTITY_NAME = "quizz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizzRepository quizzRepository;

    public QuizzResource(QuizzRepository quizzRepository) {
        this.quizzRepository = quizzRepository;
    }

    /**
     * {@code POST  /quizzes} : Create a new quizz.
     *
     * @param quizz the quizz to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizz, or with status {@code 400 (Bad Request)} if the quizz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quizzes")
    public Mono<ResponseEntity<Quizz>> createQuizz(@Valid @RequestBody Quizz quizz) throws URISyntaxException {
        log.debug("REST request to save Quizz : {}", quizz);
        if (quizz.getId() != null) {
            throw new BadRequestAlertException("A new quizz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return quizzRepository
            .save(quizz)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/quizzes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /quizzes/:id} : Updates an existing quizz.
     *
     * @param id the id of the quizz to save.
     * @param quizz the quizz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizz,
     * or with status {@code 400 (Bad Request)} if the quizz is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quizzes/{id}")
    public Mono<ResponseEntity<Quizz>> updateQuizz(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Quizz quizz
    ) throws URISyntaxException {
        log.debug("REST request to update Quizz : {}, {}", id, quizz);
        if (quizz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return quizzRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return quizzRepository
                    .save(quizz.setIsPersisted())
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
     * {@code PATCH  /quizzes/:id} : Partial updates given fields of an existing quizz, field will ignore if it is null
     *
     * @param id the id of the quizz to save.
     * @param quizz the quizz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizz,
     * or with status {@code 400 (Bad Request)} if the quizz is not valid,
     * or with status {@code 404 (Not Found)} if the quizz is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quizzes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Quizz>> partialUpdateQuizz(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Quizz quizz
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quizz partially : {}, {}", id, quizz);
        if (quizz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return quizzRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Quizz> result = quizzRepository
                    .findById(quizz.getId())
                    .map(existingQuizz -> {
                        if (quizz.getUserId() != null) {
                            existingQuizz.setUserId(quizz.getUserId());
                        }
                        if (quizz.getCourseId() != null) {
                            existingQuizz.setCourseId(quizz.getCourseId());
                        }
                        if (quizz.getTitle() != null) {
                            existingQuizz.setTitle(quizz.getTitle());
                        }
                        if (quizz.getSectionId() != null) {
                            existingQuizz.setSectionId(quizz.getSectionId());
                        }
                        if (quizz.getExamDate() != null) {
                            existingQuizz.setExamDate(quizz.getExamDate());
                        }
                        if (quizz.getNumberOfQuestions() != null) {
                            existingQuizz.setNumberOfQuestions(quizz.getNumberOfQuestions());
                        }
                        if (quizz.getTimeLimit() != null) {
                            existingQuizz.setTimeLimit(quizz.getTimeLimit());
                        }
                        if (quizz.getScore() != null) {
                            existingQuizz.setScore(quizz.getScore());
                        }

                        return existingQuizz;
                    })
                    .flatMap(quizzRepository::save);

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
     * {@code GET  /quizzes} : get all the quizzes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizzes in body.
     */
    @GetMapping("/quizzes")
    public Mono<List<Quizz>> getAllQuizzes() {
        log.debug("REST request to get all Quizzes");
        return quizzRepository.findAll().collectList();
    }

    /**
     * {@code GET  /quizzes} : get all the quizzes as a stream.
     * @return the {@link Flux} of quizzes.
     */
    @GetMapping(value = "/quizzes", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Quizz> getAllQuizzesAsStream() {
        log.debug("REST request to get all Quizzes as a stream");
        return quizzRepository.findAll();
    }

    /**
     * {@code GET  /quizzes/:id} : get the "id" quizz.
     *
     * @param id the id of the quizz to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizz, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quizzes/{id}")
    public Mono<ResponseEntity<Quizz>> getQuizz(@PathVariable String id) {
        log.debug("REST request to get Quizz : {}", id);
        Mono<Quizz> quizz = quizzRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(quizz);
    }

    /**
     * {@code DELETE  /quizzes/:id} : delete the "id" quizz.
     *
     * @param id the id of the quizz to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quizzes/{id}")
    public Mono<ResponseEntity<Void>> deleteQuizz(@PathVariable String id) {
        log.debug("REST request to delete Quizz : {}", id);
        return quizzRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
