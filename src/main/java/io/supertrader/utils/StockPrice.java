package io.supertrader.utils;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class StockPrice {

    public static BigDecimal generateStockPriceFrom1To1000() {
        return new BigDecimal(ThreadLocalRandom.current().nextInt(1, 10000));
    }

}
