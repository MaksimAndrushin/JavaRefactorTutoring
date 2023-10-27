package io.supertrader.utils;

import io.supertrader.entity.StockEntity;

public class StockInfoGenerator {
    public static final String GAZPROM_TICKER = "GAZP";
    public static final String GAZPROM_STOCK_DESCRIPTION = "Gazprom stock";

    public static StockEntity generateStockInfo() {
        return new StockEntity(GAZPROM_TICKER, GAZPROM_STOCK_DESCRIPTION,
                StockMagicValue.generateMagicValueFrom1To1000());
    }
}
