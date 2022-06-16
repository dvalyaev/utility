package app.infor.service

import app.infor.dao.BalanceDao
import app.infor.dao.IdDao
import app.infor.dao.InventoryHoldDao
import app.infor.dao.LocDao
import app.infor.dao.LotDao
import app.infor.dao.LotLocIdDao
import app.infor.dao.SkuLocDao
import app.infor.entity.Balance
import app.infor.entity.Holds
import app.infor.entity.LotLocId
import app.infor.entity.LotQty
import app.infor.entity.SkuLoc
import app.infor.enum.HoldStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.stream.Collectors.toSet

@Service
class BalanceService(
    private val balanceDao: BalanceDao,
    private val lotLocIdDao: LotLocIdDao,
    private val skuLocDao: SkuLocDao,
    private val inventoryHoldDao: InventoryHoldDao,
    private val lotDao: LotDao,
    private val locDao: LocDao,
    private val idDao: IdDao,
) {

    fun fixBalances() {
        val balances = balanceDao.getAll()
        val holds = inventoryHoldDao.getHolds()
        val locs = locDao.getAll()
        val locTypeByLoc = locs.associateBy({ it.loc }, { it.locationType })

        insertLotLocId(balances, holds)
        insertSkuLoc(balances, locTypeByLoc)
        updateLot(balances, holds)
        insertIdsIfMissing(balances)
    }

    private fun insertLotLocId(balances: List<Balance>, holds: Holds) = balances
        .groupBy { it.lotLocIdKey }
        .map { e ->
            LotLocId(
                lotLocIdKey = e.key,
                skuId = e.value.first().skuId,
                qty = e.value.sumOf { it.qty },
                qtyPicked = e.value.sumOf { it.qtyPicked },
                status = if (e.key in holds) HoldStatus.HOLD else HoldStatus.OK
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

    private fun updateLot(balances: List<Balance>, holds: Holds) = balances
        .groupBy { it.lot }
        .map { e ->
            LotQty(
                lot = e.key,
                qty = e.value.sumOf { it.qty },
                qtyPicked = e.value.sumOf { it.qtyPicked },
                qtyOnHold = balances.filter { it.lotLocIdKey in holds }.sumOf { it.qty },
                status = if (e.key in holds.lots) HoldStatus.HOLD else HoldStatus.OK
            )
        }
        .let(lotDao::addQty)

    private fun insertIdsIfMissing(balances: List<Balance>) {
        val existingIds = idDao.findAllIds()
        val idsToInsert = balances.map { it.id }.filter { it.isNotBlank() && it !in existingIds }.toSet()
        idDao.insert(idsToInsert)
    }

}

