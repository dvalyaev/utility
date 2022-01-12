package ru.yandex.market.v2;

import java.time.Duration;
import java.time.LocalTime;

public class Test {
    public static void main(String[] args) {
        LocalTime time = LocalTime.parse("04:35");

        System.out.println(Duration.ofSeconds(time.toSecondOfDay()));
        System.out.println(Test.class.getSimpleName());
    }
}
