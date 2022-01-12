package app.scriptexecutor

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class ScriptExecutor(private val jdbc: NamedParameterJdbcTemplate) {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun execute(path: String) {
        var counter = 0
        val buffer = mutableListOf<String>()
        val start = System.currentTimeMillis()
        Files.readAllLines(Paths.get(path)).asSequence()
            .map { it.trimEnd() }
            .forEach {
                if (it.isBlank()) {
                    println(it)
                } else {
                    buffer.add(it)
                    if (it.endsWith(";")) {
                        val sql = buffer.joinToString("\n")
                        println(sql)
                        counter += jdbc.update(sql, MapSqlParameterSource())
                        buffer.clear()
                    }
                }
            }
        val time = System.currentTimeMillis() - start

        println("${LocalDateTime.now().format(formatter)} - $counter rows affected in $time ms")
    }

}