package io.supertrader.dao;

import io.supertrader.entity.StockPriceEntity;
import io.supertrader.infra.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockPriceDAO {

    private final static String sqlInsertPrice = """
            INSERT INTO stock_prices (stock_id, price_ts, price)
                   VALUES (?, ?, ?)
            """;

    private final static String sqlSelectPrices = """
            SELECT id, stock_id, price_ts, price
                 FROM stock_prices
                 WHERE stock_id = ?
                 ORDER BY price_ts ASC
            """;

    public void update(StockPriceEntity stockPrice) {
        try (var connection = ConnectionUtil.get();
             var insertPriceStatement = connection.prepareStatement(sqlInsertPrice)) {

            insertPriceStatement.setString(1, stockPrice.getStockId());
            insertPriceStatement.setObject(2, stockPrice.getPriceTs());
            insertPriceStatement.setBigDecimal(3, stockPrice.getStockPrice());

            insertPriceStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockPriceEntity> getPricesByStockId(String stockId) {

        try (var connection = ConnectionUtil.get();
             var selectStockPriceByIdStatement = connection.prepareStatement(sqlSelectPrices);) {

            selectStockPriceByIdStatement.setString(1, stockId);
            ResultSet processedPricesRS = selectStockPriceByIdStatement.executeQuery();

            ArrayList<StockPriceEntity> result = new ArrayList<>();
            while (processedPricesRS.next()) {
                StockPriceEntity stockPriceEntity = new StockPriceEntity(stockId,
                        processedPricesRS.getObject("price_ts", LocalDateTime.class),
                        processedPricesRS.getBigDecimal("price"));
                result.add(stockPriceEntity);
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
