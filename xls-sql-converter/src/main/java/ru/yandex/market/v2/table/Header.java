package ru.yandex.market.v2.table;

public class Header {
    private final String name;
    private final boolean isDelete;
    private final boolean isTruncate;
    private final boolean isUpdate;

    public Header(String name, boolean isDelete, boolean isTruncate, boolean isUpdate) {
        this.name = name;
        this.isDelete = isDelete;
        this.isTruncate = isTruncate;
        this.isUpdate = isUpdate;
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

    public boolean isUpdate() {
        return isUpdate;
    }

    @Override
    public String toString() {
        return "Column{" + name + "}";
    }
}
