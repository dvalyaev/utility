package app.converter.table

class Row(val fields: List<Field>) {

    fun field(index: Int): Field {
        require(index >= 0 && index < fields.size) { toString() }
        return fields[index]
    }

    override fun toString(): String = "Row$fields"
}
