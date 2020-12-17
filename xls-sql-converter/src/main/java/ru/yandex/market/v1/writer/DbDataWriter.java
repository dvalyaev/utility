package ru.yandex.market.v1.writer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import ru.yandex.market.v1.table.Table;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public abstract class DbDataWriter {

    public abstract void write(List<Table> tables, String target);

    protected void writeFile(List<String> lines, String target) {
        try {
            Files.write(Paths.get(target), lines, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        System.out.println("File saved: " + target);
    }
}
