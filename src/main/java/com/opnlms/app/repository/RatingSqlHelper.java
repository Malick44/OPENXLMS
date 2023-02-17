package com.opnlms.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RatingSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("course_id", table, columnPrefix + "_course_id"));
        columns.add(Column.aliased("instractor_id", table, columnPrefix + "_instractor_id"));
        columns.add(Column.aliased("value", table, columnPrefix + "_value"));
        columns.add(Column.aliased("comment", table, columnPrefix + "_comment"));
        columns.add(Column.aliased("timestamp", table, columnPrefix + "_timestamp"));

        return columns;
    }
}
