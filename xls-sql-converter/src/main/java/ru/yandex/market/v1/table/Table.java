package ru.yandex.market.v1.table;

import java.util.List;

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
