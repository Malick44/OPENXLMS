package com.opnlms.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class InstructorSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("course_id", table, columnPrefix + "_course_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("instructor_url", table, columnPrefix + "_instructor_url"));
        columns.add(Column.aliased("instructor_thumbnail", table, columnPrefix + "_instructor_thumbnail"));
        columns.add(Column.aliased("instructor_rating", table, columnPrefix + "_instructor_rating"));
        columns.add(Column.aliased("instructor_rating_count", table, columnPrefix + "_instructor_rating_count"));
        columns.add(Column.aliased("instructor_total_students", table, columnPrefix + "_instructor_total_students"));
        columns.add(Column.aliased("instructor_total_reviews", table, columnPrefix + "_instructor_total_reviews"));
        columns.add(Column.aliased("rating_count", table, columnPrefix + "_rating_count"));

        return columns;
    }
}
