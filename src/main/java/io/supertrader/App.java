package io.supertrader;

import io.supertrader.dao.StockInfoDAO;
import io.supertrader.dao.StockPriceDAO;
import io.supertrader.entity.StockEntity;
import io.supertrader.entity.StockPriceEntity;
import io.supertrader.infra.ConnectionUtil;
import io.supertrader.infra.DbMigration;
import io.supertrader.processor.StockProcessor;
import io.supertrader.utils.StockInfoGenerator;
import io.supertrader.utils.StockPriceGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        App app = new App();
        app.start();
    }

    private void start() throws InterruptedException {
        DbMigration.initDb();

        while (true) {
            StockEntity stockEntity = StockInfoGenerator.generateStockInfo();

            // 4. Update DB
            //PreparedStatement selectStockByIdStatement = getPreparedStatement(connection, result);
            StockInfoDAO stockInfoService = new StockInfoDAO();
            stockInfoService.upsert(stockEntity);

            StockPriceEntity stockPriceEntity = StockPriceGenerator.generateStockInfo(stockEntity.getStockId());
            StockPriceDAO stockPriceDAO = new StockPriceDAO();
            stockPriceDAO.update(stockPriceEntity);

            // 5. Stocks data processing
            StockProcessor stockProcessor = new StockProcessor(stockEntity);
            stockProcessor.processing();

            Thread.sleep(2000);
        }

    }

}
