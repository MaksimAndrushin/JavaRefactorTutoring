package io.supertrader.processor;

import io.supertrader.dao.StockPriceDAO;
import io.supertrader.entity.StockEntity;
import io.supertrader.entity.StockPriceEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class StockProcessor {
    private final StockEntity stockEntity;
    private int processedMagicValue;

    public StockProcessor(StockEntity stockEntity) {
        if (stockEntity == null) throw new RuntimeException("Stock info == null");

        this.stockEntity = stockEntity;
        this.processedMagicValue = stockEntity.getMagicValue();
    }

    public void processing() {
        System.out.println(String.format("Processing stock [%s] - (%s), MV = %d",
                stockEntity.getStockId(),
                stockEntity.getDescription(),
                processedMagicValue
        ));

        BigDecimal stockAvgPrice = getStockAvgPrice();
        makeDecision(stockAvgPrice);
    }

    private void makeDecision(BigDecimal stockAvgPrice) {
        if (stockAvgPrice.divide(BigDecimal.valueOf(processedMagicValue), RoundingMode.FLOOR).compareTo(BigDecimal.TEN) > 0) {
            System.out.println("BUY BUY BUY BUY");
        } else {
            System.out.println("SELL SELL SELL");
        }
    }

    private BigDecimal getStockAvgPrice() {
        StockPriceDAO stockPriceDAO = new StockPriceDAO();
        List<StockPriceEntity> pricesByStockId = stockPriceDAO.getPricesByStockId(stockEntity.getStockId());

        BigDecimal avgPrice = BigDecimal.ZERO;
        for (StockPriceEntity stockPriceEntity : pricesByStockId) {
            avgPrice = avgPrice.add(stockPriceEntity.getStockPrice());
        }
        avgPrice = avgPrice.divide(BigDecimal.valueOf(pricesByStockId.size()), RoundingMode.FLOOR);

        System.out.println(String.format("AVG Price for [%s] is %s", stockEntity.getStockId(), avgPrice.toString()));

        return avgPrice;
    }
}
