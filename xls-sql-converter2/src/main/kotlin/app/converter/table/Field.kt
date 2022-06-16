package app.converter.table

class Field(val rawValue: String, quoted: Boolean) {
    companion object {
        fun of(value: Int): Field = of(value.toString())
        fun of(value: Double): Field = of(value.toString())
        fun of(value: Boolean): Field = of(if (value) 1 else 0)
        fun of(value: String): Field = Field(value, false)
        fun quoted(value: String): Field = Field(value, true)
    }

    val sqlValue: String =
        if (quoted) {
            val prefix = if (rawValue.codePoints().anyMatch { it >= Byte.MAX_VALUE }) "N" else ""
            "$prefix'$rawValue'"
        } else rawValue

}
