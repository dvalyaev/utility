package ru.yandex.market.v2;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import ru.yandex.market.v2.reader.XlsReader;
import ru.yandex.market.v2.scriptexecutor.ScriptExecutor;
import ru.yandex.market.v2.table.Table;
import ru.yandex.market.v2.writer.DbDataWriter;
import ru.yandex.market.v2.writer.DbUnitXmlWriter;
import ru.yandex.market.v2.writer.SqlWriter;

public class Converter {
    private static final String DEFAULT_TARGET_SQL = "output.sql";
    private static final String DEFAULT_TARGET_DB_UNIT_XML = "output.xml";
    private final List<String> sources = new ArrayList<>();
    private final List<List<Table>> tables = new ArrayList<>();
    private String sqlScriptPath;

    public Converter read(String inputPath) {
        this.sources.add(inputPath);
        this.tables.add(new XlsReader(inputPath).read());
        sqlScriptPath = null;
        return this;
    }

    public Converter saveAsSql(String target) {
        save(new SqlWriter(), target);
        sqlScriptPath = target;
        return this;
    }

    public Converter saveAsSql() {
        return saveAsSql(DEFAULT_TARGET_SQL);
    }

    public Converter saveAsDbUnitXml(String target) {
        return save(new DbUnitXmlWriter(), target);
    }

    public Converter saveAsDbUnitXml() {
        return saveAsDbUnitXml(DEFAULT_TARGET_DB_UNIT_XML);
    }

    public Converter applyToDatabase(DataSource dataSource) {
        if (sqlScriptPath == null) {
            saveAsSql();
        }
        ScriptExecutor scriptExecutor = new ScriptExecutor(dataSource);
        scriptExecutor.execute(sqlScriptPath);
        return this;
    }

    private Converter save(DbDataWriter writer, String target) {
        if (sources.stream().anyMatch(s -> s.equalsIgnoreCase(target))) {
            throw new IllegalArgumentException("Target file is source file");
        }
        writer.write(tables, target);
        return this;
    }
}
