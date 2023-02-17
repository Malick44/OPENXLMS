package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Instructor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Instructor}, with proper type conversions.
 */
@Service
public class InstructorRowMapper implements BiFunction<Row, String, Instructor> {

    private final ColumnConverter converter;

    public InstructorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Instructor} stored in the database.
     */
    @Override
    public Instructor apply(Row row, String prefix) {
        Instructor entity = new Instructor();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setCourseId(converter.fromRow(row, prefix + "_course_id", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setInstructorUrl(converter.fromRow(row, prefix + "_instructor_url", String.class));
        entity.setInstructorThumbnail(converter.fromRow(row, prefix + "_instructor_thumbnail", String.class));
        entity.setInstructorRating(converter.fromRow(row, prefix + "_instructor_rating", String.class));
        entity.setInstructorRatingCount(converter.fromRow(row, prefix + "_instructor_rating_count", String.class));
        entity.setInstructorTotalStudents(converter.fromRow(row, prefix + "_instructor_total_students", String.class));
        entity.setInstructorTotalReviews(converter.fromRow(row, prefix + "_instructor_total_reviews", String.class));
        entity.setRatingCount(converter.fromRow(row, prefix + "_rating_count", String.class));
        return entity;
    }
}
