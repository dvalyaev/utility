package app.infor.entity

class Holds(
    val lots: Set<String>,
    val locs: Set<String>,
    val ids: Set<String>,
) {
    operator fun contains(lotLocIdKey: LotLocIdKey): Boolean =
        lotLocIdKey.lot in lots
            || lotLocIdKey.loc in locs
            || lotLocIdKey.id in ids
}