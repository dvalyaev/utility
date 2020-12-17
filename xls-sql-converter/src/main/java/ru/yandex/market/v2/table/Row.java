package ru.yandex.market.v2.table;

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
        if (index < 0 || index >= fields.size()) {
            throw new IllegalArgumentException(toString());
        }
        return fields.get(index);
    }

    @Override
    public String toString() {
        return "Row" + fields;
    }
}
