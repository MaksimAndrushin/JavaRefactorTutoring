package io.supertrader.utils;

import io.supertrader.entity.StockEntity;
import io.supertrader.entity.StockPriceEntity;

public class StockPriceGenerator {
    public static final String GAZPROM_TICKER = "GAZP";
    public static final String GAZPROM_STOCK_DESCRIPTION = "Gazprom stock";
    public static StockPriceEntity generateStockInfo(String stockId) {
        return new StockPriceEntity(stockId,
                StockChangeTime.generateStockLastChangeTimestamp(),
                StockPrice.generateStockPriceFrom1To1000());
    }
}
