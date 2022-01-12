package app.converter.table

class Table constructor(val schema: String, val name: String, val headers: List<Header>, val rows: List<Row>) {
    val fullName: String = if (schema.isBlank()) name else "$schema.$name"
}