package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Section;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Section}, with proper type conversions.
 */
@Service
public class SectionRowMapper implements BiFunction<Row, String, Section> {

    private final ColumnConverter converter;

    public SectionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Section} stored in the database.
     */
    @Override
    public Section apply(Row row, String prefix) {
        Section entity = new Section();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCourseId(converter.fromRow(row, prefix + "_course_id", String.class));
        entity.setText(converter.fromRow(row, prefix + "_text", String.class));
        entity.setVideoUrl(converter.fromRow(row, prefix + "_video_url", String.class));
        entity.setVideoId(converter.fromRow(row, prefix + "_video_id", String.class));
        entity.setVideoDuration(converter.fromRow(row, prefix + "_video_duration", String.class));
        entity.setVideoTitle(converter.fromRow(row, prefix + "_video_title", String.class));
        entity.setVideoDescription(converter.fromRow(row, prefix + "_video_description", String.class));
        entity.setVideoChannelLanguage(converter.fromRow(row, prefix + "_video_channel_language", String.class));
        return entity;
    }
}
