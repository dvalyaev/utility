package ru.yandex.market.v1;

import java.util.List;

import ru.yandex.market.v1.reader.XlsReader;
import ru.yandex.market.v1.table.Table;
import ru.yandex.market.v1.writer.DbDataWriter;
import ru.yandex.market.v1.writer.DbUnitXmlWriter;
import ru.yandex.market.v1.writer.SqlWriter;

public class Converter {
    private static final String DEFAULT_TARGET_SQL = "output.sql";
    private static final String DEFAULT_TARGET_DB_UNIT_XML = "output.xml";
    private final String source;
    private final List<Table> tables;

    private Converter(String source, List<Table> tables) {
        this.source = source;
        this.tables = tables;
    }

    public static Converter read(String inputPath) {
        return new Converter(inputPath, new XlsReader(inputPath).read());
    }

    public Converter saveAsSql(String target) {
        return save(new SqlWriter(), target);
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

    private Converter save(DbDataWriter writer, String target) {
        if (source.equalsIgnoreCase(target)) {
            throw new IllegalArgumentException("Target file is source file");
        }
        writer.write(tables, target);
        return this;
    }
}
