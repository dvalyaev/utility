package ru.yandex.market.v2.scriptexecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class ScriptExecutor {
    private final JdbcTemplate jdbc;

    public ScriptExecutor(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public void execute(String path) {
        try {
            exec(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void exec(String path) throws IOException {
        AtomicInteger counter = new AtomicInteger();
        List<String> buffer = new ArrayList<>();
        long start = System.currentTimeMillis();
        Files.readAllLines(Paths.get(path)).stream()
            .map(String::stripTrailing)
            .forEach(s -> {
                if (s.isBlank()) {
                    System.out.println(s);
                } else {
                    buffer.add(s);
                    if (s.endsWith(";")) {
                        String sql = String.join("\n", buffer);
                        System.out.println(sql);
                        int rowsCount = jdbc.update(sql);
                        counter.addAndGet(rowsCount);
                        buffer.clear();
                    }
                }
            });
        long time = System.currentTimeMillis() - start;
        System.out.printf("%s rows affected in %s ms", counter.get(), time);
    }


}
