package app.infor.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class IdDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        const val SELECT = "select ID from wmwhse1.ID"
        const val INSERT = "INSERT INTO wmwhse1.ID (ID) VALUES (:id)"
    }

    fun findAllIds(): Set<String> =
        jdbc.queryForList(SELECT, mapOf<String, Any>(), String::class.java).toSet()


    fun insert(ids: Collection<String>) {
        val params = ids.map { mapOf("id" to it) }.toTypedArray()
        jdbc.batchUpdate(INSERT, params)
    }
}