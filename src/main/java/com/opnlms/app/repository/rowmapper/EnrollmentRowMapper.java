package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Enrollment;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Enrollment}, with proper type conversions.
 */
@Service
public class EnrollmentRowMapper implements BiFunction<Row, String, Enrollment> {

    private final ColumnConverter converter;

    public EnrollmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Enrollment} stored in the database.
     */
    @Override
    public Enrollment apply(Row row, String prefix) {
        Enrollment entity = new Enrollment();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setCourseId(converter.fromRow(row, prefix + "_course_id", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", String.class));
        entity.setEnrolledDate(converter.fromRow(row, prefix + "_enrolled_date", LocalDate.class));
        entity.setCompletionRate(converter.fromRow(row, prefix + "_completion_rate", Integer.class));
        entity.setCompletedDate(converter.fromRow(row, prefix + "_completed_date", LocalDate.class));
        return entity;
    }
}
