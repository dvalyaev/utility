package app.infor.service

import app.infor.dao.*
import app.infor.entity.*
import org.springframework.stereotype.Service

@Service
class BalanceService(
    private val balanceDao: BalanceDao,
    private val lotLocIdDao: LotLocIdDao,
    private val skuLocDao: SkuLocDao,
    private val lotDao: LotDao,
    private val locDao: LocDao,
) {

    fun fixBalances() {
        val balances = balanceDao.getAll()
        val locs = locDao.getAll()
        val locTypeByLoc = locs.associateBy({ it.loc }, { it.locationType })

        insertLotLocId(balances)
        insertSkuLoc(balances, locTypeByLoc)
        updateLot(balances)
    }

    private fun insertLotLocId(balances: List<Balance>) = balances
        .groupBy { it.lotLocIdKey }
        .map { e ->
            LotLocId(
                lotLocIdKey = e.key,
                skuId = e.value.first().skuId,
                qty = e.value.sumOf { it.qty },
                qtyPicked = e.value.sumOf { it.qtyPicked }
            )
        }
        .let(lotLocIdDao::insert)

    private fun insertSkuLoc(balances: List<Balance>, locTypeByLoc: Map<String, String>) = balances
        .groupBy { it.skuLocKey }
        .map { e ->
            SkuLoc(
                skuLocKey = e.key,
                locType = locTypeByLoc[e.key.loc]!!,
                qty = e.value.sumOf { it.qty },
                qtyPicked = e.value.sumOf { it.qtyPicked }
            )
        }
        .let(skuLocDao::insert)

    private fun updateLot(balances: List<Balance>) = balances
        .groupBy { it.lot }
        .map { e ->
            LotQty(
                lot = e.key,
                qty = e.value.sumOf { it.qty },
                qtyPicked = e.value.sumOf { it.qtyPicked }
            )
        }
        .let(lotDao::addQty)

}

