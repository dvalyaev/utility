package ru.yandex.market.v1.table;

import java.util.List;

public class Row {
    private final List<Field> fields;

    public Row(List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Field getField(int index) {
        return fields.get(index);
    }

    @Override
    public String toString() {
        return "Row" + fields;
    }
}
