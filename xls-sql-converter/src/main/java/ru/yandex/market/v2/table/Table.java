package ru.yandex.market.v2.table;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Table {
    private final String schema;
    private final String name;
    private final List<Header> headers;
    private final List<Row> rows;

    public Table(String schema, String name, List<Header> headers, List<Row> rows) {
        this.schema = schema;
        this.name = name;
        this.headers = headers;
        this.rows = rows;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return StringUtils.isBlank(schema) ? name : schema + "." + name;
    }


    public List<Header> getHeaders() {
        return headers;
    }

    public List<Row> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "Table{" +
            "schema='" + schema + '\'' +
            ", name='" + name + '\'' +
            ", columns=" + headers +
            ", rows=" + rows +
            '}';
    }
}
