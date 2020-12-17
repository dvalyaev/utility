package ru.yandex.market.v2.writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.yandex.market.v2.table.Header;
import ru.yandex.market.v2.table.Row;
import ru.yandex.market.v2.table.Table;

public class DbUnitXmlWriter extends DbDataWriter {
    private static final String XML_HEADER = "<?xml version=\"1.0\"?>";
    private static final String DATASET_START = "<dataset>";
    private static final String DATASET_END = "</dataset>";

    @Override
    public void write(List<List<Table>> tables, String target) {
        List<String> lines = tables.stream()
            .flatMap(t -> getQueries(t).stream())
            .collect(Collectors.toList());
        writeFile(lines, target);
    }

    public List<String> getQueries(List<Table> tables) {
        List<String> lines = new ArrayList<>();
        lines.add(XML_HEADER);
        lines.add(DATASET_START);
        lines.add("");

        for (Table table : tables) {
            lines.addAll(createXmlRows(table));
            lines.add("");
        }

        lines.add(DATASET_END);

        return lines;
    }

    private List<String> createXmlRows(Table table) {
        if (table.getRows().isEmpty()) {
            return Collections.singletonList("    <" + table.getName() + "/>");
        }

        return table.getRows().stream()
            .map(row -> createXmlRow(table.getName(), table.getHeaders(), row))
            .collect(Collectors.toList());
    }

    private String createXmlRow(String tableName, List<Header> headers, Row row) {
        return Stream.iterate(0, i -> i + 1)
            .limit(headers.size())
            .map(i -> String.format(" %s=\"%s\"", headers.get(i).getName(), row.getField(i).getRawValue()))
            .collect(Collectors.joining("", "    <" + tableName, "/>"));
    }

}
