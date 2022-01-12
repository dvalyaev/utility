package app.converter.table

class Header(
    val name: String,
    val isDelete: Boolean,
    val isTruncate: Boolean,
    val isUpdate: Boolean
) {
    override fun toString(): String = "Column{$name}"
}