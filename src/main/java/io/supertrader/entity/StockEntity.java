package io.supertrader.entity;

public class StockEntity {
    private String stockId;
    private String description;
    private int magicValue;

    public String getStockId() {
        return stockId;
    }

    public String getDescription() {
        return description;
    }

    public int getMagicValue() {
        return magicValue;
    }

    public StockEntity(String stockId, String description, int magicValue) {
        this.stockId = stockId;
        this.description = description;
        this.magicValue = magicValue;
    }

}
