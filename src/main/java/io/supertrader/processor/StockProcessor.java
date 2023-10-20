package io.supertrader.processor;

import io.supertrader.entity.StockEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StockProcessor {

    private final StockEntity stockEntity;
    private int processedMagicValue;
    private BigDecimal avgPrice;

    public StockProcessor(StockEntity stockEntity, int processedMagicValue, BigDecimal avgPrice) {

        if(stockEntity == null) throw new RuntimeException("Stock info == null");
        if(avgPrice == null) throw new RuntimeException("Avg price == null");

        this.stockEntity = stockEntity;
        this.processedMagicValue = processedMagicValue;
        this.avgPrice = avgPrice;
    }

    public void makeDecision() {
        if (avgPrice.divide(BigDecimal.valueOf(processedMagicValue), RoundingMode.FLOOR).compareTo(BigDecimal.TEN) > 0) {
            System.out.println("BUY BUY BUY BUY");
        } else {
            System.out.println("SELL SELL SELL");
        }
    }

    public void processing(){
        System.out.println(String.format("Processing stock [%s] - (%s), MV = %d",
                stockEntity.getStockId(),
                stockEntity.getDescription(),
                processedMagicValue
        ));
    }






}
