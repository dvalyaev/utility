package app

import app.converter.Converter
import app.infor.service.OrderService
import app.infor.service.FixDbService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val baseDir = "/Users/dvalyaev/mega/ya/infor/sql"

@SpringBootApplication
class ConverterApp(
    val converter: Converter,
    val fixDbService: FixDbService,
) : CommandLineRunner {


    val base = "$baseDir/base.xls"
    val packing_1x20 = "$baseDir/packing_1x20-2.xls"
    val packing_1x200 = "$baseDir/packing_1x200.xls"
    val packing_1x1000 = "$baseDir/packing_1x1000.xls"
    val packing_1x5000 = "$baseDir/packing_1x5000.xls"
    val packing_10x1 = "$baseDir/packing_10x1.xls"
    val packing_5x2 = "$baseDir/packing_5x2.xls"
    val packing_withdrawal = "$baseDir/packing_withdrawal.xls"
    val packing_single_10 = "$baseDir/packing_single_10.xls"
    val packing_oversize_1 = "$baseDir/packing_oversize_1.xls" // КГТ волна
    val packing_nonsort_10 = "$baseDir/packing_nonsort_10.xls"
    val packing_nonpack = "$baseDir/packing_nonpack.xls"
    val packing_nonsort_oversize = "$baseDir/packing_nonsort_oversize.xls"
    val packing_nonsort_withdrawal = "$baseDir/packing_nonsort_withdrawal.xls"
    val packing_mixed = "$baseDir/packing_mixed.xls"
    val packing_promo = "$baseDir/packing_promo.xls"
    val packing_bbxd = "$baseDir/packing_bbxd.xls"
    val dropping_1x4 = "$baseDir/dropping_1x4.xls"
    val dropping_withdrawals = "$baseDir/dropping_withdrawals.xls"
    val ship_drop_1x2x2 = "$baseDir/ship_drop_1x2x2.xls"
    val shipment_1 = "$baseDir/shipment_1.xls"
    val receivingCrossdock = "$baseDir/receiving_crossdock.xls"
    val receivingDefault = "$baseDir/receiving_default.xls"
    val cancel_order = "$baseDir/tests/cancel_order.xls"
    val cancel_split_order = "$baseDir/tests/cancel_split_order.xls"
    val lot_split_and_hold = "$baseDir/lot_split_and_hold.xls"
    val transfer = "$baseDir/transfer.xls"
    val replenishment = "$baseDir/replenishment.xls"
    val nonsort1 = "$baseDir/nonsort_1.xls"
    val nonsort2 = "$baseDir/nonsort_2.xls"
    val nonsort_single_10 = "$baseDir/nonsort_single_10.xls"
    val outbound23 = "$baseDir/outbound23.xls"
    val withdrawals = "$baseDir/withdrawals.xls"
    val split_buildings = "$baseDir/split_buildings.xls"
    val cons_line_2lines = "$baseDir/cons_line_2lines.xls"
    val cons_station_3ord_2cont = "$baseDir/cons_station_3ord_2cont.xls"
    val dark = "$baseDir/dark.xls"

    override fun run(args: Array<String>) {
        converter
            .read(base)
            .read(shipment_1)
            .applyToDatabase()

        fixDbService.fixDb()
    }
}

fun main(args: Array<String>) {
    runApplication<ConverterApp>(*args)
}
