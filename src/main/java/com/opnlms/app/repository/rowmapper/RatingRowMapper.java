package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Rating;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Rating}, with proper type conversions.
 */
@Service
public class RatingRowMapper implements BiFunction<Row, String, Rating> {

    private final ColumnConverter converter;

    public RatingRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Rating} stored in the database.
     */
    @Override
    public Rating apply(Row row, String prefix) {
        Rating entity = new Rating();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", String.class));
        entity.setCourseId(converter.fromRow(row, prefix + "_course_id", String.class));
        entity.setInstractorId(converter.fromRow(row, prefix + "_instractor_id", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", Integer.class));
        entity.setComment(converter.fromRow(row, prefix + "_comment", String.class));
        entity.setTimestamp(converter.fromRow(row, prefix + "_timestamp", Instant.class));
        return entity;
    }
}
