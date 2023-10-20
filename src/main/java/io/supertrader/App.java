package io.supertrader;

import io.supertrader.dao.StockInfoDAO;
import io.supertrader.entity.StockEntity;
import io.supertrader.infra.ConnectionUtil;
import io.supertrader.infra.DbMigration;
import io.supertrader.processor.StockProcessor;
import io.supertrader.utils.StockInfoGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    private void start() {
        DbMigration.initDb();

        // 2. Main loop
        try (Connection connection = ConnectionUtil.getConnection()) {
            while (true) {
                StockEntity stockEntity = StockInfoGenerator.generateStockInfo();

                // 4. Update DB
                //PreparedStatement selectStockByIdStatement = getPreparedStatement(connection, result);
                StockInfoDAO stockInfoService = new StockInfoDAO();
                stockInfoService.upsert(stockEntity);

                // 5. Stocks data processing

                StockEntity processedStockEntity = null;
                StockProcessor stockProcessor = new StockProcessor(processedStockEntity, 0, null);

                //TODO refactor
                stockProcessor.processing();
                stockProcessor.makeDecision();

//                Result2 result2 = getResult2(connection, result, selectStockByIdStatement);



                Thread.sleep(2000);

            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    private static Result2 getResult2(Connection connection, Result result, PreparedStatement selectStockByIdStatement) throws SQLException {
//        selectStockByIdStatement.setString(1, result.stockId());
//        ResultSet stockForProcessingRS = selectStockByIdStatement.executeQuery();
//
//        stockForProcessingRS.next();
//
//        int processedMagicValue = stockForProcessingRS.getInt("magic_value");
//
//        System.out.println(String.format("Processing stock [%s] - (%s), MV = %d",
//                stockForProcessingRS.getString("id"),
//                stockForProcessingRS.getString("description"),
//                processedMagicValue
//        ));


        String sqlSelectPrices = """
                SELECT id, stock_id, price_ts, price
                     FROM stock_prices
                     WHERE stock_id = ?
                     ORDER BY price_ts ASC
                """;

        PreparedStatement processedPricesStatement = connection.prepareStatement(sqlSelectPrices);
        processedPricesStatement.setString(1, result.stockId());
        ResultSet processedPricesRS = processedPricesStatement.executeQuery();

        BigDecimal avgPrice = BigDecimal.ZERO;
        int pricesCount = 0;

        while (processedPricesRS.next()) {
//                    System.out.println(String.format("Price for [%s] on [%s] is %s",
//                            processedPricesRS.getString("stock_id"),
//                            processedPricesRS.getObject("price_ts", LocalDateTime.class).toString(),
//                            processedPricesRS.getBigDecimal("price").toString()));
            avgPrice = avgPrice.add(processedPricesRS.getBigDecimal("price"));
            pricesCount++;
        }

        avgPrice = avgPrice.divide(BigDecimal.valueOf(pricesCount), RoundingMode.FLOOR);
        System.out.println(String.format("AVG Price for [%s] is %s", result.stockId(), avgPrice.toString()));
        Result2 result2 = new Result2(processedMagicValue, avgPrice);
        return result2;
    }

//    private record Result2(int processedMagicValue, BigDecimal avgPrice) {
//    }

}
