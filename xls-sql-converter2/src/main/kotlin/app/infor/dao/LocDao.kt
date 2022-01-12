package app.infor.dao

import app.infor.entity.Loc
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LocDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        private const val SELECT = """
            select * from wmwhse1.LOC
            """
    }

    fun getAll(): List<Loc> =
        jdbc.query(SELECT, MapSqlParameterSource()) { rs, _ ->
            Loc(loc = rs.getString("LOC"), locationType = rs.getString("LOCATIONTYPE"))
        }
}