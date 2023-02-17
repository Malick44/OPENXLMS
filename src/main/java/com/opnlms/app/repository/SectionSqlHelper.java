package com.opnlms.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SectionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("course_id", table, columnPrefix + "_course_id"));
        columns.add(Column.aliased("text", table, columnPrefix + "_text"));
        columns.add(Column.aliased("video_url", table, columnPrefix + "_video_url"));
        columns.add(Column.aliased("video_id", table, columnPrefix + "_video_id"));
        columns.add(Column.aliased("video_duration", table, columnPrefix + "_video_duration"));
        columns.add(Column.aliased("video_title", table, columnPrefix + "_video_title"));
        columns.add(Column.aliased("video_description", table, columnPrefix + "_video_description"));
        columns.add(Column.aliased("video_channel_language", table, columnPrefix + "_video_channel_language"));

        return columns;
    }
}
