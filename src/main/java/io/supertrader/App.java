package io.supertrader;

import io.supertrader.infra.ConnectionUtil;
import io.supertrader.infra.DbMigration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

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
                // 3. Get stocks info
                Result result = getResult();
                // 4. Update DB
                PreparedStatement selectStockByIdStatement = getPreparedStatement(connection, result);

                // 5. Stocks data processing
                Result2 result2 = getResult2(connection, result, selectStockByIdStatement);

                makeDecision(result2);

                Thread.sleep(2000);

            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void makeDecision(Result2 result2) {
        if (result2.avgPrice().divide(BigDecimal.valueOf(result2.processedMagicValue()), RoundingMode.FLOOR).compareTo(BigDecimal.TEN) > 0) {
            System.out.println("BUY BUY BUY BUY");
        } else {
            System.out.println("SELL SELL SELL");
        }
    }

    private static Result2 getResult2(Connection connection, Result result, PreparedStatement selectStockByIdStatement) throws SQLException {
        selectStockByIdStatement.setString(1, result.stockId());
        ResultSet stockForProcessingRS = selectStockByIdStatement.executeQuery();

        stockForProcessingRS.next();

        int processedMagicValue = stockForProcessingRS.getInt("magic_value");

        System.out.println(String.format("Processing stock [%s] - (%s), MV = %d",
                stockForProcessingRS.getString("id"),
                stockForProcessingRS.getString("description"),
                processedMagicValue
        ));


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

    private record Result2(int processedMagicValue, BigDecimal avgPrice) {
    }

    private static PreparedStatement getPreparedStatement(Connection connection, Result result) throws SQLException {
        String sqlSelectStockById = """
                SELECT id, magic_value, description FROM stocks
                       WHERE id = ?
                """;
        var selectStockByIdStatement = connection.prepareStatement(sqlSelectStockById);

        String sqlInsertStock = """
                INSERT INTO stocks (id, magic_value, description)
                       VALUES (?, ?, ?)
                """;
        var insertStockStatement = connection.prepareStatement(sqlInsertStock);

        String sqlUpdateStock = """
                UPDATE stocks 
                       SET magic_value = ?
                       WHERE id = ?                    
                """;
        var updateStockStatement = connection.prepareStatement(sqlUpdateStock);

        selectStockByIdStatement.setString(1, result.stockId());
        ResultSet stockFromDbRS = selectStockByIdStatement.executeQuery();
        if (stockFromDbRS.next()) {
            updateStockStatement.setInt(1, result.magicValue());
            updateStockStatement.setString(2, result.stockId());

            updateStockStatement.executeUpdate();
        } else {
            insertStockStatement.setString(1, result.stockId());
            insertStockStatement.setInt(2, result.magicValue());
            insertStockStatement.setString(3, result.description());

            insertStockStatement.executeUpdate();
        }


        String sqlInsertPrice = """
                INSERT INTO stock_prices (stock_id, price_ts, price)
                       VALUES (?, ?, ?)
                """;
        PreparedStatement insertPriceStatement = connection.prepareStatement(sqlInsertPrice);
        insertPriceStatement.setString(1, result.stockId());
        insertPriceStatement.setObject(2, result.priceTs());
        insertPriceStatement.setBigDecimal(3, result.stockPrice());

        insertPriceStatement.executeUpdate();
        return selectStockByIdStatement;
    }

    private static Result getResult() {
        String stockId = "GAZP";
        String description = "Gazprom stock";
        int magicValue = ThreadLocalRandom.current().nextInt(1, 10000);

        LocalDateTime priceTs = LocalDateTime.now();
        BigDecimal stockPrice = new BigDecimal(ThreadLocalRandom.current().nextInt(1, 10000));
        Result result = new Result(stockId, description, magicValue, priceTs, stockPrice);
        return result;
    }

    private record Result(String stockId, String description, int magicValue, LocalDateTime priceTs,
                          BigDecimal stockPrice) {
    }


}
