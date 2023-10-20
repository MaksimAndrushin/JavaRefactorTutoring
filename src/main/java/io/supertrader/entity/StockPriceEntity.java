package io.supertrader.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockPriceEntity {
    private int id;
    private StockEntity stock;
    private LocalDateTime priceTs;
    private BigDecimal stockPrice;


    public StockPriceEntity(StockEntity stock, LocalDateTime priceTs, BigDecimal stockPrice) {
        this.stock = stock;
        this.priceTs = priceTs;
        this.stockPrice = stockPrice;
    }

    public StockEntity getStock() {
        return stock;
    }

    public LocalDateTime getPriceTs() {
        return priceTs;
    }

    public BigDecimal getStockPrice() {
        return stockPrice;
    }
}
