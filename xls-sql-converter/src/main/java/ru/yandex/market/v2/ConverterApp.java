package ru.yandex.market.v2;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ConverterApp {
    public static void main(String[] args) {
        String base = "/Users/dvalyaev/mega/ya/infor/sql/base.xls";

        String packing_1x20 = "/Users/dvalyaev/mega/ya/infor/sql/packing_1x20.xls";
        String packing_10x1 = "/Users/dvalyaev/mega/ya/infor/sql/packing_10x1.xls";
        String packing_single_10 = "/Users/dvalyaev/mega/ya/infor/sql/packing_single_10.xls";
        String packing_nonsort_10 = "/Users/dvalyaev/mega/ya/infor/sql/packing_nonsort_10.xls";

        String receivingCrossdock = "/Users/dvalyaev/mega/ya/infor/sql/receiving_crossdock.xls";
        String receivingDefault = "/Users/dvalyaev/mega/ya/infor/sql/receiving_default.xls";

        String shipping = "/Users/dvalyaev/mega/ya/infor/sql/shipping.wmwhse1.xls";

        String test_shipping_load = "/Users/dvalyaev/mega/ya/infor/sql/tests/shipping-load.wmwhse1.xls";

        String lot_split_and_hold = "/Users/dvalyaev/mega/ya/infor/sql/lot_split_and_hold.xls";
        String transfer = "/Users/dvalyaev/mega/ya/infor/sql/transfer.xls";


        new Converter()
            .read(base)
            .read(packing_10x1)
            .saveAsSql()
            .applyToDatabase(dataSource());

    }

    private static DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://localhost:1433;database=SCPRD");
        dataSource.setUsername("sa");
        dataSource.setPassword("yourStrong(!)Password");
        return dataSource;
    }


}
