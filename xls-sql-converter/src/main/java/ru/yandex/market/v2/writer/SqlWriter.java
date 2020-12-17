package ru.yandex.market.v2.writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.yandex.market.v2.table.Field;
import ru.yandex.market.v2.table.Header;
import ru.yandex.market.v2.table.Table;

public class SqlWriter extends DbDataWriter {

    @Override
    public void write(List<List<Table>> tables, String target) {
        List<String> lines = tables.stream()
            .flatMap(t -> getQueries(t).stream())
            .collect(Collectors.toList());
        writeFile(lines, target);
    }

    private List<String> getQueries(List<Table> tables) {
        LinkedList<String> deletes = new LinkedList<>();
        List<String> inserts = new ArrayList<>();
        List<String> updates = new ArrayList<>();

        tables.forEach(table -> {
            deletes.addAll(createDelete(table));
            inserts.addAll(createInsert(table));
            updates.addAll(createUpdate(table));
        });

        List<String> lines = new ArrayList<>();
        deletes.descendingIterator().forEachRemaining(lines::add);
        lines.addAll(inserts);
        lines.addAll(updates);
        return lines;
    }

    private List<String> createDelete(Table table) {
        try {
            List<Header> headers = table.getHeaders();
            String delete = null;
            if (headers.stream().anyMatch(Header::isTruncate)) {
                delete = String.format("DELETE FROM %s;\n", table.getFullName());
            } else if (headers.stream().anyMatch(Header::isDelete)) {
                delete = String.format("DELETE FROM %s\nWHERE %s;\n", table.getFullName(),
                    table.getRows().stream()
                        .map(row -> Stream.iterate(0, i -> i + 1)
                            .limit(headers.size())
                            .filter(i -> headers.get(i).isDelete())
                            .map(i -> headers.get(i).getName() + "=" + row.getField(i).getSqlValue())
                            .collect(Collectors.joining(" AND ", "(", ")"))
                        )
                        .distinct()
                        .collect(Collectors.joining("\n   OR ")));
            }
            return delete == null ? Collections.emptyList() : Collections.singletonList(delete);
        } catch (Exception e) {
            throw new RuntimeException(table.toString(), e);
        }
    }

    private List<String> createUpdate(Table table) {
        List<Header> headers = table.getHeaders();
        if (table.getRows().isEmpty() || headers.stream().noneMatch(Header::isUpdate)) {
            return Collections.emptyList();
        }

        List<Integer> keyIndices = Stream.iterate(0, i -> i + 1)
            .limit(headers.size())
            .filter(i -> headers.get(i).isUpdate())
            .collect(Collectors.toList());
        List<Integer> valueIndices = Stream.iterate(0, i -> i + 1)
            .limit(headers.size())
            .filter(i -> !headers.get(i).isUpdate())
            .collect(Collectors.toList());


        return table.getRows().stream()
            .map(row -> String.format("UPDATE %s\nSET %s\nWHERE %s;\n",
                table.getFullName(),
                valueIndices.stream()
                    .map(i -> String.format("%s = %s", headers.get(i).getName(), row.getField(i).getSqlValue()))
                    .collect(Collectors.joining(", ")),
                keyIndices.stream()
                    .map(i -> String.format("%s = %s", headers.get(i).getName(), row.getField(i).getSqlValue()))
                    .collect(Collectors.joining(" AND "))
            ))
            .collect(Collectors.toList());
    }

    private List<String> createInsert(Table table) {
        if (table.getRows().isEmpty() || table.getHeaders().stream().anyMatch(Header::isUpdate)) {
            return Collections.emptyList();
        }
        String insert = String.format("INSERT INTO %s (%s) VALUES \n%s;\n", table.getFullName(),
            table.getHeaders().stream().map(Header::getName).collect(Collectors.joining(",")),
            table.getRows().stream()
                .map(row -> row.getFields().stream()
                    .map(Field::getSqlValue)
                    .collect(Collectors.joining(",", "   (", ")")))
                .collect(Collectors.joining(",\n"))
        );
        return Collections.singletonList(insert);
    }
}
