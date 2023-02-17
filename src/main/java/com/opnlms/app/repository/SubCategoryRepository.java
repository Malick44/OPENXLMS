package com.opnlms.app.repository;

import com.opnlms.app.domain.SubCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SubCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubCategoryRepository extends ReactiveCrudRepository<SubCategory, String>, SubCategoryRepositoryInternal {
    @Override
    <S extends SubCategory> Mono<S> save(S entity);

    @Override
    Flux<SubCategory> findAll();

    @Override
    Mono<SubCategory> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface SubCategoryRepositoryInternal {
    <S extends SubCategory> Mono<S> save(S entity);

    Flux<SubCategory> findAllBy(Pageable pageable);

    Flux<SubCategory> findAll();

    Mono<SubCategory> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SubCategory> findAllBy(Pageable pageable, Criteria criteria);

}
