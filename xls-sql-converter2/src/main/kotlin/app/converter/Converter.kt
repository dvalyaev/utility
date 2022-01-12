package app.converter

import app.converter.reader.XlsReader
import app.converter.table.Table
import app.scriptexecutor.ScriptExecutor
import app.converter.writer.DbDataWriter
import app.converter.writer.SqlWriter
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Scope(SCOPE_PROTOTYPE)
@Component
class Converter(private val scriptExecutor: ScriptExecutor) {
    companion object {
        private const val DEFAULT_TARGET_SQL = "output.sql"
    }

    private val sources = mutableListOf<String>()
    private val tables = mutableListOf<Table>()
    private var sqlScriptPath: String? = null

    fun read(inputPath: String): Converter {
        sources.add(inputPath)
        tables.addAll(XlsReader(inputPath).read())
        sqlScriptPath = null
        return this
    }

    fun saveAsSql(target: String = DEFAULT_TARGET_SQL): Converter {
        save(SqlWriter(), target)
        sqlScriptPath = target
        return this
    }

    fun applyToDatabase(): Converter {
        if (sqlScriptPath === null) {
            saveAsSql()
        }
        scriptExecutor.execute(sqlScriptPath!!)
        return this
    }

    private fun save(writer: DbDataWriter, target: String): Converter {
        require(sources.none { it.equals(target, ignoreCase = true) }) { "Target file is source file" }
        writer.write(tables, target)
        return this
    }
}