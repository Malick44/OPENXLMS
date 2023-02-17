package com.opnlms.app.repository;

import com.opnlms.app.domain.Rating;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends ReactiveCrudRepository<Rating, String>, RatingRepositoryInternal {
    @Override
    <S extends Rating> Mono<S> save(S entity);

    @Override
    Flux<Rating> findAll();

    @Override
    Mono<Rating> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface RatingRepositoryInternal {
    <S extends Rating> Mono<S> save(S entity);

    Flux<Rating> findAllBy(Pageable pageable);

    Flux<Rating> findAll();

    Mono<Rating> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Rating> findAllBy(Pageable pageable, Criteria criteria);

}
