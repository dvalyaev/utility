package ru.yandex.market.v1.writer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.yandex.market.v1.table.Field;
import ru.yandex.market.v1.table.Header;
import ru.yandex.market.v1.table.Table;

public class SqlWriter extends DbDataWriter {

    @Override
    public void write(List<Table> tables, String target) {
        LinkedList<String> deletes = new LinkedList<>();
        List<String> inserts = new ArrayList<>();

        tables.forEach(table -> {
            createDelete(table).ifPresent(deletes::add);
            createInsert(table).ifPresent(inserts::add);
        });

        List<String> lines = new ArrayList<>();
        deletes.descendingIterator().forEachRemaining(lines::add);
        lines.addAll(inserts);

        writeFile(lines, target);
    }

    private Optional<String> createDelete(Table table) {
        List<Header> headers = table.getHeaders();
        String delete = headers.stream().anyMatch(Header::isTruncate)
            ? String.format("DELETE FROM %s.%s\n", table.getSchema(), table.getName())
            : String.format("DELETE FROM %s.%s\nWHERE %s;\n", table.getSchema(), table.getName(),
            table.getRows().stream()
                .map(row -> Stream.iterate(0, i -> i + 1)
                    .limit(headers.size())
                    .filter(i -> headers.get(i).isDelete())
                    .map(i -> headers.get(i).getName() + "=" + row.getField(i).getSqlValue())
                    .collect(Collectors.joining(" AND ", "(", ")"))
                )
                .distinct()
                .collect(Collectors.joining("\n   OR "))
        );
        return Optional.of(delete);
    }

    private Optional<String> createInsert(Table table) {
        if (table.getRows().isEmpty()) {
            return Optional.empty();
        }
        String insert = String.format("INSERT INTO %s.%s (%s) VALUES \n%s;\n", table.getSchema(), table.getName(),
            table.getHeaders().stream().map(Header::getName).collect(Collectors.joining(",")),
            table.getRows().stream()
                .map(row -> row.getFields().stream()
                    .map(Field::getSqlValue)
                    .collect(Collectors.joining(",", "   (", ")")))
                .collect(Collectors.joining(",\n"))
        );
        return Optional.of(insert);
    }
}
