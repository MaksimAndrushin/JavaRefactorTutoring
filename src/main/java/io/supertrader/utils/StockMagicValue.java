package io.supertrader.utils;

import java.util.concurrent.ThreadLocalRandom;

public class StockMagicValue {
    public static int generateMagicValueFrom1To1000() {
        return ThreadLocalRandom.current().nextInt(1, 10000);
    }
}
