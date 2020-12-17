package ru.yandex.market.v1.table;

public class Header {
    private final String name;
    private final boolean isDelete;
    private final boolean isTruncate;

    public Header(String name, boolean isDelete, boolean isTruncate) {
        this.name = name;
        this.isDelete = isDelete;
        this.isTruncate = isTruncate;
    }

    public String getName() {
        return name;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public boolean isTruncate() {
        return isTruncate;
    }

    @Override
    public String toString() {
        return "Column{" + name + "}";
    }
}
