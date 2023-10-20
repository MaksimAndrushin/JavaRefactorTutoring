package io.supertrader.utils;

import java.time.LocalDateTime;

public class StockChangeTime {

    public static LocalDateTime generateStockLastChangeTimestamp() {
        return LocalDateTime.now();
    }

}
