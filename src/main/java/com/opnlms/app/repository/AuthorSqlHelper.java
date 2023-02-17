package com.opnlms.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AuthorSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("first_name", table, columnPrefix + "_first_name"));
        columns.add(Column.aliased("last_name", table, columnPrefix + "_last_name"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("password", table, columnPrefix + "_password"));
        columns.add(Column.aliased("create_date", table, columnPrefix + "_create_date"));
        columns.add(Column.aliased("avatar_url", table, columnPrefix + "_avatar_url"));
        columns.add(Column.aliased("activated", table, columnPrefix + "_activated"));
        columns.add(Column.aliased("lang_key", table, columnPrefix + "_lang_key"));
        columns.add(Column.aliased("reset_date", table, columnPrefix + "_reset_date"));
        columns.add(Column.aliased("reset_key", table, columnPrefix + "_reset_key"));
        columns.add(Column.aliased("authorities", table, columnPrefix + "_authorities"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("course_id", table, columnPrefix + "_course_id"));

        return columns;
    }
}
