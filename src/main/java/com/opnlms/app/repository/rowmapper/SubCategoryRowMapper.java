package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.SubCategory;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SubCategory}, with proper type conversions.
 */
@Service
public class SubCategoryRowMapper implements BiFunction<Row, String, SubCategory> {

    private final ColumnConverter converter;

    public SubCategoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SubCategory} stored in the database.
     */
    @Override
    public SubCategory apply(Row row, String prefix) {
        SubCategory entity = new SubCategory();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
