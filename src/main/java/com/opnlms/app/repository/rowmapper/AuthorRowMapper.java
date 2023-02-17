package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Author;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Author}, with proper type conversions.
 */
@Service
public class AuthorRowMapper implements BiFunction<Row, String, Author> {

    private final ColumnConverter converter;

    public AuthorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Author} stored in the database.
     */
    @Override
    public Author apply(Row row, String prefix) {
        Author entity = new Author();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setFirstName(converter.fromRow(row, prefix + "_first_name", String.class));
        entity.setLastName(converter.fromRow(row, prefix + "_last_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setPassword(converter.fromRow(row, prefix + "_password", String.class));
        entity.setCreateDate(converter.fromRow(row, prefix + "_create_date", Instant.class));
        entity.setAvatarUrl(converter.fromRow(row, prefix + "_avatar_url", String.class));
        entity.setActivated(converter.fromRow(row, prefix + "_activated", Boolean.class));
        entity.setLangKey(converter.fromRow(row, prefix + "_lang_key", String.class));
        entity.setResetDate(converter.fromRow(row, prefix + "_reset_date", Instant.class));
        entity.setResetKey(converter.fromRow(row, prefix + "_reset_key", String.class));
        entity.setAuthorities(converter.fromRow(row, prefix + "_authorities", String.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCourseId(converter.fromRow(row, prefix + "_course_id", String.class));
        return entity;
    }
}
