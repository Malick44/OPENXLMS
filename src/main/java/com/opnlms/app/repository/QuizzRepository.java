package com.opnlms.app.repository;

import com.opnlms.app.domain.Quizz;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Quizz entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizzRepository extends ReactiveCrudRepository<Quizz, String>, QuizzRepositoryInternal {
    @Override
    <S extends Quizz> Mono<S> save(S entity);

    @Override
    Flux<Quizz> findAll();

    @Override
    Mono<Quizz> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface QuizzRepositoryInternal {
    <S extends Quizz> Mono<S> save(S entity);

    Flux<Quizz> findAllBy(Pageable pageable);

    Flux<Quizz> findAll();

    Mono<Quizz> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Quizz> findAllBy(Pageable pageable, Criteria criteria);

}
