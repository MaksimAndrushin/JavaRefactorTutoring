package io.supertrader.dao;

import io.supertrader.entity.StockEntity;
import io.supertrader.infra.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockInfoDAO {
    private final static String sqlSelectStockById = """
            SELECT id, magic_value, description FROM stocks
                   WHERE id = ?
            """;
    private final static String sqlInsertStock = """
            INSERT INTO stocks (id, magic_value, description)
                   VALUES (?, ?, ?)
            """;
    private final static String sqlUpdateStock = """
            UPDATE stocks 
                   SET magic_value = ?
                   WHERE id = ?                    
            """;

    public static final int STOCK_ID_SELECT_PARAM_INDEX = 1;

    public void upsert(StockEntity stockEntity) {

        try (var connection = ConnectionUtil.get();
             var selectStockByIdStatement = connection.prepareStatement(sqlSelectStockById);) {
            selectStockByIdStatement.setString(STOCK_ID_SELECT_PARAM_INDEX, stockEntity.getStockId());

            ResultSet stockFromDbRS = selectStockByIdStatement.executeQuery();
            if (stockFromDbRS.next()) {
                update(stockEntity, connection);
            } else {
                insert(stockEntity, connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(StockEntity stockEntity, Connection connection) {
        try (PreparedStatement updateStockStatement = connection.prepareStatement(sqlUpdateStock)) {
            updateStockStatement.setInt(1, stockEntity.getMagicValue());
            updateStockStatement.setString(2, stockEntity.getStockId());

            updateStockStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(StockEntity stockEntity, Connection connection) {
        try (var insertStockStatement = connection.prepareStatement(sqlInsertStock)) {
            insertStockStatement.setString(1, stockEntity.getStockId());
            insertStockStatement.setInt(2, stockEntity.getMagicValue());
            insertStockStatement.setString(3, stockEntity.getDescription());

            insertStockStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
