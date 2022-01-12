package app

import app.converter.Converter
import app.infor.service.BalanceService
import app.infor.service.OrderService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

val baseDir = "/Users/dvalyaev/mega/ya/infor/sql"

@SpringBootApplication
class ConverterApp(
    val converter: Converter,
    val balanceService: BalanceService,
    val orderService: OrderService,
) : CommandLineRunner {


    val base = "$baseDir/base.xls"
    val packing_1x20 = "$baseDir/packing_1x20-2.xls"
    val packing_10x1 = "$baseDir/packing_10x1.xls"
    val packing_withdrawal = "$baseDir/packing_withdrawal.xls"
    val packing_single_10 = "$baseDir/packing_single_10.xls"
    val packing_nonsort_10 = "$baseDir/packing_nonsort_10.xls"
    val packing_nonpack = "$baseDir/packing_nonpack.xls"
    val packing_nonsort_oversize = "$baseDir/packing_nonsort_oversize.xls"
    val packing_nonsort_withdrawal = "$baseDir/packing_nonsort_withdrawal.xls"
    val packing_mixed = "$baseDir/packing_mixed.xls"
    val dropping_1x4 = "$baseDir/dropping_1x4.xls"
    val dropping_withdrawals = "$baseDir/dropping_withdrawals.xls"
    val shipping_1x2x2 = "$baseDir/shipping_1x2x2.xls"
    val receivingCrossdock = "$baseDir/receiving_crossdock.xls"
    val receivingDefault = "$baseDir/receiving_default.xls"
    val test_shipping_load = "$baseDir/tests/shipping-load.wmwhse1.xls"
    val lot_split_and_hold = "$baseDir/lot_split_and_hold.xls"
    val transfer = "$baseDir/transfer.xls"
    val replenishment = "$baseDir/replenishment.xls"
    val nonsort1 = "$baseDir/nonsort_1.xls"
    val nonsort2 = "$baseDir/nonsort_2.xls"
    val nonsort_single_10 = "$baseDir/nonsort_single_10.xls"
    val outbound23 = "$baseDir/outbound23.xls"

    override fun run(args: Array<String>) {
        converter
            .read(base)
            .read(nonsort1)
            .applyToDatabase()

        balanceService.fixBalances()
        orderService.setScheduledShipDateFromShipmentDateTime()
    }
}

fun main(args: Array<String>) {
    runApplication<ConverterApp>(*args)
}
