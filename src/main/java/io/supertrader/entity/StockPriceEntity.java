package io.supertrader.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockPriceEntity {
    private int id;
    //private StockEntity stock;
    private String stockId;
    private LocalDateTime priceTs;
    private BigDecimal stockPrice;


    public StockPriceEntity(/*StockEntity stock,*/ String stockId, LocalDateTime priceTs, BigDecimal stockPrice) {
        //this.stock = stock;
        this.stockId = stockId;
        this.priceTs = priceTs;
        this.stockPrice = stockPrice;
    }

    public String getStockId() {
        return stockId;
    }

    public LocalDateTime getPriceTs() {
        return priceTs;
    }

    public BigDecimal getStockPrice() {
        return stockPrice;
    }
}
