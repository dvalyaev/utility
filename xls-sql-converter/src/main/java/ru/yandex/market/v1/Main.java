package ru.yandex.market.v1;


/*
delete from wmwhse1.RECEIPTDETAIL
delete from wmwhse1.ADJUSTMENTDETAIL
 */
public class Main {
    public static void main(String[] args) {
        String packing = "/Users/dvalyaev/mega/ya/infor/sql/packing.wmwhse1.xls";
        String packing_2orders = "/Users/dvalyaev/mega/ya/infor/sql/packing-2orders.wmwhse1.xls";
        String packing_10orders = "/Users/dvalyaev/mega/ya/infor/sql/packing-10orders.wmwhse1.xls";
        String shipping = "/Users/dvalyaev/mega/ya/infor/sql/shipping.wmwhse1.xls";

        String test_shipping_load = "/Users/dvalyaev/mega/ya/infor/sql/tests/shipping-load.wmwhse1.xls";


        Converter.read(packing_10orders).saveAsSql().saveAsDbUnitXml();
    }
}
