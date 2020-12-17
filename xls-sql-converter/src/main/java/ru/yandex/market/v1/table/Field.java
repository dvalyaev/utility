package ru.yandex.market.v1.table;

public class Field {
    private static final String FORMAT = "'%s'";
    private static final String N_FORMAT = "N" + FORMAT;
    private final String rawValue;
    private final boolean quoted;

    public Field(String rawValue, boolean quoted) {
        this.rawValue = rawValue;
        this.quoted = quoted;
    }

    public static Field of(int value) {
        return of(String.valueOf(value));
    }

    public static Field of(double value) {
        return of(String.valueOf(value));
    }

    public static Field of(String value) {
        return new Field(value, false);
    }

    public static Field quoted(String value) {
        return new Field(value, true);
    }

    public String getRawValue() {
        return rawValue;
    }

    public String getSqlValue() {
        if (quoted) {
            String format = rawValue.codePoints().anyMatch(n -> n >= Byte.MAX_VALUE) ? N_FORMAT : FORMAT;
            return String.format(format, rawValue);
        } else {
            return rawValue;
        }
    }

    @Override
    public String toString() {
        return rawValue;
    }
}
