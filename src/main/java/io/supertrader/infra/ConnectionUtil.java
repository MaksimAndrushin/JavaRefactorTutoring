package io.supertrader.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionUtil {
    private static final String USERNAME = "robot";
    private static final String PASSWORD = "1234";
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/robot_db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
