package app.converter.writer

import app.converter.table.Header
import app.converter.table.Table
import java.lang.Exception

class SqlWriter : DbDataWriter() {

    override fun write(tables: List<Table>, target: String) {
        writeFile(getQueries(tables), target)
    }

    private fun getQueries(tables: List<Table>): List<String> {
        val deletes = mutableListOf<String>()
        val inserts = mutableListOf<String>()
        val updates = mutableListOf<String>()
        tables.forEach {
            deletes += createDelete(it)
            inserts += createInsert(it)
            updates += createUpdate(it)
        }
        return deletes.asReversed() + inserts + updates
    }

    private fun createDelete(table: Table): List<String> = try {
        val headers = table.headers
        when {
            headers.any { it.isTruncate } -> listOf("DELETE FROM ${table.fullName};\n")
            headers.any { it.isDelete } -> {
                val keys = table.rows
                    .map { row ->
                        headers.indices.asSequence()
                            .filter { headers[it].isDelete }
                            .joinToString(" AND ", "(", ")") {
                                headers[it].name + "=" + row.field(it).sqlValue
                            }
                    }
                    .distinct()
                    .joinToString("\n   OR ")
                listOf("DELETE FROM ${table.fullName}\nWHERE $keys;\n")
            }
            else -> listOf()
        }
    } catch (e: Exception) {
        throw RuntimeException(table.toString(), e)
    }

    private fun createUpdate(table: Table): List<String> {
        val headers: List<Header> = table.headers
        val (keyIndices, valueIndices) = headers.indices.partition { headers[it].isUpdate }
        if (table.rows.isEmpty() || keyIndices.isEmpty()) {
            return listOf()
        }
        return table.rows
            .map { row ->
                val values = valueIndices.joinToString(", ") { "${headers[it].name} = ${row.field(it).sqlValue}" }
                val keys = keyIndices.joinToString(" AND ") { "${headers[it].name} = ${row.field(it).sqlValue}" }
                "UPDATE ${table.fullName}\nSET $values\nWHERE $keys;\n";
            };
    }

    private fun createInsert(table: Table): List<String> {
        if (table.rows.isEmpty() || table.headers.any { it.isUpdate }) {
            return listOf()
        }
        val columnNames = table.headers.joinToString { it.name }
        val values = table.rows.joinToString(",\n") { row ->
            row.fields.joinToString(", ", "   (", ")") { it.sqlValue }
        }
        return listOf("INSERT INTO ${table.fullName} ($columnNames) VALUES \n$values;\n")
    }
}
