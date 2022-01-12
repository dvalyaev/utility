package app.converter.writer

import app.converter.table.Table
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

abstract class DbDataWriter {
    abstract fun write(tables: List<Table>, target: String)

    protected fun writeFile(lines: List<String>, target: String) {
        Files.write(Paths.get(target), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        println("File saved: $target")
    }
}