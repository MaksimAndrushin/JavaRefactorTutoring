package io.supertrader.infra;

import java.sql.SQLException;
import java.sql.Statement;

public class DbMigration {
    private static final String SQL_CREATE_STOCK_TABLE = """
            CREATE TABLE IF NOT EXISTS stocks (
            id VARCHAR(10) PRIMARY KEY,
            magic_value INTEGER,
            description VARCHAR(255)
            )
            """;

    private static final String SQL_CREATE_PRICE_TABLE = """
            CREATE TABLE IF NOT EXISTS stock_prices (
            id SERIAL PRIMARY KEY,
            stock_id VARCHAR(10),
            price_ts TIMESTAMP,
            price NUMERIC(13,3)
            )
            """;

    public static void initDb() {
        try (var conn = ConnectionUtil.getConnection();
             var statement = conn.createStatement();) {
            statement.execute(SQL_CREATE_STOCK_TABLE);
            statement.execute(SQL_CREATE_PRICE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
