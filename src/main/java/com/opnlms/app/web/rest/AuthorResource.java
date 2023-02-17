package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Author;
import com.opnlms.app.repository.AuthorRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Author}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AuthorResource {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    private static final String ENTITY_NAME = "author";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorRepository authorRepository;

    public AuthorResource(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * {@code POST  /authors} : Create a new author.
     *
     * @param author the author to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new author, or with status {@code 400 (Bad Request)} if the author has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authors")
    public Mono<ResponseEntity<Author>> createAuthor(@Valid @RequestBody Author author) throws URISyntaxException {
        log.debug("REST request to save Author : {}", author);
        if (author.getId() != null) {
            throw new BadRequestAlertException("A new author cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return authorRepository
            .save(author)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/authors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /authors/:id} : Updates an existing author.
     *
     * @param id the id of the author to save.
     * @param author the author to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated author,
     * or with status {@code 400 (Bad Request)} if the author is not valid,
     * or with status {@code 500 (Internal Server Error)} if the author couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authors/{id}")
    public Mono<ResponseEntity<Author>> updateAuthor(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Author author
    ) throws URISyntaxException {
        log.debug("REST request to update Author : {}, {}", id, author);
        if (author.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, author.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return authorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return authorRepository
                    .save(author.setIsPersisted())
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
     * {@code PATCH  /authors/:id} : Partial updates given fields of an existing author, field will ignore if it is null
     *
     * @param id the id of the author to save.
     * @param author the author to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated author,
     * or with status {@code 400 (Bad Request)} if the author is not valid,
     * or with status {@code 404 (Not Found)} if the author is not found,
     * or with status {@code 500 (Internal Server Error)} if the author couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/authors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Author>> partialUpdateAuthor(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Author author
    ) throws URISyntaxException {
        log.debug("REST request to partial update Author partially : {}, {}", id, author);
        if (author.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, author.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return authorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Author> result = authorRepository
                    .findById(author.getId())
                    .map(existingAuthor -> {
                        if (author.getFirstName() != null) {
                            existingAuthor.setFirstName(author.getFirstName());
                        }
                        if (author.getLastName() != null) {
                            existingAuthor.setLastName(author.getLastName());
                        }
                        if (author.getEmail() != null) {
                            existingAuthor.setEmail(author.getEmail());
                        }
                        if (author.getPassword() != null) {
                            existingAuthor.setPassword(author.getPassword());
                        }
                        if (author.getCreateDate() != null) {
                            existingAuthor.setCreateDate(author.getCreateDate());
                        }
                        if (author.getAvatarUrl() != null) {
                            existingAuthor.setAvatarUrl(author.getAvatarUrl());
                        }
                        if (author.getActivated() != null) {
                            existingAuthor.setActivated(author.getActivated());
                        }
                        if (author.getLangKey() != null) {
                            existingAuthor.setLangKey(author.getLangKey());
                        }
                        if (author.getResetDate() != null) {
                            existingAuthor.setResetDate(author.getResetDate());
                        }
                        if (author.getResetKey() != null) {
                            existingAuthor.setResetKey(author.getResetKey());
                        }
                        if (author.getAuthorities() != null) {
                            existingAuthor.setAuthorities(author.getAuthorities());
                        }
                        if (author.getCreatedBy() != null) {
                            existingAuthor.setCreatedBy(author.getCreatedBy());
                        }
                        if (author.getLastModifiedBy() != null) {
                            existingAuthor.setLastModifiedBy(author.getLastModifiedBy());
                        }
                        if (author.getLastModifiedDate() != null) {
                            existingAuthor.setLastModifiedDate(author.getLastModifiedDate());
                        }
                        if (author.getCourseId() != null) {
                            existingAuthor.setCourseId(author.getCourseId());
                        }

                        return existingAuthor;
                    })
                    .flatMap(authorRepository::save);

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
     * {@code GET  /authors} : get all the authors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authors in body.
     */
    @GetMapping("/authors")
    public Mono<List<Author>> getAllAuthors() {
        log.debug("REST request to get all Authors");
        return authorRepository.findAll().collectList();
    }

    /**
     * {@code GET  /authors} : get all the authors as a stream.
     * @return the {@link Flux} of authors.
     */
    @GetMapping(value = "/authors", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Author> getAllAuthorsAsStream() {
        log.debug("REST request to get all Authors as a stream");
        return authorRepository.findAll();
    }

    /**
     * {@code GET  /authors/:id} : get the "id" author.
     *
     * @param id the id of the author to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the author, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authors/{id}")
    public Mono<ResponseEntity<Author>> getAuthor(@PathVariable String id) {
        log.debug("REST request to get Author : {}", id);
        Mono<Author> author = authorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(author);
    }

    /**
     * {@code DELETE  /authors/:id} : delete the "id" author.
     *
     * @param id the id of the author to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authors/{id}")
    public Mono<ResponseEntity<Void>> deleteAuthor(@PathVariable String id) {
        log.debug("REST request to delete Author : {}", id);
        return authorRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
