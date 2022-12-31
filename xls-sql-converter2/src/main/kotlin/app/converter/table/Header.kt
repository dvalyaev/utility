package app.converter.table

class Header(
    val name: String,
    val isDelete: Boolean = false,
    val isTruncate: Boolean = false,
    val isUpdate: Boolean = false,
) {
    override fun toString(): String = "Column{$name}"
}